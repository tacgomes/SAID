package view;

import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import model.SaidModel;
import classifier.Classifier;
import classifier.Hac;


public class HacDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel hacContentPanel = null;
	private JLabel clusterSimLabel = null;
	private JComboBox clusterSimComboBox = null;
	private JLabel proxMeasureLabel = null;
	private JComboBox proxMeasureComboBox = null;
	private JLabel kLabel = null;
	private JTextField kTextField = null;
	private JButton cancelButton = null;
	private JButton runAlgorithmButton = null;

	private SaidModel said_;
	private boolean   actionCancelled_;


	/**
	 * @param parent
	 */
	public HacDialog(JFrame parent, SaidModel said) {
		super(parent);
		initialize();
		new EscapeAction().register(this);
		said_ = said;
	}

	/**
	 * This method initializes this.
	 */
	private void initialize() {
		this.setSize(482, 262);
		this.setTitle("Hierarchical Aglomerative Clustering Parameters");
		this.setResizable(false);
		this.setName("kmeansJDialog");
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setContentPane(getHacContentPanel());
	}

	/**
	 * This method initializes hacContentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getHacContentPanel() {
		if (hacContentPanel == null) {
			clusterSimLabel = new JLabel();
			clusterSimLabel.setBounds(new Rectangle(20, 20, 195, 23));
			clusterSimLabel.setText("cluster similarity method:");
			proxMeasureLabel = new JLabel();
			proxMeasureLabel.setBounds(new Rectangle(20, 70, 195, 23));
			proxMeasureLabel.setText("proximity measure:");
			kLabel = new JLabel();
			kLabel.setBounds(new Rectangle(20, 120, 195, 23));
			kLabel.setText("# clusters (K):");
			kLabel.setEnabled(true);
			hacContentPanel = new JPanel();
			hacContentPanel.setLayout(null);
			hacContentPanel.add(proxMeasureLabel, null);
			hacContentPanel.add(getProxMeasureComboBox(), null);
			hacContentPanel.add(clusterSimLabel, null);
			hacContentPanel.add(getClusterSimComboBox(), null);
			hacContentPanel.add(kLabel, null);
			hacContentPanel.add(getKTextField(), null);
			hacContentPanel.add(getCancelButton(), null);
			hacContentPanel.add(getRunAlgorithmButton(), null);
		}
		return hacContentPanel;
	}

	/**
	 * This method initializes clusterSimComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getClusterSimComboBox() {
		if (clusterSimComboBox == null) {
			clusterSimComboBox = new JComboBox();
			clusterSimComboBox.setBounds(new Rectangle(220, 20, 242, 23));
			clusterSimComboBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (clusterSimComboBox.getSelectedIndex() == 4) {
						proxMeasureComboBox.setEnabled(false);
					}
					else {
						proxMeasureComboBox.setEnabled(true);
					}
				}
			});
			clusterSimComboBox.addItem("single linkage");
			clusterSimComboBox.addItem("complete linkage");
			clusterSimComboBox.addItem("group average");
			clusterSimComboBox.addItem("distance between centroids");
			clusterSimComboBox.addItem("ward's method");
		}
		return clusterSimComboBox;
	}
	
	/**
	 * This method initializes proxMeasureComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getProxMeasureComboBox() {
		if (proxMeasureComboBox == null) {
			proxMeasureComboBox = new JComboBox();
			proxMeasureComboBox.setBounds(new Rectangle(220, 70, 242, 23));
			proxMeasureComboBox.addItem("cosine similarity");
			proxMeasureComboBox.addItem("euclidean distance");
			proxMeasureComboBox.addItem("manhattan distance");
		}
		return proxMeasureComboBox;
	}

	/**
	 * This method initializes kTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getKTextField() {
		if (kTextField == null) {
			kTextField = new JTextField();
			kTextField.setBounds(new Rectangle(220, 120, 242, 23));
			kTextField.setText("");
			kTextField.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					if (Character.isDigit(e.getKeyChar()) == false) {
						e.consume();
					}
				}
				public void keyReleased(java.awt.event.KeyEvent e) {
					checkBeans();
				}
			});
		}
		return kTextField;
	}

	/**
	 * This method initializes cancelButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setBounds(new Rectangle(90, 190, 146, 26));
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					actionCancelled_ = true;
					HacDialog.this.dispose();
				}
			});
		}
		return cancelButton;
	}

	/**
	 * This method initializes runAlgorithmButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRunAlgorithmButton() {
		if (runAlgorithmButton == null) {
			runAlgorithmButton = new JButton();
			runAlgorithmButton.setBounds(new Rectangle(250, 190, 146, 26));
			runAlgorithmButton.setActionCommand("");
			runAlgorithmButton.setText("Run Algorithm");
			runAlgorithmButton.setEnabled(false);
			runAlgorithmButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					runAlgorithm();
				}
			});
		}
		return runAlgorithmButton;
	}

	private void checkBeans() {
		runAlgorithmButton.setEnabled(true);
		if (kTextField.getText().length() == 0) {
			runAlgorithmButton.setEnabled(false);
		}
	}

	private void runAlgorithm() {
		Classifier classif = said_.classifier();

		int clusterSim = 0;
		switch (clusterSimComboBox.getSelectedIndex()) {
		case 0:
			clusterSim = Hac.SINGLE_LINKAGE;
			break;
		case 1:
			clusterSim = Hac.COMPLETE_LINKAGE;
			break;
		case 2:
			clusterSim = Hac.GROUP_AVERAGE;
			break;
		case 3:
			clusterSim = Hac.CENTROID_PROXIMITY;
			break;
		case 4:
			clusterSim = Hac.WARDS_METHOD;
		}
		
		if (clusterSim == Hac.WARDS_METHOD) {
			classif.setProximityMeasure(Classifier.EUCLIDEAN_DISTANCE);			
		}
		else {
			switch (proxMeasureComboBox.getSelectedIndex()) {
			case 0:
				classif.setProximityMeasure(Classifier.COSINE_SIMILARITY);
				break;
				
			case 1:
				classif.setProximityMeasure(Classifier.EUCLIDEAN_DISTANCE);
				break;

			case 2:
				classif.setProximityMeasure(Classifier.MANHATTAN_DISTANCE);
				break;
			}
		}
		
		int k = Integer.parseInt(kTextField.getText());
		if (k == 0) {
			Util.showOptionPane(this, Util.M_CLUSTERS_ZERO);
			return;
		}
		if (k > said_.getNumDocuments()) {
			Util.showOptionPane(this, Util.M_CLUSTERS_TOO_MANY);
			return;
		}

		this.dispose();
		int[] args = {k,  clusterSim};
		new ProgressDialog(
				(JFrame) this.getParent(), 
				said_, 
				ProgressDialog.T_RUN_HAC, 
				args
				).setVisible(true);
		
		actionCancelled_ = false;
	}
	
	public void updateToShow() {
		actionCancelled_ = true;
		this.getRootPane().setDefaultButton(getRunAlgorithmButton());
		SwingUtilities.invokeLater(new Runnable() {   
			public void run() {   
				getKTextField().requestFocus();   
			}   
		});  
	}

	public boolean actionCancelled() {
		return actionCancelled_;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
