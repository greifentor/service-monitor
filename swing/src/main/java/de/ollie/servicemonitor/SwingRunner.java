package de.ollie.servicemonitor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Named;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
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

@Named
public class SwingRunner extends JFrame implements ActionListener, Runnable {

	static final int FONT_SIZE = 16;
	static final int FONT_SIZE_BUTTONS = FONT_SIZE;
	static final int FONT_SIZE_LABELS = FONT_SIZE;
	static final int HGAP = 3;
	static final int VGAP = 3;

	private final MessageValueReplacer messageValueReplacer;
	private final MonitorService monitorService;
	private final MonitoringConfigurationToCheckRequestGroupConverter monitoringConfigurationToCheckRequestGroupConverter;
	private final YAMLConfigurationFileReader yamlConfigurationFileReader;

	private boolean firstRunDone = false;
	private List<CheckRequestGroup> checkRequestGroups;
	private MonitoringConfiguration monitoringConfiguration;

	private JButton buttonQuit;
	private JTree jTreeStatusView;
	private CallParameters callParameters;

	public SwingRunner(MessageValueReplacer messageValueReplacer, MonitorService monitorService,
	        MonitoringConfigurationToCheckRequestGroupConverter monitoringConfigurationToCheckRequestGroupConverter,
	        YAMLConfigurationFileReader yamlConfigurationFileReader) {
		this.messageValueReplacer = messageValueReplacer;
		this.monitorService = monitorService;
		this.monitoringConfigurationToCheckRequestGroupConverter = monitoringConfigurationToCheckRequestGroupConverter;
		this.yamlConfigurationFileReader = yamlConfigurationFileReader;
		setTitle("Service-Monitor");
	}

	public void buildComponents() {
		JPanel panelMain = new JPanel(new BorderLayout(HGAP, VGAP));
		panelMain.add(createDataPanel(), BorderLayout.CENTER);
		panelMain.add(createButtonPanel(), BorderLayout.SOUTH);
		setContentPane(panelMain);
		pack();
		new Thread(this).start();
	}

	private JPanel createDataPanel() {
		JPanel p = new JPanel(new GridLayout(1, 1, HGAP, VGAP));
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
		MonitoringResultRootTreeNode root = new MonitoringResultRootTreeNode("Results");
		checkRequestGroups.forEach(checkRequestGroup -> {
			MonitorResult result = monitorService.monitor(checkRequestGroup.getCheckRequests());
			MonitoringResultGroupTreeNode node = new MonitoringResultGroupTreeNode(checkRequestGroup.getName());
			root.addNode(node);
			result
			        .getCheckResults()
			        .forEach(checkResult -> node.addNode(new SingleMonitoringResultTreeNode(checkResult)));
		});
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
		setVisible(false);
		dispose();
		System.exit(0);
	}

}