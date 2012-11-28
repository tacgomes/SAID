package view;

import java.awt.Rectangle;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

import model.SaidModel;


public class NewAnalysisDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel newAnalysisContentPanel = null;
	private JLabel documentsLabel = null;
	private JLabel directoryLabel = null;
	private JTextField docsDirTextField = null;
	private JButton docsDirButton = null;
	private JLabel termVocabularyLabel = null;
	private JRadioButton everyWordRadioButton = null;
	private JRadioButton dictionaryRadioButton = null;
	private JLabel fileLabel = null;
	private JTextField dicPathTextField = null;
	private JButton dicPathButton = null;
	private JCheckBox decayCheckBox = null;
	private JLabel decayTimeLabel = null;
	private JTextField decayTimeTextField = null;
	private JLabel lsiLabel = null;
	private JCheckBox lsiCheckBox = null;
	private JLabel percTraceLabel = null;
	private JSpinner percTraceSpinner = null;
	private JLabel weightSchemeLabel = null;
	private JRadioButton tfIdfRadioButton = null;
	private JRadioButton tfRadioButton = null;
	private JButton cancelButton = null;
	private JButton startButton = null;

	private SaidModel said_;
	private boolean	  actionCancelled_;


	public NewAnalysisDialog(JFrame parent, SaidModel said) {
		super(parent);
		initialize();
		new EscapeAction().register(this);
		said_ = said;
	}

	/**
	 * This method initializes this.
	 */
	private void initialize() {
		this.setSize(477, 570);
		this.setResizable(false);
		this.setTitle("New Analysis");
		this.setName("newAnalysisJDialog");
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setContentPane(getNewAnalysisContentPanel());
	}

	/**
	 * This method initializes newAnalysisContentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getNewAnalysisContentPanel() {
		if (newAnalysisContentPanel == null) {
			documentsLabel = new JLabel();
			documentsLabel.setBounds(new Rectangle(20, 15, 437, 23));
			documentsLabel.setText("Documents:");
			directoryLabel = new JLabel();
			directoryLabel.setBounds(new Rectangle(49, 45, 72, 23));
			directoryLabel.setText("directory:");
			termVocabularyLabel = new JLabel();
			termVocabularyLabel.setBounds(new Rectangle(17, 102, 437, 23));
			termVocabularyLabel.setText("Vocabulary Of Terms:");
			fileLabel = new JLabel();
			fileLabel.setBounds(new Rectangle(70, 180, 38, 23));
			fileLabel.setEnabled(false);
			fileLabel.setText("File:");
			decayTimeLabel = new JLabel();
			decayTimeLabel.setBounds(new Rectangle(237, 210, 87, 23));
			decayTimeLabel.setText("decay time:");
			decayTimeLabel.setEnabled(false);
			lsiLabel = new JLabel();
			lsiLabel.setBounds(new Rectangle(20, 264, 437, 23));
			lsiLabel.setText("Latent Semantic Indexing:");
			percTraceLabel = new JLabel();
			percTraceLabel.setBounds(new Rectangle(71, 316, 315, 23));
			percTraceLabel.setEnabled(false);
			percTraceLabel.setText("percentage of the trace of S matrix to keep:");
			weightSchemeLabel = new JLabel();
			weightSchemeLabel.setBounds(new Rectangle(21, 377, 437, 23));
			weightSchemeLabel.setText("Weight Scheme:");
			newAnalysisContentPanel = new JPanel();
			newAnalysisContentPanel.setLayout(null);
			newAnalysisContentPanel.add(documentsLabel, null);
			newAnalysisContentPanel.add(directoryLabel, null);
			newAnalysisContentPanel.add(getDocsDirTextField(), null);
			newAnalysisContentPanel.add(getDocsDirButton(), null);
			newAnalysisContentPanel.add(termVocabularyLabel, null);
			newAnalysisContentPanel.add(getEveryWordRadioButton(), null);
			newAnalysisContentPanel.add(getDictionaryRadioButton(), null);
			newAnalysisContentPanel.add(fileLabel, null);
			newAnalysisContentPanel.add(getDicPathTextField(), null);
			newAnalysisContentPanel.add(getDicPathButton(), null);
			newAnalysisContentPanel.add(decayTimeLabel, null);
			newAnalysisContentPanel.add(getDecayCheckBox(), null);
			newAnalysisContentPanel.add(getDecayTimeTextField(), null);
			newAnalysisContentPanel.add(lsiLabel, null);
			newAnalysisContentPanel.add(getLsiCheckBox(), null);
			newAnalysisContentPanel.add(percTraceLabel, null);
			newAnalysisContentPanel.add(getPercTraceSpinner(), null);
			newAnalysisContentPanel.add(weightSchemeLabel, null);
			newAnalysisContentPanel.add(getTfIdfRadioButton(), null);
			newAnalysisContentPanel.add(getTfRadioButton(), null);
			newAnalysisContentPanel.add(getStartButton(), null);
			newAnalysisContentPanel.add(getCancelButton(), null);
			ButtonGroup bg1 = new ButtonGroup();
			bg1.add(getTfRadioButton());
			bg1.add(getTfIdfRadioButton());
			ButtonGroup bg2 = new ButtonGroup();
			bg2.add(getEveryWordRadioButton());
			bg2.add(getDictionaryRadioButton());
		}
		return newAnalysisContentPanel;
	}

	/**
	 * This method initializes docsDirTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getDocsDirTextField() {
		if (docsDirTextField == null) {
			docsDirTextField = new JTextField();
			docsDirTextField.setBounds(new Rectangle(128, 45, 278, 23));
			docsDirTextField.setEditable(false);
		}
		return docsDirTextField;
	}

	/**
	 * This method initializes docsDirButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDocsDirButton() {
		if (docsDirButton == null) {
			docsDirButton = new JButton();
			docsDirButton.setBounds(new Rectangle(414, 45, 42, 23));
			docsDirButton.setText("...");
			docsDirButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					if (fc.showOpenDialog(NewAnalysisDialog.this) == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						docsDirTextField.setText(file.getAbsolutePath());
						checkBeans();
					}
				}
			});
		}
		return docsDirButton;
	}

	/**
	 * This method initializes everyWordRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getEveryWordRadioButton() {
		if (everyWordRadioButton == null) {
			everyWordRadioButton = new JRadioButton();
			everyWordRadioButton.setBounds(new Rectangle(49, 127, 152, 23));
			everyWordRadioButton.setSelected(true);
			everyWordRadioButton.setText("use every word");
			everyWordRadioButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					fileLabel.setEnabled(false);
					dicPathButton.setEnabled(false);
					dicPathTextField.setEnabled(false);
					decayTimeLabel.setEnabled(false);
					decayCheckBox.setEnabled(false);
					decayTimeTextField.setEnabled(false);
					checkBeans();
				}
			});
		}
		return everyWordRadioButton;
	}

	/**
	 * This method initializes dictionaryRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getDictionaryRadioButton() {
		if (dictionaryRadioButton == null) {
			dictionaryRadioButton = new JRadioButton();
			dictionaryRadioButton.setBounds(new Rectangle(49, 154, 185, 23));
			dictionaryRadioButton.setText("use a dictionary");
			dictionaryRadioButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					fileLabel.setEnabled(true);
					dicPathButton.setEnabled(true);
					dicPathTextField.setEnabled(true);
					decayCheckBox.setEnabled(true);
					if (decayCheckBox.isSelected()) {
						decayTimeLabel.setEnabled(true);
						decayTimeTextField.setEnabled(true);
					}
					checkBeans();
				}
			});
		}
		return dictionaryRadioButton;
	}

	/**
	 * This method initializes dicPathTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getDicPathTextField() {
		if (dicPathTextField == null) {
			dicPathTextField = new JTextField();
			dicPathTextField.setBounds(new Rectangle(109, 180, 297, 23));
			dicPathTextField.setEditable(false);
		}
		return dicPathTextField;
	}

	/**
	 * This method initializes dicPathButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDicPathButton() {
		if (dicPathButton == null) {
			dicPathButton = new JButton();
			dicPathButton.setBounds(new Rectangle(414, 180, 42, 23));
			dicPathButton.setEnabled(false);
			dicPathButton.setText("...");
			dicPathButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					if (fc.showOpenDialog(NewAnalysisDialog.this) == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						dicPathTextField.setText(file.getAbsolutePath());
						checkBeans();
					}
				}
			});
		}
		return dicPathButton;
	}

	/**
	 * This method initializes decayCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getDecayCheckBox() {
		if (decayCheckBox == null) {
			decayCheckBox = new JCheckBox();
			decayCheckBox.setBounds(new Rectangle(68, 210, 145, 23));
			decayCheckBox.setEnabled(false);
			decayCheckBox.setText("evolve dictionary");
			decayCheckBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					decayTimeLabel.setEnabled(!decayTimeLabel.isEnabled());
					decayTimeTextField.setEnabled(!decayTimeTextField.isEnabled());
					checkBeans();
				}
			});
		}
		return decayCheckBox;
	}

	/**
	 * This method initializes decayTimeTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getDecayTimeTextField() {
		if (decayTimeTextField == null) {
			decayTimeTextField = new JTextField();
			decayTimeTextField.setBounds(new Rectangle(326, 210, 81, 23));
			decayTimeTextField.setText("15000");
			decayTimeTextField.setEnabled(false);
			decayTimeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
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
		return decayTimeTextField;
	}

	/**
	 * This method initializes lsiCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getLsiCheckBox() {
		if (lsiCheckBox == null) {
			lsiCheckBox = new JCheckBox();
			lsiCheckBox.setBounds(new Rectangle(49, 292, 140, 23));
			lsiCheckBox.setText("activate LSI");
			lsiCheckBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					percTraceLabel.setEnabled(!percTraceLabel.isEnabled());
					percTraceSpinner.setEnabled(!percTraceSpinner.isEnabled());
					checkBeans();
				}
			});
		}
		return lsiCheckBox;
	}

	/**
	 * This method initializes percTraceSpinner
	 *
	 * @return javax.swing.JSpinner
	 */
	private JSpinner getPercTraceSpinner() {
		if (percTraceSpinner == null) {
			percTraceSpinner = new JSpinner(new SpinnerNumberModel(.90, .01, 1, .01));
			JSpinner.NumberEditor editor = new JSpinner.NumberEditor(percTraceSpinner, "0%");
			percTraceSpinner.setBounds(new Rectangle(394, 316, 62, 23));
			percTraceSpinner.setEditor(editor);
			percTraceSpinner.setEnabled(false);
		}
		return percTraceSpinner;
	}

	/**
	 * This method initializes tfIdfRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getTfIdfRadioButton() {
		if (tfIdfRadioButton == null) {
			tfIdfRadioButton = new JRadioButton();
			tfIdfRadioButton.setBounds(new Rectangle(49, 405, 131, 23));
			tfIdfRadioButton.setText("TF * IDF");
			tfIdfRadioButton.setSelected(true);
		}
		return tfIdfRadioButton;
	}
	
	/**
	 * This method initializes tfRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getTfRadioButton() {
		if (tfRadioButton == null) {
			tfRadioButton = new JRadioButton();
			tfRadioButton.setBounds(new Rectangle(49, 433, 147, 23));
			tfRadioButton.setText("term frequency");
		}
		return tfRadioButton;
	}

	/**
	 * This method initializes cancelButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setBounds(new Rectangle(76, 500, 160, 26));
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					NewAnalysisDialog.this.dispose();
				}
			});
		}
		return cancelButton;
	}

	/**
	 * This method initializes startButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getStartButton() {
		if (startButton == null) {
			startButton = new JButton();
			startButton.setBounds(new Rectangle(250, 500, 153, 26));
			startButton.setText("Start Analysis");
			startButton.setEnabled(false);
			startButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					startNewAnalysis();
				}
			});
		}
		return startButton;
	}

	private void checkBeans() {
		startButton.setEnabled(true);
		if (docsDirTextField.getText().length() == 0
		 || dictionaryRadioButton.isSelected() && dicPathTextField.getText().length() == 0
		 || decayCheckBox.isSelected() && decayTimeTextField.getText().length() == 0) {
			startButton.setEnabled(false);
		}
	}

	private void startNewAnalysis() {
		said_.prepareNewAnalysis();
		said_.setDocumentsDirectory(docsDirTextField.getText());

		if (dictionaryRadioButton.isSelected()) {
			if (decayCheckBox.isSelected()) {
				int decayTime = Integer.parseInt(getDecayTimeTextField().getText());
				said_.setDictionary(dicPathTextField.getText(), decayTime);
			}
			else {
				said_.setDictionary(dicPathTextField.getText());
			}
		}

		if (lsiCheckBox.isSelected()) {
			said_.setLatentSemanticIndexing((Double) percTraceSpinner.getValue());
		}

		if (tfRadioButton.isSelected()) {
			said_.setWeightScheme(SaidModel.WS_TF);
		}
		else {
			said_.setWeightScheme(SaidModel.WS_TFIDF);
		}

		this.dispose();
		new ProgressDialog(
				(JFrame) this.getParent(), 
				said_, 
				ProgressDialog.T_NEW_ANALYSIS, 
				null
				).setVisible(true);
		actionCancelled_ = false;
	}
	
	public void updateToShow() {
		actionCancelled_ = true;
		this.getRootPane().setDefaultButton(getStartButton());
	}

	public boolean actionCancelled() {
		return actionCancelled_;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
