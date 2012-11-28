package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import model.Document;


public class ViewDocumentDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel viewDocumentContentPanel = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private JScrollPane viewDocumentScrollPane = null;
	private JTextArea viewDocumentTextArea = null;
	private JPanel bottomPanel = null;
	private JButton closeButton = null;


	public ViewDocumentDialog(JFrame parent, Document doc) {
		super(parent);
		initialize();
		Util.centerWithinParent(this);
		this.setTitle(doc.getName());
		viewDocumentTextArea.setText(doc.getText());
		viewDocumentTextArea.setCaretPosition(0);
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
		this.setTitle("View Document");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setModal(false);
		this.setName("viewDocumentDialog");
		this.setContentPane(getViewDocumentContentPanel());
	}

	/**
	 * This method initializes viewDocumentContentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getViewDocumentContentPanel() {
		if (viewDocumentContentPanel == null) {
			viewDocumentContentPanel = new JPanel();
			viewDocumentContentPanel.setLayout(new BorderLayout());
			viewDocumentContentPanel.setSize(new Dimension(758, 495));
			viewDocumentContentPanel.add(getViewDocumentScrollPane(), BorderLayout.CENTER);
			viewDocumentContentPanel.add(getBottomPanel(), BorderLayout.SOUTH);
			viewDocumentContentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(
					10, 10, 0, 10));
		}
		return viewDocumentContentPanel;
	}

	/**
	 * This method initializes viewDocumentScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getViewDocumentScrollPane() {
		if (viewDocumentScrollPane == null) {
			viewDocumentScrollPane = new JScrollPane();
			viewDocumentScrollPane.setViewportView(getViewDocumentTextArea());
		}
		return viewDocumentScrollPane;
	}

	/**
	 * This method initializes viewDocumentTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getViewDocumentTextArea() {
		if (viewDocumentTextArea == null) {
			viewDocumentTextArea = new JTextArea();
			viewDocumentTextArea.setEditable(false);
			viewDocumentTextArea.setLineWrap(true);
			viewDocumentTextArea.setWrapStyleWord(true);
			viewDocumentTextArea.setFont(new Font("Lucida Sans", Font.PLAIN, 12));
		}
		return viewDocumentTextArea;
	}

	/**
	 * This method initializes bottomPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getBottomPanel() {
		if (bottomPanel == null) {
			bottomPanel = new JPanel();
			bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 10));
			bottomPanel.setSize(new Dimension(758, 195));
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
					ViewDocumentDialog.this.dispose();
				}
			});
		}
		return closeButton;
	}

}  //  @jve:decl-index=0:visual-constraint="10,9"
