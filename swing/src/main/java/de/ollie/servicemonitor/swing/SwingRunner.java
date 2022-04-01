package de.ollie.servicemonitor.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Named;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import de.ollie.servicemonitor.parameter.CallParameters;

@Named
public class SwingRunner extends JFrame implements ActionListener {

	static final int FONT_SIZE = 16;
	static final int FONT_SIZE_BUTTONS = FONT_SIZE;
	static final int FONT_SIZE_LABELS = FONT_SIZE;
	static final int HGAP = 3;
	static final int VGAP = 3;

	private JButton buttonQuit;
	private CallParameters callParameters;

	public SwingRunner() {
		setTitle("Service-Monitor");
		JPanel panelMain = new JPanel(new BorderLayout(HGAP, VGAP));
		panelMain.add(createDataPanel(), BorderLayout.CENTER);
		panelMain.add(createButtonPanel(), BorderLayout.SOUTH);
		setContentPane(panelMain);
		pack();
	}

	private JPanel createDataPanel() {
		JPanel p = new JPanel(new GridLayout(1, 1, HGAP, VGAP));
		JLabel label = new JLabel(
		        "<html><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Swing GUI is not implemented yet!!!&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>&nbsp;</html>",
		        SwingConstants.CENTER);
		Font font = label.getFont();
		label.setFont(new Font(font.getName(), font.getStyle(), FONT_SIZE_LABELS));
		p.add(label);
		return p;
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
	}

}