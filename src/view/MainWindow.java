package view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import model.Document;
import model.SaidModel;
import util.Round;
import view.atms.ClusterWordsATM;
import view.atms.DocumentWordsATM;
import view.atms.SystemWordsATM;
import classifier.Classifier;
import classifier.DataPoint;
import classifier.cluster.Cluster;


public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = -8985420459655355005L;

	private JPanel saidContentPanel = null;

	private JMenuBar saidMenuBar = null;

	private JMenu fileMenu = null;
	private JMenuItem newAnalysisMenuItem = null;
	private JMenuItem streamingClustMenuItem = null;
	private JMenuItem addMoreDocsMenuItem = null;
	private JMenuItem exitMenuItem = null;

	private JMenu clusteringMenu = null;
	private JMenuItem runKmeansMenuItem = null;
	private JMenuItem runBisectingKmeansMenuItem = null;
	private JMenuItem runHACMenuItem = null;
	private JMenuItem runAnotherIterationMenuItem = null;
	private JMenuItem runUntilConvergeMenuItem = null;
	private JMenuItem splitAgainMenuItem = null;
	private JMenuItem joinAgainMenuItem = null;

	private JMenu viewMenu = null;
	private JMenuItem logMenuItem = null;
	
	private JMenu toolsMenu = null;
	private JMenuItem exportClustersMenuItem = null;

	private JMenu helpMenu = null;
	private JMenuItem aboutMenuItem = null;

	private JTabbedPane tabbedPane = null;

	private JPanel generalPanel = null;
	private JLabel lblDocumentsLabel = null;
	private JLabel nDocsLabel = null;
	private JLabel nInvalidDocsLabel = null;
	private JLabel nValidWordsLabel = null;
	private JLabel nDiffWordsLabel = null;
	private JLabel lblDictionaryLabel = null;
	private JLabel dicSizeLabel = null;
	private JLabel nReplacedWordsLabel = null;
	private JLabel lblLSILabel = null;
	private JLabel nDimensionsLabel = null;
	private JLabel lblClusteringLabel = null;
	private JLabel finalKLabel = null;
	private JLabel kmeansItersLabel = null;
	private JLabel avgDispersionLabel = null;
	private JLabel lblWordsLabel = null;
	private JScrollPane systemWordsScrollPane = null;
	private JTable systemWordsTable = null;
	
	private JPanel clustersPanel = null;
	private JButton collapseExpandButton = null;
	private JTextField searchClustersTextField = null;
	private JComboBox searchClustersComboBox = null;
	private JSplitPane clustersSplitPane = null;
	private JPanel leftPanel = null;
	private JScrollPane clustersTreeScrollPane = null;
	private JTree clustersTree = null;
	private JPanel rightPanel = null;
	private JLabel lblAboutClusterLabel = null;
	private JLabel clusterSizeLabel = null;
	private JLabel dispersionLabel = null;
	private JLabel lblClusterWordsLabel = null;
	private JScrollPane clusterWordsScrollPane = null;
	private JTable clusterWordsTable = null;
	private JButton viewDocumentButton = null;
	private JLabel lblAboutDocumentLabel = null;
	private JLabel filePathLabel = null;
	private JLabel lblPreviewLabel = null;
	private JTextArea previewTextArea = null;
	private JLabel lblDocumentWordsLabel = null;
	private JScrollPane documentWordsScrollPane = null;
	private JTable documentWordsTable = null;
	
	private SaidModel            said_;
	private NewAnalysisDialog    naDialog_;
	private StreamingClustDialog scDialog_;
	private KmeansDialog         kmDialog_;
	private BsKmeansDialog       bskmDialog_;
	private HacDialog            hacDialog_;
	
	private int prevWidth  = 1015;
	private int prevHeight = 581;
	private int prevPanelWidth = 480;

	private GraphicsTab graphicsTab;

	private SilhouetteCoefficientsTab silhCoeffsTab;
	

	public MainWindow(SaidModel said_) {
		super();
		initialize();
		Util.centerWindowOnScreen(this);
		this.said_ = said_;
		naDialog_  = new NewAnalysisDialog(this, said_);
		scDialog_  = new StreamingClustDialog(this, said_);
	}

	/**
	 * Initialize the class.
	 */
	private void initialize() {
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(772, 581));
		this.setSize(1015, 581);
		this.setTitle("SAID");
		this.setContentPane(getSaidContentPanel());
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentResized(java.awt.event.ComponentEvent e) {
				int windowWidth  = MainWindow.this.getWidth();
				int windowHeight = MainWindow.this.getHeight();
				int diffWidth    = windowWidth  - prevWidth;
				int diffHeight   = windowHeight - prevHeight;
				
				systemWordsScrollPane.setSize(
						systemWordsScrollPane.getWidth() + diffWidth, 
						systemWordsScrollPane.getHeight() + diffHeight);
				systemWordsScrollPane.updateUI();
				
				clustersSplitPane.setSize(
						clustersSplitPane.getWidth() + diffWidth,
						clustersSplitPane.getHeight() + diffHeight);
				
				lblWordsLabel.setSize(
						lblWordsLabel.getWidth() + diffWidth, 
						lblWordsLabel.getHeight());
				
				clusterWordsScrollPane.setSize(
						clusterWordsScrollPane.getWidth(), 
						clusterWordsScrollPane.getHeight() + diffHeight);
				clusterWordsScrollPane.updateUI();
				
				int height1 = diffHeight / 2;
				int height2 = height1 + (diffHeight % 2);
				previewTextArea.setSize(
						previewTextArea.getWidth(),
						previewTextArea.getHeight() + height2);
				viewDocumentButton.setBounds(
						viewDocumentButton.getX(), 
						viewDocumentButton.getY() + height2,
						viewDocumentButton.getWidth(), 
						viewDocumentButton.getHeight());
				lblDocumentWordsLabel.setBounds(
						lblDocumentWordsLabel.getX(), 
						lblDocumentWordsLabel.getY() + height2,
						lblDocumentWordsLabel.getWidth(),
						lblDocumentWordsLabel.getHeight());
				documentWordsScrollPane.setBounds(
						documentWordsScrollPane.getX(),
						documentWordsScrollPane.getY() + height2,
						documentWordsScrollPane.getWidth(), 
						documentWordsScrollPane.getHeight() + height1);
								
				prevWidth  = windowWidth;
				prevHeight = windowHeight;
			}
		});
	}
	
	/**
	 * Return the saidContentPanel property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getSaidContentPanel() {
		if (saidContentPanel == null) {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setVgap(5);
			saidContentPanel = new JPanel();
			saidContentPanel.setLayout(borderLayout);
			saidContentPanel.setName("contentPaneJPanel");
			saidContentPanel.add(getSaidMenuBar(), BorderLayout.NORTH);
			saidContentPanel.add(getTabbedPane(), BorderLayout.CENTER);
		}
		return saidContentPanel;
	}

	/**
	 * This method initializes saidMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getSaidMenuBar() {
		if (saidMenuBar == null) {
			saidMenuBar = new JMenuBar();
			saidMenuBar.add(getFileMenu());
			saidMenuBar.add(getClusteringMenu());
			saidMenuBar.add(getViewMenu());
			saidMenuBar.add(getToolsMenu());
			saidMenuBar.add(getHelpMenu());
		}
		return saidMenuBar;
	}

	/**
	 * This method initializes fileMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.setMnemonic(KeyEvent.VK_F);
			fileMenu.add(getNewAnalysisMenuItem());
			fileMenu.add(getStreamingClustMenuItem());
			fileMenu.add(getAddMoreDocsMenuItem());
			fileMenu.add(new JSeparator());
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}

	/**
	 * This method initializes newAnalysisMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getNewAnalysisMenuItem() {
		if (newAnalysisMenuItem == null) {
			newAnalysisMenuItem = new JMenuItem();
			newAnalysisMenuItem.setText("New Analysis");
			newAnalysisMenuItem.setAccelerator(KeyStroke.
					getKeyStroke('N', java.awt.Event.CTRL_MASK, false));
			newAnalysisMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					newAnalysis();
				}
			});
		}
		return newAnalysisMenuItem;
	}

	/**
	 * This method initializes streamingClustMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getStreamingClustMenuItem() {
		if (streamingClustMenuItem == null) {
			streamingClustMenuItem = new JMenuItem();
			streamingClustMenuItem.setText("Streaming Clustering");
		}
		streamingClustMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				streamingClustering();
			}
		});
		return streamingClustMenuItem;
	}
	
	/**
	 * This method initializes addMoreDocsMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getAddMoreDocsMenuItem() {
		if (addMoreDocsMenuItem == null) {
			addMoreDocsMenuItem = new JMenuItem();
			addMoreDocsMenuItem.setText("Add More Documents");
			addMoreDocsMenuItem.setEnabled(false);
			addMoreDocsMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addStreamingClusteringDocuments();			
				}
			});
		}
		return addMoreDocsMenuItem;
	}
	
	/**
	 * This method initializes exitMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new JMenuItem();
			exitMenuItem.setText("Exit");
			exitMenuItem.setAccelerator(KeyStroke.getKeyStroke('Q', java.awt.Event.CTRL_MASK, false));
			exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MainWindow.this.dispose();
				}
			});
		}
		return exitMenuItem;
	}

	/**
	 * This method initializes clusteringMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getClusteringMenu() {
		if (clusteringMenu == null) {
			clusteringMenu = new JMenu();
			clusteringMenu.setText("Clustering");
			clusteringMenu.setMnemonic(KeyEvent.VK_C);
			clusteringMenu.add(getRunKmeansMenuItem());
			clusteringMenu.add(getRunBisectingKmeansMenuItem());
			clusteringMenu.add(getRunHACMenuItem());
			clusteringMenu.add(new JSeparator());
			clusteringMenu.add(getRunAnotherIterationMenuItem());
			clusteringMenu.add(getRunUntilConvergeMenuItem());
			clusteringMenu.add(getSplitAgainMenuItem());
			clusteringMenu.add(getJoinAgainMenuItem());
		}
		return clusteringMenu;
	}

	/**
	 * This method initializes runKmeansMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getRunKmeansMenuItem() {
		if (runKmeansMenuItem == null) {
			runKmeansMenuItem = new JMenuItem();
			runKmeansMenuItem.setText("Run Kmeans");
			runKmeansMenuItem.setAccelerator(KeyStroke.getKeyStroke('K', java.awt.Event.CTRL_MASK, false));
			runKmeansMenuItem.setEnabled(false);
			runKmeansMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					runClusteringAlgorithm(Classifier.KMEANS);
				}
			});
		}
		return runKmeansMenuItem;
	}

	/**
	 * This method initializes runBisectingKmeansMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getRunBisectingKmeansMenuItem() {
		if (runBisectingKmeansMenuItem == null) {
			runBisectingKmeansMenuItem = new JMenuItem();
			runBisectingKmeansMenuItem.setText("Run Bisecting-kmeans");
			runBisectingKmeansMenuItem.setAccelerator(KeyStroke.getKeyStroke('B', java.awt.Event.CTRL_MASK, false));
			runBisectingKmeansMenuItem.setEnabled(false);
			runBisectingKmeansMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					runClusteringAlgorithm(Classifier.BS_KMEANS);
				}
			});
		}
		return runBisectingKmeansMenuItem;
	}

	/**
	 * This method initializes runHACMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getRunHACMenuItem() {
		if (runHACMenuItem == null) {
			runHACMenuItem = new JMenuItem();
			runHACMenuItem.setText("Run HAC");
			runHACMenuItem.setAccelerator(KeyStroke.getKeyStroke(
					'H', java.awt.Event.CTRL_MASK, false));
			runHACMenuItem.setEnabled(false);
			runHACMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					runClusteringAlgorithm(Classifier.HAC);
				}
			});
		}
		return runHACMenuItem;
	}

	/**
	 * This method initializes runAnotherIterationMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getRunAnotherIterationMenuItem() {
		if (runAnotherIterationMenuItem == null) {
			runAnotherIterationMenuItem = new JMenuItem();
			runAnotherIterationMenuItem.setText("Run Another Iteration");
			runAnotherIterationMenuItem.setAccelerator(
					KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0, false));
			runAnotherIterationMenuItem.setEnabled(false);
			runAnotherIterationMenuItem
			.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					new ProgressDialog(
							MainWindow.this, 
							said_,
							ProgressDialog.T_RUN_1_KMEANS_ITER, 
							null
							).setVisible(true);
					if (said_.classifier().kmeans().itConverged()) {
						runAnotherIterationMenuItem.setEnabled(false);
						runUntilConvergeMenuItem.setEnabled(false);
					}
					showClusteringInfo("");
					updateClusterList();
				}
			});
		}
		return runAnotherIterationMenuItem;
	}

	/**
	 * This method initializes runUntilConvergeMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getRunUntilConvergeMenuItem() {
		if (runUntilConvergeMenuItem == null) {
			runUntilConvergeMenuItem = new JMenuItem();
			runUntilConvergeMenuItem.setText("Run Until Converge");
			runUntilConvergeMenuItem.setAccelerator(KeyStroke.getKeyStroke(
					KeyEvent.VK_F5, 0, false));
			runUntilConvergeMenuItem.setEnabled(false);
			runUntilConvergeMenuItem
			.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					new ProgressDialog(
							MainWindow.this, 
							said_,
							ProgressDialog.T_RUN_KMEANS_CONVERGE, 
							null
							).setVisible(true);
					showClusteringInfo("");
					runAnotherIterationMenuItem.setEnabled(false);
					runUntilConvergeMenuItem.setEnabled(false);
					updateClusterList();
				}
			});
		}
		return runUntilConvergeMenuItem;
	}

	/**
	 * This method initializes splitAgainMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSplitAgainMenuItem() {
		if (splitAgainMenuItem == null) {
			splitAgainMenuItem = new JMenuItem();
			splitAgainMenuItem.setText("Split Again");
			splitAgainMenuItem.setAccelerator(KeyStroke.getKeyStroke(
					KeyEvent.VK_F6, 0, false));
			splitAgainMenuItem.setEnabled(false);
			splitAgainMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					new ProgressDialog(
							MainWindow.this, 
							said_,
							ProgressDialog.T_RUN_1_BSKMEANS_ITER, 
							null
							).setVisible(true);
					showClusteringInfo("");
					if (said_.classifier().getFinalK() == said_.getNumDocuments()) {
						splitAgainMenuItem.setEnabled(false);
					}
					updateClusterList();
				}
			});
		}
		return splitAgainMenuItem;
	}
	/**
	 * This method initializes joinAgainMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJoinAgainMenuItem() {
		if (joinAgainMenuItem == null) {
			joinAgainMenuItem = new JMenuItem();
			joinAgainMenuItem.setText("Join Again");
			joinAgainMenuItem.setAccelerator(KeyStroke.getKeyStroke(
					KeyEvent.VK_F7, 0, false));
			joinAgainMenuItem.setEnabled(false);
			joinAgainMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					new ProgressDialog(
							MainWindow.this, 
							said_,
							ProgressDialog.T_RUN_1_HAC_ITER, 
							null
							).setVisible(true);
					showClusteringInfo("");
					if (said_.classifier().getFinalK() == 1) {
						joinAgainMenuItem.setEnabled(false);
					}
					updateClusterList();
				}
			});
		}
		return joinAgainMenuItem;
	}
	
	/**
	 * This method updates the JCombobox in graphicsTab
	 */
	private void updateClusterList() {
		graphicsTab.setSaid(said_);
		updateHeatMapComboBox();
	}

	/**
	 * This method initializes viewMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getViewMenu() {
		if (viewMenu == null) {
			viewMenu = new JMenu();
			viewMenu.setText("View");
			viewMenu.setMnemonic(KeyEvent.VK_V);
			viewMenu.add(getLogMenuItem());
		}
		return viewMenu;
	}

	/**
	 * This method initializes logMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getLogMenuItem() {
		if (logMenuItem == null) {
			logMenuItem = new JMenuItem();
			logMenuItem.setText("Log");
			logMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					new LogDialog(MainWindow.this, said_).setVisible(true);
				}
			});
		}
		return logMenuItem;
	}
	
	/**
	 * This method initializes toolsMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getToolsMenu() {
		if (toolsMenu == null) {
			toolsMenu = new JMenu();
			toolsMenu.setText("Tools");
			toolsMenu.setMnemonic(KeyEvent.VK_T);
			toolsMenu.add(getExportClustersMenuItem());
		}
		return toolsMenu;
	}

	/**
	 * This method initializes exportClustersMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getExportClustersMenuItem() {
		if (exportClustersMenuItem == null) {
			exportClustersMenuItem = new JMenuItem();
			exportClustersMenuItem.setText("Export Clusters");
			exportClustersMenuItem.setEnabled(false);
			exportClustersMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					int returnVal = fc.showSaveDialog(MainWindow.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File f = fc.getSelectedFile ();
						if (f.exists ()) {
							if (Util.showOptionPane(MainWindow.this, Util.M_FILE_OVERWRITE) 
									== JOptionPane.NO_OPTION) {
								return;
							}
						}
						exportClusters(f);
					}
				}
			});
		}
		return exportClustersMenuItem;
	}

	/**
	 * This method initializes helpMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setText("Help");
			helpMenu.setMnemonic(KeyEvent.VK_H);
			helpMenu.add(getAboutMenuItem());
			helpMenu.add(getLeftPanel());
		}
		return helpMenu;
	}

	/**
	 * This method initializes aboutMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAboutMenuItem() {
		if (aboutMenuItem == null) {
			aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About");
			aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					AboutDialog ad = new AboutDialog(MainWindow.this);
					ad.setVisible(true);
				}
			});
		}
		return aboutMenuItem;
	}

	/**
	 * This method initializes tabbedPane
	 * 
	 * @return JTabbedPane
	 */
	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("General", null, getGeneralPanel(), null);
			tabbedPane.addTab("Clusters", null, getClustersPanel(), null);
			tabbedPane.addTab("Graphics", null, graphicsTab = new GraphicsTab(MainWindow.this));
			tabbedPane.addTab("Silhouette Coefficients", null, silhCoeffsTab = new SilhouetteCoefficientsTab(MainWindow.this));
			tabbedPane.setEnabledAt(0, false);
			tabbedPane.setEnabledAt(1, false);
			tabbedPane.setEnabledAt(2, false);
			tabbedPane.setEnabledAt(3, false);
		}
		return tabbedPane;
	}

	/**
	 * This method initializes generalPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getGeneralPanel() {
		if (generalPanel == null) {
			lblDocumentsLabel = new JLabel();
			lblDocumentsLabel.setBounds(new Rectangle(10, 10, 350, 23));
			lblDocumentsLabel.setBackground(Color.white);
			lblDocumentsLabel.setOpaque(true);
			lblDocumentsLabel.setText("Documents:");
			nDocsLabel = new JLabel();
			nDocsLabel.setBounds(new Rectangle(10, 40, 350, 23));
			nDocsLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			nDocsLabel.setText("# documents:");
			nInvalidDocsLabel = new JLabel();
			nInvalidDocsLabel.setBounds(new Rectangle(10, 70, 350, 23));
			nInvalidDocsLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			nInvalidDocsLabel.setText("# invalid documents:");
			nValidWordsLabel = new JLabel();
			nValidWordsLabel.setBounds(new Rectangle(10, 100, 350, 23));
			nValidWordsLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			nValidWordsLabel.setText("# valid words:");
			nDiffWordsLabel = new JLabel();
			nDiffWordsLabel.setBounds(new Rectangle(10, 130, 350, 23));
			nDiffWordsLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			nDiffWordsLabel.setText("# different words:");
			lblDictionaryLabel = new JLabel();
			lblDictionaryLabel.setBounds(new Rectangle(10, 180, 350, 23));
			lblDictionaryLabel.setBackground(Color.white);
			lblDictionaryLabel.setOpaque(true);
			lblDictionaryLabel.setText("Dictionary:");
			dicSizeLabel = new JLabel();
			dicSizeLabel.setBounds(new Rectangle(10, 210, 350, 23));
			dicSizeLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			dicSizeLabel.setText("dictionary size:");
			nReplacedWordsLabel = new JLabel();
			nReplacedWordsLabel.setBounds(new Rectangle(10, 240, 350, 23));
			nReplacedWordsLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			nReplacedWordsLabel.setText("# replaced words:");
			lblLSILabel = new JLabel();
			lblLSILabel.setBounds(new Rectangle(10, 290, 350, 23));
			lblLSILabel.setBackground(Color.white);
			lblLSILabel.setOpaque(true);
			lblLSILabel.setText("Latent Semantic Indexing:");
			nDimensionsLabel = new JLabel();
			nDimensionsLabel.setBounds(new Rectangle(10, 320, 350, 23));
			nDimensionsLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			nDimensionsLabel.setText("# dimensions:");
			lblClusteringLabel = new JLabel();
			lblClusteringLabel.setBounds(new Rectangle(10, 375, 350, 23));
			lblClusteringLabel.setBackground(Color.white);
			lblClusteringLabel.setOpaque(true);
			lblClusteringLabel.setText("Clustering:");
			finalKLabel = new JLabel();
			finalKLabel.setBounds(new Rectangle(10, 405, 350, 23));
			finalKLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			finalKLabel.setText("# created clusters (k):");
			kmeansItersLabel = new JLabel();
			kmeansItersLabel.setBounds(new Rectangle(10, 435, 350, 23));
			kmeansItersLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			kmeansItersLabel.setText("# kmeans iterations:");
			avgDispersionLabel = new JLabel();
			avgDispersionLabel.setBounds(new Rectangle(10, 465, 350, 23));
			avgDispersionLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			avgDispersionLabel.setText("average dispersion:");
			lblWordsLabel = new JLabel();
			lblWordsLabel.setBounds(new Rectangle(405, 10, 593, 23));
			lblWordsLabel.setBackground(Color.white);
			lblWordsLabel.setOpaque(true);
			lblWordsLabel.setText("Words:");
			generalPanel = new JPanel();
			generalPanel.setLayout(null);
			generalPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
			generalPanel.add(lblDocumentsLabel, null);
			generalPanel.add(nDocsLabel, null);
			generalPanel.add(nInvalidDocsLabel, null);
			generalPanel.add(nValidWordsLabel, null);
			generalPanel.add(nDiffWordsLabel, null);
			generalPanel.add(lblDictionaryLabel, null);
			generalPanel.add(dicSizeLabel, null);
			generalPanel.add(nReplacedWordsLabel, null);
			generalPanel.add(lblLSILabel, null);
			generalPanel.add(nDimensionsLabel, null);
			generalPanel.add(lblClusteringLabel, null);
			generalPanel.add(finalKLabel, null);
			generalPanel.add(kmeansItersLabel, null);
			generalPanel.add(avgDispersionLabel, null);
			generalPanel.add(lblWordsLabel, null);
			generalPanel.add(getSystemWordsScrollPane(), null);
		}
		return generalPanel;
	}
	
	/**
	 * This method initializes systemWordsScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSystemWordsScrollPane() {
		if (systemWordsScrollPane == null) {
			systemWordsScrollPane = new JScrollPane();
			systemWordsScrollPane.setBounds(new Rectangle(405, 40, 593, 448));
			systemWordsScrollPane.setViewportView(getSystemWordsTable());
		}
		return systemWordsScrollPane;
	}

	/**
	 * This method initializes systemWordsTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getSystemWordsTable() {
		if (systemWordsTable == null) {
			systemWordsTable = new JTable();
			systemWordsTable = new JTable(
					new String[][] {},
					new String[] { "Word", "Collection Frequency", 
							       "Document Frequency", "Weight (%)" });
			systemWordsTable.setEnabled(false);
			systemWordsTable.getTableHeader().setReorderingAllowed(false);
			systemWordsTable.setAutoCreateRowSorter(true);

		}
		return systemWordsTable;
	}
			
	/**
	 * This method initializes clustersPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getClustersPanel() {
		if (clustersPanel == null) {
			clustersPanel = new JPanel();
			clustersPanel.setLayout(null);
			clustersPanel.add(getCollapseExpandButton(), null);
			clustersPanel.add(getSearchClustersTextField(), null);
			clustersPanel.add(getSearchClustersComboBox(), null);
			clustersPanel.add(getClustersSplitPane(), null);
		}
		return clustersPanel;
	}
	
	/**
	 * This method initializes collapseExpandButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCollapseExpandButton() {
		if (collapseExpandButton == null) {
			collapseExpandButton = new JButton();
			collapseExpandButton.setText("Collapse All");
			collapseExpandButton.setBounds(new Rectangle(10, 10, 117, 23));
			collapseExpandButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (collapseExpandButton.getText().equals("Collapse All")) {
						for (int i = 0; i < clustersTree.getRowCount(); i++) {
							clustersTree.collapseRow(i);
						}
						collapseExpandButton.setText("Expand All");
					}
					else {
						for (int i = 0; i < clustersTree.getRowCount(); i++) {
							clustersTree.expandRow(i);
						}
						collapseExpandButton.setText("Collapse All");
					}
				}
			});
		}
		return collapseExpandButton;
	}
	
	/**
	 * This method initializes searchClustersTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSearchClustersTextField() {
		if (searchClustersTextField == null) {
			searchClustersTextField = new JTextField();
			searchClustersTextField.setBounds(new Rectangle(160, 10, 350, 23));
			searchClustersTextField.setFont(new Font("Dialog", Font.ITALIC, 12));
			searchClustersTextField.setText("search...");
		}
		searchClustersTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent e) {
				String query   = searchClustersTextField.getText();
				int caretPos   = searchClustersTextField.getCaretPosition();
				int startIndex = query.lastIndexOf(" ", caretPos - 1);
				int endIndex   = query.indexOf(" ", caretPos);
				
				String leadingQuery;
				String trailingQuery;
				
				if (startIndex == -1) {
					startIndex = 0;
					leadingQuery = "";
				}
				else {
					startIndex++;
					leadingQuery = query.substring(0, startIndex);
				}
				
				if (endIndex == -1) {
					endIndex = caretPos;
					trailingQuery = " ";
				}
				else {
					trailingQuery  = query.substring(endIndex);
				}
				
				String incompletedWord = query.substring(startIndex, caretPos);
				
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (searchClustersComboBox.getItemCount() == 0) {
						return;
					}
					String selectedWord = (String) searchClustersComboBox.getSelectedItem();
					searchClustersTextField.setText(leadingQuery + selectedWord + trailingQuery);
					caretPos = leadingQuery.length() + selectedWord.length() + 1;
					searchClustersTextField.setCaretPosition(caretPos);
					searchClustersComboBox.removeAllItems();
					searchClustersComboBox.hidePopup();
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP) {
					int index = searchClustersComboBox.getSelectedIndex();
					if (index > 0) {
						searchClustersComboBox.setSelectedIndex(index - 1);
					}
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					int index = searchClustersComboBox.getSelectedIndex();
					if (index < searchClustersComboBox.getItemCount() - 1) {
						searchClustersComboBox.setSelectedIndex(index + 1);
					}
				}
				else {
					searchClustersComboBox.removeAllItems();
					searchClustersComboBox.hidePopup();
					if (incompletedWord.length() > 0) {
						ArrayList<String> words = said_.getMatchedWordList(incompletedWord);
						if (words.size() > 0) {
							for (String word: words) {
								searchClustersComboBox.addItem(word);
								// don't show more than 8 hints
								if (searchClustersComboBox.getItemCount() == 8) {
									break;
								}
							}
							searchClustersComboBox.showPopup();
						}
					}
				}
				showClusteringInfo(searchClustersTextField.getText());
			}
		});
		searchClustersTextField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent e) {    
				if (searchClustersTextField.getText().length() == 0) {
					searchClustersTextField.setFont(new Font("Dialog", Font.ITALIC, 12));
					searchClustersTextField.setText("search...");
				}
			}
			public void focusGained(java.awt.event.FocusEvent e) {
				if (searchClustersTextField.getText().equals("search...")) {
					searchClustersTextField.setFont(new Font("Dialog", Font.PLAIN, 12));
					searchClustersTextField.setText("");
				}
			}
		});
		return searchClustersTextField;
	}

	/**
	 * This method initializes searchClustersComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getSearchClustersComboBox() {
		if (searchClustersComboBox == null) {
			searchClustersComboBox = new JComboBox();
			searchClustersComboBox.setBounds(new Rectangle(160, 10, 349, 23));
			searchClustersComboBox.setFont(new Font("Dialog", Font.PLAIN, 12));
			searchClustersComboBox.setOpaque(false);
			searchClustersComboBox.setFocusable(false);
		}
		return searchClustersComboBox;
	}
	
	/**
	 * This method initializes clustersSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getClustersSplitPane() {
		if (clustersSplitPane == null) {
			clustersSplitPane = new JSplitPane();
			clustersSplitPane.setBounds(new Rectangle(10, 40, 988, 448));
			clustersSplitPane.setDividerSize(7);
			clustersSplitPane.setDividerLocation(500);
			clustersSplitPane.setLeftComponent(getLeftPanel());
			clustersSplitPane.setRightComponent(getRightPanel());
		}
		return clustersSplitPane;
	}

	/**
	 * This method initializes leftPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLeftPanel() {
		if (leftPanel == null) {
			leftPanel = new JPanel();
			leftPanel.setLayout(new BorderLayout());
			leftPanel.setMinimumSize(new Dimension(300, 0));
			leftPanel.add(getClustersTreeScrollPane(), BorderLayout.CENTER);
			leftPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
				public void componentResized(java.awt.event.ComponentEvent e) {
					int dividerLocation = MainWindow.this.getClustersSplitPane().getDividerLocation();
					searchClustersTextField.setBounds(
							searchClustersTextField.getX(), 
							searchClustersTextField.getY(), 
							dividerLocation - searchClustersTextField.getX() + 10, 
							searchClustersTextField.getHeight());
					searchClustersComboBox.setBounds(
							searchClustersComboBox.getX(),
							searchClustersComboBox.getY(),
							dividerLocation - searchClustersTextField.getX() + 9, 
							searchClustersComboBox.getHeight());	
				}
			});
		}
		return leftPanel;
	}
	
	/**
	 * This method initializes clustersTreeScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getClustersTreeScrollPane() {
		if (clustersTreeScrollPane == null) {
			clustersTreeScrollPane = new JScrollPane();
			clustersTreeScrollPane.setViewportView(getClustersTree());
		}
		return clustersTreeScrollPane;
	}

	/**
	 * This method initializes clustersTree
	 * 
	 * @return javax.swing.JTree
	 */
	private JTree getClustersTree() {
		if (clustersTree == null) {
			clustersTree = new JTree(new DefaultTreeModel(null));
			clustersTree.setRootVisible(false);
			clustersTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
				public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)
							clustersTree.getLastSelectedPathComponent();
					if (node == null) {
						return;
					}
					TreeModel model = clustersTree.getModel();
					if (node.isLeaf()) {
						DataPoint dp = (DataPoint) node.getUserObject();
						showDocumentInfo(dp.getIndex());
					}
					else {
						int clusterIndex = model.getIndexOfChild(node.getParent(), node);
						showClusterInfo(clusterIndex);
					}
				}
			});
		}
		return clustersTree;
	}

	/**
	 * This method initializes rightPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getRightPanel() {
		if (rightPanel == null) {
			rightPanel = new JPanel();
			rightPanel.setLayout(null);
			rightPanel.setMinimumSize(new Dimension(300, 0));
			lblAboutClusterLabel = new JLabel();
			lblAboutClusterLabel.setBounds(new Rectangle(10, 10, 460, 23));
			lblAboutClusterLabel.setOpaque(true);
			lblAboutClusterLabel.setText("About Selected Cluster");
			lblAboutClusterLabel.setBackground(Color.white);
			clusterSizeLabel = new JLabel();
			clusterSizeLabel.setBounds(new Rectangle(10, 40, 460, 23));
			clusterSizeLabel.setText("# documents:");
			clusterSizeLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			dispersionLabel = new JLabel();
			dispersionLabel.setBounds(new Rectangle(10, 70, 460, 23));
			dispersionLabel.setText("dispersion: ");
			dispersionLabel.setFont(new Font("Dialog", Font.PLAIN, 12));			
			lblClusterWordsLabel = new JLabel();
			lblClusterWordsLabel.setBounds(new Rectangle(10, 112, 460, 23));
			lblClusterWordsLabel.setOpaque(true);
			lblClusterWordsLabel.setText("Cluster Words");
			lblClusterWordsLabel.setBackground(Color.white);
			lblAboutDocumentLabel = new JLabel();
			lblAboutDocumentLabel.setBounds(new Rectangle(10, 10, 460, 23));
			lblAboutDocumentLabel.setBackground(Color.white);
			lblAboutDocumentLabel.setOpaque(true);
			lblAboutDocumentLabel.setText("About Selected Document:");
			filePathLabel = new JLabel();
			filePathLabel.setBounds(new Rectangle(10, 40, 460, 23));
			filePathLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			filePathLabel.setText("file path:");
			lblPreviewLabel = new JLabel();
			lblPreviewLabel.setBounds(new Rectangle(10, 75, 460, 23));
			lblPreviewLabel.setBackground(Color.white);
			lblPreviewLabel.setOpaque(true);
			lblPreviewLabel.setText("Preview:");
			lblDocumentWordsLabel = new JLabel();
			lblDocumentWordsLabel.setBounds(new Rectangle(10, 273, 460, 23));
			lblDocumentWordsLabel.setOpaque(true);
			lblDocumentWordsLabel.setBackground(Color.white);
			lblDocumentWordsLabel.setText("Document Words:");
			rightPanel.add(lblAboutClusterLabel, null);
			rightPanel.add(clusterSizeLabel, null);
			rightPanel.add(dispersionLabel, null);
			rightPanel.add(lblClusterWordsLabel, null);
			rightPanel.add(getClusterWordsScrollPane(), null);
			rightPanel.add(lblAboutDocumentLabel, null);
			rightPanel.add(filePathLabel, null);		
			rightPanel.add(lblPreviewLabel, null);
			rightPanel.add(getpreviewTextArea(), null);
			rightPanel.add(lblDocumentWordsLabel, null);
			rightPanel.add(getDocumentWordsScrollPane(), null);
			rightPanel.add(getViewDocumentButton(), null);
			rightPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
				public void componentResized(java.awt.event.ComponentEvent e) {
					int panelWidth  = MainWindow.this.getRightPanel().getWidth();
					int diffWidth   = panelWidth  - prevPanelWidth;
					
					lblAboutClusterLabel.setSize(
							lblAboutClusterLabel.getWidth() + diffWidth, 
							lblAboutClusterLabel.getHeight());
					
					lblClusterWordsLabel.setSize(
							lblClusterWordsLabel.getWidth() + diffWidth, 
							lblClusterWordsLabel.getHeight());
					
					clusterWordsScrollPane.setSize(
							clusterWordsScrollPane.getWidth() + diffWidth, 
							clusterWordsScrollPane.getHeight());
					clusterWordsScrollPane.updateUI();
					
					lblAboutDocumentLabel.setSize(
							lblAboutDocumentLabel.getWidth() + diffWidth, 
							lblAboutDocumentLabel.getHeight());
					
					filePathLabel.setSize(
							filePathLabel.getWidth() + diffWidth, 
							filePathLabel.getHeight());
					
					lblPreviewLabel.setSize(
							lblPreviewLabel.getWidth() + diffWidth, 
							lblPreviewLabel.getHeight());
					
					previewTextArea.setSize(
							previewTextArea.getWidth() + diffWidth,
							previewTextArea.getHeight());
					
					lblDocumentWordsLabel.setSize(
							lblDocumentWordsLabel.getWidth() + diffWidth,
							lblDocumentWordsLabel.getHeight());
					
					documentWordsScrollPane.setSize(
							documentWordsScrollPane.getWidth() + diffWidth,
							documentWordsScrollPane.getHeight());
					documentWordsScrollPane.updateUI();
							
					prevPanelWidth  = panelWidth;
				}
			});
		}
		return rightPanel;
	}

	/**
	 * This method initializes clusterWordsScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getClusterWordsScrollPane() {
		if (clusterWordsScrollPane == null) {
			clusterWordsScrollPane = new JScrollPane();
			clusterWordsScrollPane.setBounds(new Rectangle(10, 142, 460, 305));
			clusterWordsScrollPane.setViewportView(getClusterWordsTable());
		}
		return clusterWordsScrollPane;
	}

	/**
	 * This method initializes clusterWordsTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getClusterWordsTable() {
		if (clusterWordsTable == null) {
			clusterWordsTable = new JTable();
			clusterWordsTable.getTableHeader().setReorderingAllowed(false);
			clusterWordsTable.setEnabled(false);
			clusterWordsTable.setAutoCreateRowSorter(true);
		}
		return clusterWordsTable;
	}
	
	/**
	 * This method initializes previewTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getpreviewTextArea() {
		if (previewTextArea == null) {
			previewTextArea = new JTextArea();
			previewTextArea.setBounds(new Rectangle(10, 105, 460, 123));
			previewTextArea.setBackground(filePathLabel.getBackground());
			previewTextArea.setBorder(
					javax.swing.BorderFactory.createLineBorder(Color.lightGray));
			previewTextArea.setWrapStyleWord(true);
			previewTextArea.setEditable(false);
			previewTextArea.setLineWrap(true);
		}
		return previewTextArea;
	}
	
	/**
	 * This method initializes viewDocumentButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getViewDocumentButton() {
		if (viewDocumentButton == null) {
			viewDocumentButton = new JButton();
			viewDocumentButton.setText("More");
			viewDocumentButton.setBounds(new Rectangle(10, 233, 90, 23));
			viewDocumentButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)
							clustersTree.getLastSelectedPathComponent();
					DataPoint dp = (DataPoint) node.getUserObject();
					Document doc = said_.documents().get(dp.getIndex());
					new ViewDocumentDialog(MainWindow.this, doc).setVisible(true);
				}
			});
		}
		return viewDocumentButton;
	}
	
	/**
	 * This method initializes documentWordsScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getDocumentWordsScrollPane() {
		if (documentWordsScrollPane == null) {
			documentWordsScrollPane = new JScrollPane();
			documentWordsScrollPane.setBounds(new Rectangle(10, 303, 460, 143));
			documentWordsScrollPane.setViewportView(getDocumentWordsTable());
		}
		return documentWordsScrollPane;
	}

	/**
	 * This method initializes documentWordsTable
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getDocumentWordsTable() {
		if (documentWordsTable == null) {
			documentWordsTable = new JTable();
			documentWordsTable.getTableHeader().setReorderingAllowed(false);
			documentWordsTable.setEnabled(false);
			documentWordsTable.setAutoCreateRowSorter(true);
		}
		return documentWordsTable;
	}

	private void newAnalysis() {
		Util.centerWithinParent(naDialog_);
		naDialog_.updateToShow();
		naDialog_.setVisible(true);
		if (naDialog_.actionCancelled()) {
			return;
		}
		
		kmDialog_   = new KmeansDialog(this, said_);
		bskmDialog_ = new BsKmeansDialog(this, said_);
		hacDialog_  = new HacDialog(this, said_);
		
		nDocsLabel.setText("# documents: " + said_.getNumDocuments());
		int nInvDocs = said_.getNumInvalidDocuments();
		nInvalidDocsLabel.setText("# invalid documents: " + nInvDocs);
		nValidWordsLabel.setText("# valid words: "+ said_.wdb().getNumValidWords());
		nDiffWordsLabel.setText("# different words: " + said_.wdb().getNumDifferentWords());

		if (said_.isDictionaryEnabled()) {
			dicSizeLabel.setText("dictionary size: " + said_.dictionary().size());
			if (said_.dictionary().isDecayEnabled()) {
				int nReplacedWords = said_.dictionary().getNumReplacedWords();
				nReplacedWordsLabel.setText("# replaced words: " + nReplacedWords);
			}
			else {
				nReplacedWordsLabel.setText("# replaced words: -");
			}
		}
		else {
			// not using a dictionary
			dicSizeLabel.setText("dictionary size: -");
			nReplacedWordsLabel.setText("# replaced words: -");
		}

		if (said_.isLatentSemanticIndexingEnabled()) {
			nDimensionsLabel.setText("# dimensions: " + model.Document.vectorSize());
		}
		else {
			nDimensionsLabel.setText("# dimensions: -");
		}

		finalKLabel.setText("# created clusters (k):");
		kmeansItersLabel.setText("# kmeans iterations:");
		avgDispersionLabel.setText("average dispersion:");
		
		systemWordsTable.setModel(new SystemWordsATM(said_));
		systemWordsTable.getRowSorter().toggleSortOrder(0);
		
		this.silhCoeffsTab.setSaid(said_);
		this.graphicsTab.setSaid(said_);
		
		tabbedPane.setSelectedIndex(0);
		tabbedPane.setEnabledAt(0, true);
		tabbedPane.setEnabledAt(1, false);
		tabbedPane.setEnabledAt(2, false);
		tabbedPane.setEnabledAt(3, true);
		
		runKmeansMenuItem.setEnabled(true);
		runBisectingKmeansMenuItem.setEnabled(true);
		runHACMenuItem.setEnabled(true);
		runAnotherIterationMenuItem.setEnabled(false);
		runUntilConvergeMenuItem.setEnabled(false);
		splitAgainMenuItem.setEnabled(false);
		joinAgainMenuItem.setEnabled(false);
		exportClustersMenuItem.setEnabled(false);
	}

	private void streamingClustering() {
		Util.centerWithinParent(scDialog_);
		scDialog_.updateToShow();
		scDialog_.setVisible(true);
		if (scDialog_.actionCancelled()) {
			return;
		}

		nDocsLabel.setText("# documents: " + said_.getNumDocuments());
		int nInvDocs = said_.getNumInvalidDocuments();
		nInvalidDocsLabel.setText("# invalid documents: " + nInvDocs);
		nValidWordsLabel.setText("# valid words: "+ said_.wdb().getNumValidWords());
		nDiffWordsLabel.setText("# different words: " + said_.wdb().getNumDifferentWords());

		dicSizeLabel.setText("dictionary size: " + said_.dictionary().size());
		nReplacedWordsLabel.setText("# replaced words: -");

		nDimensionsLabel.setText("# dimensions: -");
		
		systemWordsTable.setModel(new SystemWordsATM(said_));
		systemWordsTable.getRowSorter().toggleSortOrder(0);
		
		showClusteringInfo("");

		tabbedPane.setSelectedIndex(1);
		tabbedPane.setEnabledAt(0, true);
		tabbedPane.setEnabledAt(1, true);
		tabbedPane.setEnabledAt(2, true);
		tabbedPane.setEnabledAt(3, true);
		
		addMoreDocsMenuItem.setEnabled(true);
		runKmeansMenuItem.setEnabled(false);
		runBisectingKmeansMenuItem.setEnabled(false);
		runHACMenuItem.setEnabled(false);
		runAnotherIterationMenuItem.setEnabled(false);
		runUntilConvergeMenuItem.setEnabled(false);
		splitAgainMenuItem.setEnabled(false);
		joinAgainMenuItem.setEnabled(false);
		exportClustersMenuItem.setEnabled(true);
	}
	
	private void addStreamingClusteringDocuments() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (fc.showOpenDialog(MainWindow.this) == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile ();
			if (f.isFile()) {
				said_.addStreamingClusteringDocument(f);
			}
			else {
				try {
					said_.setDocumentsDirectory(f.getCanonicalPath());
					new ProgressDialog(MainWindow.this, said_, 
							ProgressDialog.T_ADD_DOCS, null).setVisible(true);
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}	
			}
			nDocsLabel.setText("# documents: " + said_.getNumDocuments());
			int nInvDocs = said_.getNumInvalidDocuments();
			nInvalidDocsLabel.setText("# invalid documents: " + nInvDocs);
			nValidWordsLabel.setText("# valid words: "+ said_.wdb().getNumValidWords());
			nDiffWordsLabel.setText("# different words: " + said_.wdb().getNumDifferentWords());
			systemWordsTable.setModel(new SystemWordsATM(said_));
			systemWordsTable.getRowSorter().toggleSortOrder(0);
			showClusteringInfo("");
		}
	}

	private void runClusteringAlgorithm(int algorithm) {
		if (algorithm == Classifier.KMEANS) {
			Util.centerWithinParent(kmDialog_);
			kmDialog_.updateToShow();
			kmDialog_.setVisible(true);

			if (kmDialog_.actionCancelled()) {
				return;
			}
			runAnotherIterationMenuItem.setEnabled(false);
			runUntilConvergeMenuItem.setEnabled(false);
			joinAgainMenuItem.setEnabled(false);
			splitAgainMenuItem.setEnabled(false);
			if (said_.classifier().kmeans().itConverged() == false) {
				runAnotherIterationMenuItem.setEnabled(true);
				runUntilConvergeMenuItem.setEnabled(true);
			}
		}
		else if (algorithm == Classifier.BS_KMEANS) {
			Util.centerWithinParent(bskmDialog_);
			bskmDialog_.updateToShow();
			bskmDialog_.setVisible(true);
			if (bskmDialog_.actionCancelled()) {
				return;
			}
			runAnotherIterationMenuItem.setEnabled(false);
			runUntilConvergeMenuItem.setEnabled(false);
			joinAgainMenuItem.setEnabled(false);
			splitAgainMenuItem.setEnabled(false);
			if (said_.classifier().getFinalK() != said_.getNumDocuments()) {
				splitAgainMenuItem.setEnabled(true);
			}
		}
		else if (algorithm == Classifier.HAC) {
			Util.centerWithinParent(hacDialog_);
			hacDialog_.updateToShow();
			hacDialog_.setVisible(true);
			if (hacDialog_.actionCancelled()) {
				return;
			}
			runAnotherIterationMenuItem.setEnabled(false);
			runUntilConvergeMenuItem.setEnabled(false);
			joinAgainMenuItem.setEnabled(false);
			splitAgainMenuItem.setEnabled(false);
			if (said_.classifier().getFinalK() != 1) {
				joinAgainMenuItem.setEnabled(true);
			}
		}
		
		showClusteringInfo("");
		
		tabbedPane.setSelectedIndex(1);
		tabbedPane.setEnabledAt(0, true);
		tabbedPane.setEnabledAt(1, true);
		tabbedPane.setEnabledAt(2, true);
		tabbedPane.setEnabledAt(3, true);
		exportClustersMenuItem.setEnabled(true);
		
		updateHeatMapComboBox();
	}
	
	private void updateHeatMapComboBox() {
		String[] items = new String[said_.classifier().getClusters().size() + 1];
		items[0] = "All Clusters";
		for(int i = 1; i < items.length; i++)
			items[i] = "Cluster " + i;
		this.graphicsTab.getHeatMapComboBox().setModel(new DefaultComboBoxModel(items));
		this.graphicsTab.getHeatMapComboBox().setSelectedIndex(0);
	}

	@SuppressWarnings("unchecked")
	private void showClusteringInfo(String query) {
		DefaultTreeModel model = (DefaultTreeModel) clustersTree.getModel();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		DefaultMutableTreeNode clusterNode = null;
		DefaultMutableTreeNode documentNode = null;
		int k = said_.classifier().getFinalK();
		
		if (query.equals("")) {
			for (int i = 0; i < k; i++) {
				Cluster c = said_.classifier().getClusters().get(i);
				clusterNode = new DefaultMutableTreeNode("Cluster " + (i + 1));
				ArrayList<DataPoint> dpoints = c.getDataPoints();
				Collections.sort(dpoints, new Comparator<DataPoint>() {
					public int compare(DataPoint dp1, DataPoint dp2) {
						return dp1.getLabel().compareTo(dp2.getLabel());
					}
				});
				for (DataPoint dp: dpoints) {
					documentNode = new DefaultMutableTreeNode(dp);
					clusterNode.add(documentNode);
				}
				root.add(clusterNode);
			}
			model.setRoot(root);
		}
		else {
			HashMap<Integer, Float> clusterIndexes = null;
			clusterIndexes = said_.getMatchedClusterIndexes(query);
			if (clusterIndexes != null) {
				Iterator iter = clusterIndexes.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<Integer, Float> e = (Map.Entry<Integer, Float>) iter.next();
					Integer index   = (Integer) e.getKey();
					float score = Round.getRoundedValue(e.getValue(),4);
					Cluster c = said_.classifier().getClusters().get(index);
					clusterNode = new DefaultMutableTreeNode("Cluster " + (index + 1) 
											+ " (score: " + score + ")");
					ArrayList<DataPoint> dpoints = c.getDataPoints();
					Collections.sort(dpoints, new Comparator<DataPoint>() {
						public int compare(DataPoint dp1, DataPoint dp2) {
							return dp1.getLabel().compareTo(dp2.getLabel());
						}
					});
					for (DataPoint dp: dpoints) {
						documentNode = new DefaultMutableTreeNode(dp);
						clusterNode.add(documentNode);
					}
					root.add(clusterNode);
				}
				model.setRoot(root);
			}
			else {
				model.setRoot(null);
			}

		}
				
		clustersTree.setSelectionRow(0);
		if (collapseExpandButton.getText().equals("Collapse All")) {
			for (int i = 0; i < clustersTree.getRowCount(); i++) {
				clustersTree.expandRow(i);
			}
		}
		else {
			for (int i = 0; i < clustersTree.getRowCount(); i++) {
				clustersTree.collapseRow(i);
			}
		}
		
		// fill general stuff related to clustering
		finalKLabel.setText("# created clusters (k): " + k);
		if (said_.classifier().getLastAlgorithmUsed() == Classifier.KMEANS) {
			int nIterations = said_.classifier().kmeans().getIterationsNumber();
			kmeansItersLabel.setText("# kmeans iterations: " + nIterations);
		}
		else {
			kmeansItersLabel.setText("# kmeans iterations: -");
		}
		
		float disp = Round.getRoundedValue(said_.classifier().getAverageDispersion(), 3); 
		avgDispersionLabel.setText("average dispersion: " + disp);
	}
	
	private void showClusterInfo(int clusterIndex) {
		if (clusterIndex == -1) {
			return;
		}
		lblAboutDocumentLabel.setVisible(false);
		filePathLabel.setVisible(false);
		filePathLabel.setVisible(false);
		lblPreviewLabel.setVisible(false);
		previewTextArea.setVisible(false);
		viewDocumentButton.setVisible(false);
		lblDocumentWordsLabel.setVisible(false);
		documentWordsScrollPane.setVisible(false);
		lblAboutClusterLabel.setVisible(true);
		clusterSizeLabel.setVisible(true);
		dispersionLabel.setVisible(true);
		lblClusterWordsLabel.setVisible(true);
		clusterWordsScrollPane.setVisible(true);
		Cluster c = said_.classifier().getClusters().get(clusterIndex);
		clusterSizeLabel.setText("# documents: " + c.size());
		dispersionLabel.setText("dispersion: " 
				+ Round.getRoundedValue(c.getSSE(), 3));
		ClusterWordsATM atm = new ClusterWordsATM(
				said_.getWeightTableOfCluster(clusterIndex));
		clusterWordsTable.setModel(atm);
		clusterWordsTable.getRowSorter().toggleSortOrder(0);
	}
	
	private void showDocumentInfo(int documentIndex) {
		if (documentIndex == -1) {
			return;
		}
		lblAboutClusterLabel.setVisible(false);
		clusterSizeLabel.setVisible(false);
		dispersionLabel.setVisible(false);
		lblClusterWordsLabel.setVisible(false);
		clusterWordsScrollPane.setVisible(false);
		lblAboutDocumentLabel.setVisible(true);
		filePathLabel.setVisible(true);
		lblPreviewLabel.setVisible(true);
		previewTextArea.setVisible(true);
		viewDocumentButton.setVisible(true);
		lblDocumentWordsLabel.setVisible(true);
		documentWordsScrollPane.setVisible(true);
		filePathLabel.setText("loading...");
		Document doc = said_.documents().get(documentIndex);
		filePathLabel.setText("file path: " + doc.getPath());
		previewTextArea.setText(doc.getText());
		DocumentWordsATM atm = new DocumentWordsATM(
				doc, said_.getWeightTableOfDocument(doc));
		documentWordsTable.setModel(atm);
		documentWordsTable.getRowSorter().toggleSortOrder(0);
	}
	
	private void exportClusters(File exportFile) {
		PrintWriter pw;
		try {
			pw = new PrintWriter (new BufferedWriter (new FileWriter (exportFile)));
			int clusterNumber = 1;

			for (Cluster c: said_.classifier().getClusters()) {
				pw.println("Cluster " + clusterNumber + " (" + c.size() + " documents):");
				ArrayList<DataPoint> docsCluster = c.getDataPoints();
				for (int i = 0; i < docsCluster.size(); i++) {
					pw.print("    " + docsCluster.get(i).getLabel() + "\n");
				}
				pw.println();
				clusterNumber++;
			}
			pw.flush ();
			pw.close ();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}  //  @jve:decl-index=0:visual-constraint="-54,-62"
