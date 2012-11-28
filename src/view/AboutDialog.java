package view;


import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel aboutContentPanel = null;
	private JLabel progNameLabel = null;
	private JLabel versionLabel = null;
	private JLabel descriptionLabel = null;
	private JButton closeButton = null;


	/**
	 * @param parent
	 */
	public AboutDialog(JFrame parent) {
		super(parent);
		initialize();
		Util.centerWithinParent(this);
		new EscapeAction().register(this);
		this.getRootPane().setDefaultButton(getCloseButton());
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(337, 246);
		this.setResizable(false);
		this.setName("aboutJDialog");
		this.setModal(true);
		this.setContentPane(getAboutContentPanel());
	}

	/**
	 * This method initializes aboutContentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAboutContentPanel() {
		if (aboutContentPanel == null) {
			descriptionLabel = new JLabel();
			descriptionLabel.setBounds(new Rectangle(1, 60, 325, 43));
			descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
			descriptionLabel.setFont(new Font("Liberation Sans", Font.PLAIN, 12));
			descriptionLabel.setText("An automatic indexing system.");
			versionLabel = new JLabel();
			versionLabel.setBounds(new Rectangle(1, 108, 326, 31));
			versionLabel.setHorizontalAlignment(SwingConstants.CENTER);
			versionLabel.setText("Version 1.0");
			progNameLabel = new JLabel();
			progNameLabel.setBounds(new Rectangle(1, 16, 325, 42));
			progNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			progNameLabel.setFont(new Font("Dialog", Font.BOLD, 36));
			progNameLabel.setText("S A I D");
			aboutContentPanel = new JPanel();
			aboutContentPanel.setLayout(null);
			aboutContentPanel.add(getCloseButton(), null);
			aboutContentPanel.add(progNameLabel, null);
			aboutContentPanel.add(versionLabel, null);
			aboutContentPanel.add(descriptionLabel, null);
		}
		return aboutContentPanel;
	}

	/**
	 * This method initializes closeButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setBounds(new Rectangle(116, 174, 92, 26));
			closeButton.setText("Close");
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					AboutDialog.this.dispose();
				}
			});
		}
		return closeButton;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
