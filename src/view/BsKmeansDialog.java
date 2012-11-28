package view;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import model.SaidModel;
import classifier.BisectingKmeans;
import classifier.Classifier;


public class BsKmeansDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel bsKmContentPanel = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private JLabel proxMeasureLabel = null;
	private JComboBox proxMeasureComboBox = null;
	private JLabel splitCriteriumLabel = null;
	private JComboBox splitCriteriumComboBox = null;
	private JLabel stopCriteriumLabel = null;
	private JRadioButton dispersionRadioButton = null;
	private JTextField dispersionTextField = null;
	private JRadioButton sizeRadioButton = null;
	private JTextField sizeTextField = null;
	private JRadioButton kRadioButton = null;
	private JTextField kTextField = null;
	private JLabel nTrialsLabel = null;
	private JTextField nTrialsTextField = null;
	private JButton cancelButton = null;
	private JButton runAlgorithmButton = null;

	private SaidModel said_;
	private boolean   actionCancelled_;


	/**
	 * @param parent
	 */
	public BsKmeansDialog(JFrame parent, SaidModel said) {
		super(parent);
		said_ = said;
		initialize();
		new EscapeAction().register(this);
	}

	/**
	 * This method initializes this.
	 */
	private void initialize() {
		this.setSize(586, 371);
		this.setTitle("Bisecting-Kmeans Parameters");
		this.setResizable(false);
		this.setName("bisectingKmeansJDialog");
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setContentPane(getBsKmContentPanel());
	}

	/**
	 * This method initializes bsKmContentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getBsKmContentPanel() {
		if (bsKmContentPanel == null) {
			proxMeasureLabel = new JLabel();
			proxMeasureLabel.setBounds(new Rectangle(20, 20, 190, 23));
			proxMeasureLabel.setText("proximity measure:");
			proxMeasureLabel.setEnabled(true);
			splitCriteriumLabel = new JLabel();
			splitCriteriumLabel.setBounds(new Rectangle(20, 70, 190, 23));
			splitCriteriumLabel.setText("split cluster with biggest:");
			stopCriteriumLabel = new JLabel();
			stopCriteriumLabel.setBounds(new Rectangle(20, 120, 190, 23));
			stopCriteriumLabel.setText("stop spliting when:");
			nTrialsLabel = new JLabel();
			nTrialsLabel.setBounds(new Rectangle(20, 230, 190, 23));
			nTrialsLabel.setText("# trials:");
			bsKmContentPanel = new JPanel();
			bsKmContentPanel.setLayout(null);
			bsKmContentPanel.setSize(new Dimension(587, 322));
			bsKmContentPanel.add(proxMeasureLabel, null);
			bsKmContentPanel.add(getProxMeasureComboBox(), null);
			bsKmContentPanel.add(splitCriteriumLabel, null);
			bsKmContentPanel.add(getSplitCriteriumComboBox(), null);
			bsKmContentPanel.add(stopCriteriumLabel, null);
			bsKmContentPanel.add(getDispersionRadioButton(), null);
			bsKmContentPanel.add(getDispersionTextField(), null);
			bsKmContentPanel.add(getSizeRadioButton(), null);
			bsKmContentPanel.add(getSizeTextField(), null);
			bsKmContentPanel.add(getKRadioButton(), null);
			bsKmContentPanel.add(getKTextField(), null);
			bsKmContentPanel.add(nTrialsLabel, null);
			bsKmContentPanel.add(getNTrialsTextField(), null);
			bsKmContentPanel.add(getRunAlgorithmButton(), null);
			bsKmContentPanel.add(getCancelButton(), null);
			ButtonGroup bg = new ButtonGroup();
			bg.add(dispersionRadioButton);
			bg.add(sizeRadioButton);
			bg.add(kRadioButton);
		}
		return bsKmContentPanel;
	}

	/**
	 * This method initializes proxMeasureComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getProxMeasureComboBox() {
		if (proxMeasureComboBox == null) {
			proxMeasureComboBox = new JComboBox();
			proxMeasureComboBox.setBounds(new Rectangle(215, 20, 350, 23));
			proxMeasureComboBox.addItem("cosine similarity");
			proxMeasureComboBox.addItem("euclidean distance");
			proxMeasureComboBox.addItem("manhattan distance");
		}
		return proxMeasureComboBox;
	}

	/**
	 * This method initializes splitCriteriumComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getSplitCriteriumComboBox() {
		if (splitCriteriumComboBox == null) {
			splitCriteriumComboBox = new JComboBox();
			splitCriteriumComboBox.setBounds(new Rectangle(215, 70, 350, 23));
			splitCriteriumComboBox.addItem("dispersion");
			splitCriteriumComboBox.addItem("size");
		}
		return splitCriteriumComboBox;
	}

	/**
	 * This method initializes dispersionRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getDispersionRadioButton() {
		if (dispersionRadioButton == null) {
			dispersionRadioButton = new JRadioButton();
			dispersionRadioButton.setBounds(new Rectangle(215, 120, 295, 23));
			dispersionRadioButton.setSelected(true);
			dispersionRadioButton.setText("each cluster dispersion is lesser than");
			dispersionRadioButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispersionTextField.setEnabled(true);
					dispersionTextField.requestFocus();
					sizeTextField.setEnabled(false);
					kTextField.setEnabled(false);
					checkBeans();
				}
			});
		}
		return dispersionRadioButton;
	}

	/**
	 * This method initializes dispersionTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getDispersionTextField() {
		if (dispersionTextField == null) {
			dispersionTextField = new JTextField();
			dispersionTextField.setBounds(new Rectangle(515, 120, 50, 23));
			dispersionTextField.addKeyListener(new java.awt.event.KeyAdapter() {
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
		return dispersionTextField;
	}

	/**
	 * This method initializes sizeRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getSizeRadioButton() {
		if (sizeRadioButton == null) {
			sizeRadioButton = new JRadioButton();
			sizeRadioButton.setBounds(new Rectangle(215, 150, 294, 23));
			sizeRadioButton.setText("each cluster size is lesser than");
			sizeRadioButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispersionTextField.setEnabled(false);
					sizeTextField.setEnabled(true);
					sizeTextField.requestFocus();
					kTextField.setEnabled(false);
					checkBeans();
				}
			});
		}
		return sizeRadioButton;
	}

	/**
	 * This method initializes sizeTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getSizeTextField() {
		if (sizeTextField == null) {
			sizeTextField = new JTextField();
			sizeTextField.setBounds(new Rectangle(515, 150, 50, 23));
			sizeTextField.setEnabled(false);
			sizeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
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
		return sizeTextField;
	}

	/**
	 * This method initializes kRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getKRadioButton() {
		if (kRadioButton == null) {
			kRadioButton = new JRadioButton();
			kRadioButton.setBounds(new Rectangle(215, 180, 294, 23));
			kRadioButton.setText("the number of clusters is");
			kRadioButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispersionTextField.setEnabled(false);
					sizeTextField.setEnabled(false);
					kTextField.setEnabled(true);
					kTextField.requestFocus();
					checkBeans();
				}
			});
		}
		return kRadioButton;
	}

	/**
	 * This method initializes kTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getKTextField() {
		if (kTextField == null) {
			kTextField = new JTextField();
			kTextField.setBounds(new Rectangle(515, 180, 50, 23));
			kTextField.setEnabled(false);
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
	 * This method initializes nTrialsTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getNTrialsTextField() {
		if (nTrialsTextField == null) {
			nTrialsTextField = new JTextField();
			nTrialsTextField.setBounds(new Rectangle(215, 230, 350, 23));
			nTrialsTextField.setText("1");
			nTrialsTextField.addKeyListener(new java.awt.event.KeyAdapter() {
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
		return nTrialsTextField;
	}

	/**
	 * This method initializes cancelButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setBounds(new Rectangle(145, 300, 146, 26));
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					BsKmeansDialog.this.dispose();
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
			runAlgorithmButton.setBounds(new Rectangle(305, 300, 146, 26));
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
		if (dispersionRadioButton.isSelected() && dispersionTextField.getText().length() == 0
		 || sizeRadioButton.isSelected() && sizeTextField.getText().length() == 0
		 || kRadioButton.isSelected() && kTextField.getText().length() == 0
		 || nTrialsTextField.getText().length() == 0) {
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

		int splitCriterium;
		if (splitCriteriumComboBox.getSelectedIndex() == 0) {
			splitCriterium = BisectingKmeans.SPLIT_BY_DISPERSION;
		}
		else {
			splitCriterium = BisectingKmeans.SPLIT_BY_SIZE;
		}

		int stopCriterium = 0;
		int stopValue = 0;

		if (dispersionRadioButton.isSelected()) {
			stopCriterium = BisectingKmeans.STOP_BY_DISPERSION;
			stopValue = Integer.parseInt(dispersionTextField.getText());
			if (stopValue == 0) {
				Util.showOptionPane(this, Util.M_DISPERSION_ZERO);
				return;
			}
		}
		else if (sizeRadioButton.isSelected()) {
			stopCriterium = BisectingKmeans.STOP_BY_SIZE;
			stopValue = Integer.parseInt(sizeTextField.getText());
			if (stopValue == 0) {
				Util.showOptionPane(this, Util.M_SIZE_ZERO);
				return;
			}
		}
		else if (kRadioButton.isSelected()){
			stopCriterium = BisectingKmeans.STOP_BY_NUM_OF_CLUSTERS;
			stopValue = Integer.parseInt(kTextField.getText());
			if (stopValue == 0) {
				Util.showOptionPane(this, Util.M_CLUSTERS_ZERO);
				return;
			}
			if (stopValue > said_.getNumDocuments()) {
				Util.showOptionPane(this, Util.M_CLUSTERS_TOO_MANY);
				return;
			}
		}

		int nTrials = Integer.parseInt(nTrialsTextField.getText());
		if (nTrials == 0) {
			Util.showOptionPane(this, Util.M_TRIALS_ZERO);
			return;
		}

		this.dispose();
		int[] args = {splitCriterium, stopCriterium, stopValue, nTrials};
		new ProgressDialog(
				(JFrame) this.getParent(), 
				said_,
				ProgressDialog.T_RUN_BSKMEANS,
				args
				).setVisible(true);
		
		actionCancelled_ = false;
	}
	
	public void updateToShow() {
		actionCancelled_ = true;
		this.getRootPane().setDefaultButton(getRunAlgorithmButton());
		SwingUtilities.invokeLater(new Runnable() {   
			public void run() { 
				if (dispersionRadioButton.isSelected()) {
					dispersionTextField.requestFocus();
				}
				else if (sizeRadioButton.isSelected()) {
					sizeTextField.requestFocus();
				}
				else if (kRadioButton.isSelected()){
					kTextField.requestFocus();
				}   
			}   
		});  
	}

	public boolean actionCancelled() {
		return actionCancelled_;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
