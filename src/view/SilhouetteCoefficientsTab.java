package view;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import model.SaidModel;
import classifier.Classifier;

public class SilhouetteCoefficientsTab extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String SILHOUETTE                = "Silhouette";
	private static final String ALL                       = "Show All";
	
	private JPanel propertiesSilhouetteCoefficientsPanel   = null;
	private JPanel displaySilhouetteCoefficientsPanel      = null;
	private JPanel chooseGraphicPanel                      = null;
	private JPanel optionsSilhouetteCoefficientsChartPanel = null;
	
	private JComboBox silhouetteComboBox;
	private JCheckBox traceCheckBox;
	
	private MainWindow mainWindow;
	
	private SaidModel said_;

	public SilhouetteCoefficientsTab(MainWindow mainWindow) {
		super();
		
		this.mainWindow = mainWindow;

		this.setLayout(new BorderLayout());
		this.add(getDisplayGraphicPanel(),       BorderLayout.CENTER);
		this.add(getPropertiesSilhouettePanel(), BorderLayout.EAST);
	}
	
	public void setSaid(SaidModel said) {
		this.said_ = said;
	}
	
	/**
	 * This method initializes displayGrapihcPanel
	 * 
	 * @return JPanel
	 */
	private JPanel getDisplayGraphicPanel() {
		if (displaySilhouetteCoefficientsPanel == null) {
			displaySilhouetteCoefficientsPanel = new JPanel();
			BorderLayout bl = new BorderLayout();
			displaySilhouetteCoefficientsPanel.setLayout(bl);
			
			Border etched = BorderFactory.createEtchedBorder();
			TitledBorder titlePanel = BorderFactory.createTitledBorder(etched, "Silhouette Coefficients:");
			displaySilhouetteCoefficientsPanel.setBorder(titlePanel);
		}
		return displaySilhouetteCoefficientsPanel;
	}
	
	/**
	 * This method initializes propertiesSilhouettePanel
	 * 
	 * @return JPanel
	 */
	private JPanel getPropertiesSilhouettePanel() {
		if (propertiesSilhouetteCoefficientsPanel == null) {
			propertiesSilhouetteCoefficientsPanel = new JPanel();
			propertiesSilhouetteCoefficientsPanel.setLayout(new BoxLayout(propertiesSilhouetteCoefficientsPanel, BoxLayout.Y_AXIS));
			propertiesSilhouetteCoefficientsPanel.add(getChooseGraphicPanel());
			propertiesSilhouetteCoefficientsPanel.add(getSilhouetteCoefficientsChartOptionsPanel());

			Border etched = BorderFactory.createEtchedBorder();
			TitledBorder titlePanel = BorderFactory.createTitledBorder(etched, "Properties:");
			propertiesSilhouetteCoefficientsPanel.setBorder(titlePanel);
		}
		return propertiesSilhouetteCoefficientsPanel;
	}
	
	/**
	 * This method initializes chooseGraphicPanel
	 * 
	 * @return JPanel
	 */
	private JPanel getChooseGraphicPanel() {
		if (chooseGraphicPanel == null) {
			chooseGraphicPanel = new JPanel();
			
			final JCheckBox silhCheck = new JCheckBox();
			final JButton silhButton = new JButton(SILHOUETTE);
			silhButton.setActionCommand(SILHOUETTE);
			silhButton.addActionListener (new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					if(silhCheck.isSelected()) showSilhCoeffsChartDialog();
					else showSilhCoeffsChart();
				}
			});
			
			JButton showAllButton = new JButton(ALL);
			showAllButton.setActionCommand(ALL);
			showAllButton.addActionListener (new ActionListener () {
			    public void actionPerformed(ActionEvent e) {
			    	showSilhCoeffsChartDialog();
			    }
		    });
			
			JLabel typeLabelSilh = new JLabel("Type:");
			JLabel externalLabelSilh = new JLabel("External:");
			
			GroupLayout layout = new GroupLayout(chooseGraphicPanel);
			chooseGraphicPanel.setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
	
			layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(externalLabelSilh)
							.addComponent(silhCheck)
							.addComponent(showAllButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(typeLabelSilh)
							.addComponent(silhButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(layout.createSequentialGroup())));
	
			layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(externalLabelSilh)
							.addComponent(typeLabelSilh)).addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(silhCheck)
							.addComponent(silhButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(showAllButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(layout.createSequentialGroup())));
			
			Border etched = BorderFactory.createEtchedBorder();
			TitledBorder titlePanel = BorderFactory.createTitledBorder(etched, "Choose The Chart Type:");
			chooseGraphicPanel.setBorder(titlePanel);
		}
		return chooseGraphicPanel;
	}

	/**
	 * This method initializes optionsSilhouetteCoefficientsChartPanel
	 * 
	 * @return JPanel
	 */
	private JPanel getSilhouetteCoefficientsChartOptionsPanel() {
		if (optionsSilhouetteCoefficientsChartPanel == null) {
			optionsSilhouetteCoefficientsChartPanel = new JPanel();
			
			JLabel runsLabel   = new JLabel("Runs:");
			traceCheckBox      = new JCheckBox("Trace (Show n lines)");
			silhouetteComboBox = new JComboBox();
			
			silhouetteComboBox.addItem("1");
			silhouetteComboBox.addItem("3");
			silhouetteComboBox.addItem("5");
			silhouetteComboBox.addItem("10");
			
			GroupLayout layout = new GroupLayout(optionsSilhouetteCoefficientsChartPanel);
			optionsSilhouetteCoefficientsChartPanel.setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(runsLabel))
							.addGroup(layout.createSequentialGroup()).addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(silhouetteComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(traceCheckBox)));
	
			layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(runsLabel)
							.addComponent(silhouetteComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(traceCheckBox)
							.addGroup(layout.createSequentialGroup())));
			
			Border etched = BorderFactory.createEtchedBorder();
			TitledBorder titlePanel = BorderFactory.createTitledBorder(etched, "Silhouette Coefficients Options:");
			optionsSilhouetteCoefficientsChartPanel.setBorder(titlePanel);
		}
		return optionsSilhouetteCoefficientsChartPanel;
	}
	
	private void showSilhCoeffsChart() {
		displaySilhouetteCoefficientsPanel.removeAll();
		Classifier classif = new Classifier(said_.getDataPoints());
		ProgressBarSilhouette pbs = new ProgressBarSilhouette(mainWindow,
				classif,
				Integer.valueOf(silhouetteComboBox.getSelectedItem().toString()),
				traceCheckBox.isSelected());
		pbs.setVisible(true);
		displaySilhouetteCoefficientsPanel.add(pbs.getChart());
		displaySilhouetteCoefficientsPanel.updateUI();
	}
	
	/**
	 * This method initializes showSilhCoeffsChartDialog
	 */
	private void showSilhCoeffsChartDialog() {
		JDialog jDialog = new JDialog();
		jDialog.setSize(710, 461);
		jDialog.setTitle("Silhouette Coefficients");
		jDialog.setResizable(true);
		jDialog.setName("silhCoeffsDialog");
		jDialog.setModal(true);
		Classifier classif = new Classifier(said_.getDataPoints());
		ProgressBarSilhouette pbs = new ProgressBarSilhouette(mainWindow,
				classif,
				Integer.valueOf(silhouetteComboBox.getSelectedItem().toString()),
				traceCheckBox.isSelected());
		pbs.setVisible(true);
		jDialog.setContentPane(pbs.getChart());
		centerWithinParent(mainWindow, jDialog);
		jDialog.setVisible(true);
	}
	
	private void centerWithinParent(MainWindow parent, JDialog jDialog) {
		Rectangle r = parent.getBounds();
	    int x = r.x + (r.width  - jDialog.getSize().width)  / 2;
	    int y = r.y + (r.height - jDialog.getSize().height) / 2;
	    jDialog.setLocation(x, y);
	}
}
