package de.ollie.servicemonitor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Named;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.springframework.boot.json.JsonParseException;

import de.ollie.servicemonitor.configuration.MonitoringConfiguration;
import de.ollie.servicemonitor.configuration.MonitoringConfigurationToCheckRequestGroupConverter;
import de.ollie.servicemonitor.configuration.reader.YAMLConfigurationFileReader;
import de.ollie.servicemonitor.model.CheckRequestGroup;
import de.ollie.servicemonitor.model.CheckResult.Status;
import de.ollie.servicemonitor.model.MonitorResult;
import de.ollie.servicemonitor.parameter.CallParameters;
import de.ollie.servicemonitor.swing.MonitorResultTreeCellRenderer;
import de.ollie.servicemonitor.swing.MonitoringResultGroupTreeNode;
import de.ollie.servicemonitor.swing.MonitoringResultRootTreeNode;
import de.ollie.servicemonitor.swing.SingleMonitoringResultTreeNode;
import de.ollie.servicemonitor.swing.inifile.Inifile;

@Named
public class SwingRunner extends JFrame implements ActionListener, Runnable {

	static final int FONT_SIZE = 16;
	static final int FONT_SIZE_BUTTONS = FONT_SIZE;
	static final int FONT_SIZE_LABELS = FONT_SIZE;
	static final int HGAP = 3;
	static final int VGAP = 3;

	private static final String GROUP = "MainWindow";
	private static final String HEIGHT = "height";
	private static final String WIDTH = "width";
	private static final String X = "x";
	private static final String Y = "y";

	private final MessageValueReplacer messageValueReplacer;
	private final MonitorConfiguration configuration;
	private final MonitorService monitorService;
	private final MonitoringConfigurationToCheckRequestGroupConverter monitoringConfigurationToCheckRequestGroupConverter;
	private final YAMLConfigurationFileReader yamlConfigurationFileReader;

	private boolean firstRunDone = false;
	private List<CheckRequestGroup> checkRequestGroups;
	private MonitoringConfiguration monitoringConfiguration;

	private JButton buttonQuit;
	private JTree jTreeStatusView;
	private CallParameters callParameters;
	private Inifile inifile = new Inifile(System.getProperty("user.home") + "/.service-monitor.ini");

	public SwingRunner(MessageValueReplacer messageValueReplacer, MonitorConfiguration configuration,
	        MonitorService monitorService,
	        MonitoringConfigurationToCheckRequestGroupConverter monitoringConfigurationToCheckRequestGroupConverter,
	        YAMLConfigurationFileReader yamlConfigurationFileReader) {
		this.configuration = configuration;
		this.messageValueReplacer = messageValueReplacer;
		this.monitorService = monitorService;
		this.monitoringConfigurationToCheckRequestGroupConverter = monitoringConfigurationToCheckRequestGroupConverter;
		this.yamlConfigurationFileReader = yamlConfigurationFileReader;
		setTitle("Service-Monitor (" + configuration.getVersion() + ")");
		try {
			inifile.load();
			System.out.println("read inifile from: " + inifile.getFilename());
		} catch (FileNotFoundException fnfe) {
			System.out.println("Inifile not found: " + inifile.getFilename());
		} catch (IOException ioe) {
			System.out.println("Error reading inifile: " + inifile.getFilename() + ", message=" + ioe.getMessage());
		}
	}

	public void buildComponents() {
		JPanel panelMain = new JPanel(new BorderLayout(HGAP, VGAP));
		panelMain.add(createDataPanel(), BorderLayout.CENTER);
		panelMain.add(createButtonPanel(), BorderLayout.SOUTH);
		setContentPane(panelMain);
		pack();
		setBounds(
		        new Rectangle(
		                inifile.readInt(GROUP, X, 0),
		                inifile.readInt(GROUP, Y, 0),
		                inifile.readInt(GROUP, WIDTH, 640),
		                inifile.readInt(GROUP, HEIGHT, 480)));
		new Thread(this).start();
	}

