package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import model.SaidModel;


public class LogDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel logContentPanel = null;
	private JScrollPane logScrollPane = null;
	private JTextArea logTextArea = null;
	private JPanel bottomPanel = null;
	private JButton clearButton = null;
	private JButton closeButton = null;

	private SaidModel said_;


	public LogDialog(JFrame parent, SaidModel said) {
		super(parent);
		initialize();
		Util.centerWithinParent(this);
		said_ = said;
		logTextArea.setText(said_.getLog());
		if (logTextArea.getText().length() == 0) {
			clearButton.setEnabled(false);
		}
		this.getRootPane().setDefaultButton(getCloseButton());
		new EscapeAction().register(this);
	}

	/**
	 * This method initializes this.
	 */
	private void initialize() {
		this.setMinimumSize(new Dimension(400, 300));
		this.setSize(767, 533);
		this.setResizable(true);
		this.setTitle("Log");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setModal(true);
		this.setName("logDialog");
		this.setContentPane(getLogContentPanel());
	}

	/**
	 * This method initializes logContentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getLogContentPanel() {
		if (logContentPanel == null) {
			logContentPanel = new JPanel();
			logContentPanel.setLayout(new BorderLayout());
			logContentPanel.add(getLogScrollPane(), BorderLayout.CENTER);
			logContentPanel.add(getBottomPanel(), BorderLayout.SOUTH);
			logContentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(
					10, 10, 0, 10));
		}
		return logContentPanel;
	}

	/**
	 * This method initializes logScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getLogScrollPane() {
		if (logScrollPane == null) {
			logScrollPane = new JScrollPane();
			logScrollPane.setViewportView(getLogTextArea());
		}
		return logScrollPane;
	}

	/**
	 * This method initializes logTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getLogTextArea() {
		if (logTextArea == null) {
			logTextArea = new JTextArea();
			logTextArea.setEditable(false);
			logTextArea.setFont(new Font("Lucida Sans", Font.PLAIN, 12));
		}
		return logTextArea;
	}

	/**
	 * This method initializes bottomPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getBottomPanel() {
		if (bottomPanel == null) {
			bottomPanel = new JPanel();
			bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 10));
			bottomPanel.add(getClearButton());
			bottomPanel.add(new Label());
			bottomPanel.add(getCloseButton());
		}
		return bottomPanel;
	}

	/**
	 * This method initializes closeButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setText("Close");
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					LogDialog.this.dispose();
				}
			});
		}
		return closeButton;
	}

	/**
	 * This method initializes clearButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getClearButton() {
		if (clearButton == null) {
			clearButton = new JButton();
			clearButton.setText("Clear");
			clearButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (Util.showOptionPane(LogDialog.this, Util.M_CLEAR_LOG) 
							== JOptionPane.YES_OPTION) {
						said_.clearLog();
						logTextArea.setText("");
						clearButton.setEnabled(false);
					}
				}
			});
		}
		return clearButton;
	}
	
	public void setDefaultButton() {
		this.getRootPane().setDefaultButton(getCloseButton());
	}

}  //  @jve:decl-index=0:visual-constraint="10,9"
