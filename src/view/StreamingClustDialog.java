package view;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import model.SaidModel;
import classifier.Classifier;


public class StreamingClustDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel streamingClustContentPanel = null;
	private JLabel documentsLabel = null;
	private JTextField docsDirTextField = null;
	private JButton docsDirButton = null;
	private JLabel dictionaryLabel = null;
	private JTextField dicPathTextField = null;
	private JButton dicPathButton = null;
	private JLabel proxMeasureLabel = null;
	private JComboBox proxMeasureComboBox = null;
	private JLabel kLabel = null;
	private JTextField kTextField = null;
	private JButton cancelButton = null;
	private JButton startButton = null;
	private JFileChooser fileChooser = null;  //  @jve:decl-index=0:visual-constraint="615,30"

	private SaidModel said_;
	private boolean	  actionCancelled_;


	public StreamingClustDialog(JFrame parent, SaidModel said) {
		super(parent);
		initialize();
		new EscapeAction().register(this);
		said_ = said;
	}

	/**
	 * This method initializes this.
	 */
	private void initialize() {
		this.setSize(524, 308);
		this.setResizable(false);
		this.setTitle("Streaming Clustering");
		this.setName("scDialog");
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setContentPane(getStreamingClustContentPanel());
	}

	/**
	 * This method initializes streamingClustContentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getStreamingClustContentPanel() {
		if (streamingClustContentPanel == null) {
			documentsLabel = new JLabel();
			documentsLabel.setBounds(new Rectangle(20, 20, 170, 23));
			documentsLabel.setText("documents directory:");
			dictionaryLabel = new JLabel();
			dictionaryLabel.setBounds(new Rectangle(20, 70, 170, 23));
			dictionaryLabel.setText("dictionary:");
			proxMeasureLabel = new JLabel();
			proxMeasureLabel.setBounds(new Rectangle(20, 120, 170, 23));
			proxMeasureLabel.setText("proximity measure:");
			proxMeasureLabel.setEnabled(true);
			kLabel = new JLabel();
			kLabel.setBounds(new Rectangle(20, 170, 170, 23));
			kLabel.setText("start # clusters (K):");
			kLabel.setEnabled(true);
			streamingClustContentPanel = new JPanel();
			streamingClustContentPanel.setLayout(null);
			streamingClustContentPanel.add(documentsLabel, null);
			streamingClustContentPanel.add(getDocsDirTextField(), null);
			streamingClustContentPanel.add(getDocsDirButton(), null);
			streamingClustContentPanel.add(dictionaryLabel, null);
			streamingClustContentPanel.add(getDicPathTextField(), null);
			streamingClustContentPanel.add(getDicPathButton(), null);
			streamingClustContentPanel.add(proxMeasureLabel, null);
			streamingClustContentPanel.add(getProxMeasureComboBox(), null);
			streamingClustContentPanel.add(kLabel, null);
			streamingClustContentPanel.add(getKTextField(), null);
			streamingClustContentPanel.add(getCancelButton(), null);
			streamingClustContentPanel.add(getStartButton(), null);
		}
		return streamingClustContentPanel;
	}

	/**
	 * This method initializes docsDirTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getDocsDirTextField() {
		if (docsDirTextField == null) {
			docsDirTextField = new JTextField();
			docsDirTextField.setBounds(new Rectangle(190, 20, 260, 23));
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
			docsDirButton.setBounds(new Rectangle(460, 20, 42, 23));
			docsDirButton.setText("...");
			docsDirButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					fileChooser = getFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					if (fileChooser.showOpenDialog(StreamingClustDialog.this)
							== JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						docsDirTextField.setText(file.getAbsolutePath());
						checkBeans();
					}
				}
			});
		}
		return docsDirButton;
	}

	/**
	 * This method initializes dicPathTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getDicPathTextField() {
		if (dicPathTextField == null) {
			dicPathTextField = new JTextField();
			dicPathTextField.setBounds(new Rectangle(190, 70, 260, 23));
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
			dicPathButton.setBounds(new Rectangle(460, 70, 42, 23));
			dicPathButton.setText("...");
			dicPathButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					fileChooser = getFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					if (fileChooser.showOpenDialog(StreamingClustDialog.this) 
							== JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						dicPathTextField.setText(file.getAbsolutePath());
						checkBeans();
					}
				}
			});
		}
		return dicPathButton;
	}

	/**
	 * This method initializes proxMeasureComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getProxMeasureComboBox() {
		if (proxMeasureComboBox == null) {
			proxMeasureComboBox = new JComboBox();
			proxMeasureComboBox.setBounds(new Rectangle(190, 120, 310, 23));
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
			kTextField.setBounds(new Rectangle(190, 170, 310, 23));
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
			cancelButton.setBounds(new Rectangle(125, 240, 130, 26));
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					StreamingClustDialog.this.dispose();
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
			startButton.setBounds(new Rectangle(270, 240, 130, 26));
			startButton.setText("Start");
			startButton.setEnabled(false);
			startButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					startStreamingClustering();
				}
			});
		}
		return startButton;
	}

	/**
	 * This method initializes fileChooser
	 * 
	 * @return javax.swing.JFileChooser
	 */
	private JFileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			fileChooser.setSize(new Dimension(611, 400));
		}
		return fileChooser;
	}

	private void checkBeans() {
		startButton.setEnabled(true);
		if (docsDirTextField.getText().length() == 0
		 || dicPathTextField.getText().length() == 0
		 || kTextField.getText().length() == 0) {
			startButton.setEnabled(false);
		}
	}

	private void startStreamingClustering() {
		said_.prepareNewAnalysis();
		said_.setDocumentsDirectory(docsDirTextField.getText());
		said_.setDictionary(dicPathTextField.getText());
		said_.setWeightScheme(SaidModel.WS_TF);
		
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

		int k = Integer.parseInt(kTextField.getText());
		if (k == 0) {
			Util.showOptionPane(this, Util.M_CLUSTERS_ZERO);
			return;
		}
		
		this.dispose();
		int[] args = {k};
		new ProgressDialog(
				(JFrame) this.getParent(),
				said_,
				ProgressDialog.T_STREAM_CLUST, 
				args
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
