package view;

import java.awt.Rectangle;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import model.SaidModel;
import classifier.Classifier;
import classifier.Kmeans;
import java.awt.Font;


public class KmeansDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel kmContentPanel = null;
	private JLabel proxMeasureLabel = null;
	private JComboBox proxMeasureComboBox = null;
	private JLabel initialCentroidsChoiceLabel = null;
	private JRadioButton uniformRadioButton = null;
	private JRadioButton kmppRadioButton = null;
	private JRadioButton spaceDistributedRadioButton = null;
	private JLabel emptyClustersLabel = null;
	private JComboBox emptyClustersComboBox = null;
	private JLabel kLabel = null;
	private JTextField kTextField = null;
	private JLabel minClusterSizeLabel = null;
	private JSpinner minClusterSizeSpinner = null;
	private JLabel maxClusterSizeLabel = null;
	private JSpinner maxClusterSizeSpinner = null;
	private JLabel runLabelLabel = null;
	private JRadioButton untilConvergeRadioButton = null;
	private JRadioButton nItersRadioButton = null;
	private JTextField nItersTextField = null;
	private JButton cancelButton = null;
	private JButton runAlgorithmButton = null;
	
	private SaidModel said_;
	private boolean   actionCancelled_;
	
	/**
	 * @param parent
	 */
	public KmeansDialog(JFrame parent, SaidModel said) {
		super(parent);
		said_ = said;
		new EscapeAction().register(this);
		initialize();
	}

	/**
	 * This method initializes this.
	 */
	private void initialize() {
		this.setSize(549, 551);
		this.setTitle("Kmeans Parameters");
		this.setResizable(false);
		this.setName("kmeansJDialog");
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setContentPane(getKmContentPanel());
	}

	/**
	 * This method initializes kmContentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getKmContentPanel() {
		if (kmContentPanel == null) {
			proxMeasureLabel = new JLabel();
			proxMeasureLabel.setBounds(new Rectangle(20, 20, 190, 23));
			proxMeasureLabel.setText("proximity measure:");
			proxMeasureLabel.setEnabled(true);
			initialCentroidsChoiceLabel = new JLabel();
			initialCentroidsChoiceLabel.setBounds(new Rectangle(20, 70, 190, 23));
			initialCentroidsChoiceLabel.setText("initial centroids choice:");
			emptyClustersLabel = new JLabel();
			emptyClustersLabel.setBounds(new Rectangle(20, 180, 190, 23));
			emptyClustersLabel.setEnabled(true);
			emptyClustersLabel.setText("empty clusters");
			kLabel = new JLabel();
			kLabel.setBounds(new Rectangle(20, 230, 190, 23));
			kLabel.setText("# clusters (K)");
			kLabel.setEnabled(true);
			minClusterSizeLabel = new JLabel();
			minClusterSizeLabel.setBounds(new Rectangle(20, 280, 190, 23));
			minClusterSizeLabel.setText("min cluster size:");
			maxClusterSizeLabel = new JLabel();
			maxClusterSizeLabel.setBounds(new Rectangle(20, 330, 190, 23));
			maxClusterSizeLabel.setText("max cluster size:");
			runLabelLabel = new JLabel();
			runLabelLabel.setBounds(new Rectangle(20, 380, 190, 23));
			runLabelLabel.setText("run:");
			kmContentPanel = new JPanel();
			kmContentPanel.setLayout(null);
			kmContentPanel.add(proxMeasureLabel, null);
			kmContentPanel.add(getProxMeasureComboBox(), null);
			kmContentPanel.add(initialCentroidsChoiceLabel, null);
			kmContentPanel.add(getUniformRadioButton(), null);
			kmContentPanel.add(getSpaceDistributedRadioButton(), null);
			kmContentPanel.add(getKmppRadioButton(), null);
			kmContentPanel.add(emptyClustersLabel, null);
			kmContentPanel.add(getEmptyClustersComboBox(), null);
			kmContentPanel.add(kLabel, null);
			kmContentPanel.add(getKTextField(), null);
			kmContentPanel.add(getMinClusterSizeSpinner());
			kmContentPanel.add(getMaxClusterSizeSpinner());
			kmContentPanel.add(runLabelLabel, null);
			kmContentPanel.add(getUntilConvergeRadioButton(), null);
			kmContentPanel.add(getNItersRadioButton(), null);
			kmContentPanel.add(getNItersTextField(), null);
			kmContentPanel.add(getCancelButton(), null);
			kmContentPanel.add(getRunAlgorithmButton(), null);
			kmContentPanel.add(minClusterSizeLabel, null);
			kmContentPanel.add(maxClusterSizeLabel, null);
			ButtonGroup bg1 = new ButtonGroup();
			bg1.add(getUniformRadioButton());
			bg1.add(getSpaceDistributedRadioButton());
			bg1.add(getKmppRadioButton());
			ButtonGroup bg2 = new ButtonGroup();
			bg2.add(getUntilConvergeRadioButton());
			bg2.add(getNItersRadioButton());
		}
		return kmContentPanel;
	}

	/**
	 * This method initializes proxMeasureComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getProxMeasureComboBox() {
		if (proxMeasureComboBox == null) {
			proxMeasureComboBox = new JComboBox();
			proxMeasureComboBox.setBounds(new Rectangle(215, 20, 310, 23));
			proxMeasureComboBox.addItem("cosine similarity");
			proxMeasureComboBox.addItem("euclidean distance");
			proxMeasureComboBox.addItem("manhattan distance");
		}
		return proxMeasureComboBox;
	}

	/**
	 * This method initializes uniformRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getUniformRadioButton() {
		if (uniformRadioButton == null) {
			uniformRadioButton = new JRadioButton();
			uniformRadioButton.setBounds(new Rectangle(215, 70, 210, 23));
			uniformRadioButton.setSelected(true);
			uniformRadioButton.setText("uniform");
			uniformRadioButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					checkBeans();
				}
			});
		}
		return uniformRadioButton;
	}

	/**
	 * This method initializes spaceDistributedRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getSpaceDistributedRadioButton() {
		if (spaceDistributedRadioButton == null) {;
			spaceDistributedRadioButton = new JRadioButton();
			spaceDistributedRadioButton.setBounds(new Rectangle(215, 100, 210, 23));
			spaceDistributedRadioButton.setText("distributed in the space");
			spaceDistributedRadioButton
			.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					checkBeans();
				}
			});
		}
		return spaceDistributedRadioButton;
	}

	/**
	 * This method initializes kmppRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getKmppRadioButton() {
		if (kmppRadioButton == null) {
			kmppRadioButton = new JRadioButton();
			kmppRadioButton.setBounds(new Rectangle(215, 130, 143, 23));
			kmppRadioButton.setText("kmeans++");
			kmppRadioButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					checkBeans();
				}
			});
		}
		return kmppRadioButton;
	}

	/**
	 * This method initializes emptyClustersComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getEmptyClustersComboBox() {
		if (emptyClustersComboBox == null) {
			emptyClustersComboBox = new JComboBox();
			emptyClustersComboBox.setBounds(new Rectangle(215, 180, 310, 23));
			emptyClustersComboBox.addItem("just remove them");
			emptyClustersComboBox.addItem("remove and split most dispersed cluster");
			emptyClustersComboBox.addItem("remove and split biggest cluster");
		}
		return emptyClustersComboBox;
	}

	/**
	 * This method initializes kTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getKTextField() {
		if (kTextField == null) {
			kTextField = new JTextField();
			kTextField.setBounds(new Rectangle(215, 230, 310, 23));
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
	 * This method initializes minClusterSizeSpinner
	 * 
	 * @return javax.swing.JSpinner
	 */
	private JSpinner getMinClusterSizeSpinner() {
		if (minClusterSizeSpinner == null) {
			minClusterSizeSpinner = new JSpinner();
			minClusterSizeSpinner.setModel(
					new SpinnerNumberModel(1, 1, said_.getNumDocuments()-1, 1));
			minClusterSizeSpinner.setFont(new Font("Dialog", Font.PLAIN, 12));
			minClusterSizeSpinner.setBounds(new Rectangle(215, 280, 310, 23));
			minClusterSizeSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					int min = (Integer) minClusterSizeSpinner.getValue();
					int max = (Integer) maxClusterSizeSpinner.getValue();
					int nDocs = said_.getNumDocuments();
					maxClusterSizeSpinner.setModel(
							new SpinnerNumberModel(max, min+1, nDocs, 1));
				}
			});
		}
		return minClusterSizeSpinner;
	}

	/**
	 * This method initializes maxClusterSizeSpinner
	 * 
	 * @return javax.swing.JSpinner
	 */
	private JSpinner getMaxClusterSizeSpinner() {
		if (maxClusterSizeSpinner == null) {
			maxClusterSizeSpinner = new JSpinner();
			int nDocs = said_.getNumDocuments();
			maxClusterSizeSpinner.setModel(
					new SpinnerNumberModel(nDocs, 2, nDocs, 1));
			maxClusterSizeSpinner.setFont(new Font("Dialog", Font.PLAIN, 12));
			maxClusterSizeSpinner.setBounds(new Rectangle(215, 330, 310, 23));
			maxClusterSizeSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					int min = (Integer) minClusterSizeSpinner.getValue();
					int max = (Integer) maxClusterSizeSpinner.getValue();
					minClusterSizeSpinner.setModel(
							new SpinnerNumberModel(min, 1, max-1, 1));
				}
			});
		}
		return maxClusterSizeSpinner;
	}

	/**
	 * This method initializes untilConvergeRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getUntilConvergeRadioButton() {
		if (untilConvergeRadioButton == null) {
			untilConvergeRadioButton = new JRadioButton();
			untilConvergeRadioButton.setBounds(new Rectangle(215, 380, 158, 23));
			untilConvergeRadioButton.setSelected(true);
			untilConvergeRadioButton.setText("until converge");
			untilConvergeRadioButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					nItersTextField.setEnabled(false);
					checkBeans();
				}
			});
		}
		return untilConvergeRadioButton;
	}

	/**
	 * This method initializes nItersRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getNItersRadioButton() {
		if (nItersRadioButton == null) {
			nItersRadioButton = new JRadioButton();
			nItersRadioButton.setBounds(new Rectangle(215, 410, 215, 23));
			nItersRadioButton.setText("the following # iterations:");
			nItersRadioButton
			.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					nItersTextField.setEnabled(true);
					checkBeans();
				}
			});
		}
		return nItersRadioButton;
	}

	/**
	 * This method initializes nItersTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getNItersTextField() {
		if (nItersTextField == null) {
			nItersTextField = new JTextField();
			nItersTextField.setBounds(new Rectangle(435, 410, 90, 23));
			nItersTextField.setText("1");
			nItersTextField.setEnabled(false);
			nItersTextField.addKeyListener(new java.awt.event.KeyAdapter() {
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
		return nItersTextField;
	}

	/**
	 * This method initializes cancelButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setBounds(new Rectangle(115, 480, 146, 26));
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					KmeansDialog.this.dispose();
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
			runAlgorithmButton.setBounds(new Rectangle(285, 480, 146, 26));
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
		if (kTextField.getText().length() == 0
		 || nItersRadioButton.isSelected() && nItersTextField.getText().length() == 0) {
				runAlgorithmButton.setEnabled(false);
		}
	}

	private void runAlgorithm() {
		Classifier classif = said_.classifier();
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

		if (uniformRadioButton.isSelected()) {
			classif.kmeans().setSeedingHeuristic(Kmeans.UNIFORM_CENTERS);
		}
		else if (spaceDistributedRadioButton.isSelected()) {
			classif.kmeans().setSeedingHeuristic(Kmeans.DISTRIBUTED_CENTERS);
		}
		else if (kmppRadioButton.isSelected()) {
			classif.kmeans().setSeedingHeuristic(Kmeans.SMART_CENTERS);
		}
		
		int index = emptyClustersComboBox.getSelectedIndex() + 1; 
		if (index == 1) {
			classif.kmeans().setEmptyClusterHandleMode(Kmeans.JUST_REMOVE);
		}
		else if (index == 2) {
			classif.kmeans().setEmptyClusterHandleMode(Kmeans.SPLIT_MOST_DISPERSED);
		}
		else {
			classif.kmeans().setEmptyClusterHandleMode(Kmeans.SPLIT_BIGGEST);
		}

		int k = Integer.parseInt(kTextField.getText());
		if (k == 0) {
			Util.showOptionPane(this, Util.M_CLUSTERS_ZERO);
			return;
		}
		else if (k > said_.getNumDocuments()) {
			Util.showOptionPane(this, Util.M_CLUSTERS_TOO_MANY);
			return;
		}

		this.dispose();
		if (untilConvergeRadioButton.isSelected()) {
			int[] args = {k};
			new ProgressDialog(
					(JFrame) this.getParent(), 
					said_,
					ProgressDialog.T_RUN_KMEANS, args
					).setVisible(true);
		}
		else if (nItersRadioButton.isSelected()) {
			int[] args = {k, Integer.parseInt(nItersTextField.getText())};
			new ProgressDialog(
					(JFrame) this.getParent(),
					said_, 
					ProgressDialog.T_RUN_N_KMEANS_ITERS,
					args
					).setVisible(true);
		}
		
		int minSize = (Integer) minClusterSizeSpinner.getValue();
		int maxSize = (Integer) maxClusterSizeSpinner.getValue(); 
		int[] args = {minSize, maxSize}; 
		if (minSize != 1 || maxSize != said_.getNumDocuments()) {
			new ProgressDialog(
					(JFrame) this.getParent(),
					said_, 
					ProgressDialog.T_NORMALIZE_SIZE,
					args
					).setVisible(true);
		
			
			if (said_.classifier().wasSuccessfulNormalized() == false) {
				Util.showOptionPane(this, Util.M_NORMALIZE_ERROR);
			}
		}

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