	private JPanel createDataPanel() {
		JPanel p = new JPanel(new GridLayout(1, 1, HGAP, VGAP));
		p.setBorder(new EmptyBorder(VGAP, HGAP, VGAP, HGAP));
		jTreeStatusView = new JTree();
		p.add(new JScrollPane(jTreeStatusView));
		try {
			readMonitoringConfigurationFromYAMLFile();
			convertMonitoringConfigurationToCheckRequestGroups();
		} catch (Exception e) {
			e.printStackTrace();
		}
		jTreeStatusView
		        .setCellRenderer(new MonitorResultTreeCellRenderer(callParameters.getFontSize(), messageValueReplacer));
		return p;
	}

	@Override
	public void run() {
		try {
			while (isCheckedForARun()) {
				callMonitorServiceForCheckRequests();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readMonitoringConfigurationFromYAMLFile() throws JsonParseException, IOException {
		monitoringConfiguration = yamlConfigurationFileReader.read(callParameters.getConfigurationFileNames().get(0));
	}

	private void convertMonitoringConfigurationToCheckRequestGroups() {
		checkRequestGroups = monitoringConfigurationToCheckRequestGroupConverter.convert(monitoringConfiguration);
	}

	private boolean isCheckedForARun() {
		boolean b = (callParameters.getRepeatInSeconds() != null) || !firstRunDone;
		if (!firstRunDone) {
			firstRunDone = true;
		} else {
			if (callParameters.getRepeatInSeconds() != null) {
				try {
					System.out
					        .println(
					                "waiting for next run at: "
					                        + LocalDateTime.now().plusSeconds(callParameters.getRepeatInSeconds()));
					Thread.sleep(callParameters.getRepeatInSeconds() * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return b;
	}

	private void callMonitorServiceForCheckRequests() {
		TreeModel treeModel = createTreeModelFromMonitoringResults();
		jTreeStatusView.setModel(treeModel);
		((MonitoringResultRootTreeNode) treeModel.getRoot()).getGroups().forEach(group -> {
			if (group.getStatus() != Status.OK) {
				int index = ((MonitoringResultRootTreeNode) treeModel.getRoot()).getGroups().indexOf(group) + 1;
				jTreeStatusView.expandRow(index);
			}
		});
	}

	private TreeModel createTreeModelFromMonitoringResults() {
		MonitoringResultRootTreeNode root = new MonitoringResultRootTreeNode();
		root.setRunFrom(LocalDateTime.now());
		checkRequestGroups.forEach(checkRequestGroup -> {
			MonitorResult result = monitorService.monitor(checkRequestGroup.getCheckRequests());
			MonitoringResultGroupTreeNode node = new MonitoringResultGroupTreeNode(checkRequestGroup.getName());
			root.addNode(node);
			result
			        .getCheckResults()
			        .forEach(checkResult -> node.addNode(new SingleMonitoringResultTreeNode(checkResult)));
		});
		root.setRunUntil(LocalDateTime.now());
		root.setNextRun(LocalDateTime.now().plusSeconds(callParameters.getRepeatInSeconds()));
		return new DefaultTreeModel(root);
	}

	private JPanel createButtonPanel() {
		buttonQuit = new JButton("Quit");
		buttonQuit.addActionListener(this);
		Font font = buttonQuit.getFont();
		buttonQuit.setFont(new Font(font.getName(), font.getStyle(), FONT_SIZE_BUTTONS));
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, HGAP, VGAP));
		p.add(buttonQuit);
		return p;
	}

	public void setCallParameters(CallParameters callParameters) {
		this.callParameters = callParameters;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			inifile.writeInt(GROUP, X, getX());
			inifile.writeInt(GROUP, Y, getY());
			inifile.writeInt(GROUP, WIDTH, getWidth());
			inifile.writeInt(GROUP, HEIGHT, getHeight());
			inifile.save();
			System.out.println("wrote inifile to: " + inifile.getFilename());
		} catch (Exception ex) {
			System.out.println("something went wrong writing the inifile: " + ex.getMessage());
		}
		setVisible(false);
		dispose();
		System.exit(0);
	}

}