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

public class GraphicsTab extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String HISTOGRAM      = "Histogram";
	private static final String HEATMAP        = "Heat Map";
	// private static final String DENDOGRAM      = "Dendogram"; 
	private static final String ALL            = "Show All";
	
	private JPanel heatMapChartOptionsPanel    = null;
	private JPanel chooseGraphicPanel          = null;
	private JPanel propertiesGraphicsPanel     = null;
	// private JPanel dendrogramChartOptionsPanel = null;
	private JPanel displayGraphicsPanel        = null;
	
	private JComboBox heatMapComboBox;
	private JCheckBox heatMapCheckBox;
	
	private MainWindow mainWindow;
	
	private SaidModel said_;

	/**
	 * Creates a new GraphicsTab instance.
	 *
	 * @param mainWindow	mainWindow
	 */
	public GraphicsTab(MainWindow mainWindow) {
		super();
		
		this.mainWindow = mainWindow;
		
		this.setLayout(new BorderLayout());
		this.add(getViewGraphicsPanel()      , BorderLayout.CENTER);
		this.add(getPropertiesGraphicsPanel(), BorderLayout.EAST);
	}
	
	// TODO
	public void setSaid(SaidModel said) {
		this.said_ = said;
	}
	
	/**
	 * @return heatMapComboBox
	 */
	public JComboBox getHeatMapComboBox() {
		return heatMapComboBox;
	}
	
	/**
	 * This method initializes propertiesGraphicsPanel
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPropertiesGraphicsPanel() {
		if (propertiesGraphicsPanel == null) {
			propertiesGraphicsPanel = new JPanel();
			propertiesGraphicsPanel.setLayout(new BoxLayout(propertiesGraphicsPanel, BoxLayout.Y_AXIS));
			propertiesGraphicsPanel.add(getChooseGraphicPanel());
			propertiesGraphicsPanel.add(getHeatMapChartOptionsPanel());
			// propertiesGraphicsPanel.add(getDendogramChartOptionsPanel());
			
			Border etched = BorderFactory.createEtchedBorder();
			TitledBorder titlePanel = BorderFactory.createTitledBorder(etched, "Properties:");
			propertiesGraphicsPanel.setBorder(titlePanel);
		}
		return propertiesGraphicsPanel;
	}
	
	/**
	 * This method initializes chooseGraphicPanel
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getChooseGraphicPanel() {
		if (chooseGraphicPanel == null) {
			chooseGraphicPanel = new JPanel();
				
			JLabel typeLabel                   = new JLabel("Type:");
			JLabel externalLabel               = new JLabel("External:");
			
			// JButton dendrogramButton           = new JButton(DENDOGRAM);
			JButton histogramButton            = new JButton(HISTOGRAM);
			JButton heatMapButton              = new JButton(HEATMAP);
			JButton showAllButton              = new JButton(ALL);
			
			final JCheckBox histogramCheckBox  = new JCheckBox();
			heatMapCheckBox                    = new JCheckBox();
			// final JCheckBox dendrogramCheckBox = new JCheckBox();
			
			// dendrogramButton.setActionCommand(DENDOGRAM);
			histogramButton.setActionCommand(HISTOGRAM);
			heatMapButton.setActionCommand(HEATMAP);
			showAllButton.setActionCommand(ALL);
	
			histogramButton.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			    	if (histogramCheckBox.isSelected()) showHistChartDialog();
			    	else showHistChart();
			    }
			});
			
			heatMapButton.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			    	if (heatMapCheckBox.isSelected()) showHeatMapChartDialog();
			    	else showHeatMapChart();
			    }
			});
			/*
			dendrogramButton.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			    	if (dendrogramCheckBox.isSelected()) showDendrogramChartDialog();
			    	else showDendrogramChart();
			    }
			});*/
			
			showAllButton.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			    	showHistChartDialog();
			    	showHeatMapChartDialog();
			    	// showDendrogramChartDialog();
			    }
		    });

			GroupLayout layout = new GroupLayout(chooseGraphicPanel);
			chooseGraphicPanel.setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(externalLabel)
							.addComponent(histogramCheckBox)
							.addComponent(heatMapCheckBox)
							.addGroup(layout.createSequentialGroup())
							// .addComponent(dendrogramCheckBox)
							.addComponent(showAllButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(typeLabel)
							.addComponent(histogramButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(heatMapButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(layout.createSequentialGroup())
							// .addComponent(dendrogramButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(layout.createSequentialGroup())));

			layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(externalLabel)
							.addComponent(typeLabel)).addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(histogramCheckBox)
							.addComponent(histogramButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(heatMapCheckBox)
							.addComponent(heatMapButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							// .addComponent(dendrogramCheckBox)
							// .addComponent(dendrogramButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(
							.addGroup(layout.createSequentialGroup())
							.addGroup(layout.createSequentialGroup())).addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(showAllButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(layout.createSequentialGroup())));
			
			Border etched = BorderFactory.createEtchedBorder();
			TitledBorder titlePanel = BorderFactory.createTitledBorder(etched, "Choose the type of graphic:");
			chooseGraphicPanel.setBorder(titlePanel);
		}
		return chooseGraphicPanel;
	}
	
	/**
	 * This method initializes heatMapChartOptionsPanel
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getHeatMapChartOptionsPanel() {
		if (heatMapChartOptionsPanel == null) {
			heatMapChartOptionsPanel = new JPanel();
			
			final JLabel chooseClusterLabel = new JLabel("Cluster:");
			heatMapComboBox                 = new JComboBox();
			
			GroupLayout layout = new GroupLayout(heatMapChartOptionsPanel);
			heatMapChartOptionsPanel.setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(chooseClusterLabel)).addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(heatMapComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

			layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(chooseClusterLabel)
						.addComponent(heatMapComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
			
			heatMapComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
			    	if(heatMapCheckBox.isSelected()) showHeatMapChartDialog();
					else showHeatMapChart();
			    }
			});
			
			Border etched = BorderFactory.createEtchedBorder();
			TitledBorder titlePanel = BorderFactory.createTitledBorder(etched, "Heat Map Options:");
			heatMapChartOptionsPanel.setBorder(titlePanel);
		}
		return heatMapChartOptionsPanel;
	}
	
	
	/**
	 * This method initializes dendogramChartOptionsPanel
	 * 	
	 * @return javax.swing.JPanel	
	 */
	/*
	private JPanel getDendogramChartOptionsPanel() {
		if (dendrogramChartOptionsPanel == null) {
			dendrogramChartOptionsPanel = new JPanel();
			
			final JLabel chooseMethodLabel    = new JLabel("Method:");
			final JComboBox dendogramComboBox = new JComboBox();
			
			dendogramComboBox.addItem("SINGLE_LINKAGE");
			dendogramComboBox.addItem("COMPLETE_LINKAGE");
			dendogramComboBox.addItem("GROUP_AVERAGE");
			dendogramComboBox.addItem("CENTROID_PROXIMITY");
			dendogramComboBox.addItem("WARDS_METHOD");
			
			GroupLayout layout = new GroupLayout(dendrogramChartOptionsPanel);
			dendrogramChartOptionsPanel.setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(chooseMethodLabel)).addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(dendogramComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

			layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(chooseMethodLabel)
						.addComponent(dendogramComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
			
			Border etched = BorderFactory.createEtchedBorder();
			TitledBorder titlePanel = BorderFactory.createTitledBorder(etched, "Dendrogram Options:");
			dendrogramChartOptionsPanel.setBorder(titlePanel);
		}
		return dendrogramChartOptionsPanel;
	}*/
	
	/**
	 * This method initializes displayGraphicsPanel
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getViewGraphicsPanel() {
		if (displayGraphicsPanel == null) {
			displayGraphicsPanel = new JPanel();
			BorderLayout bl = new BorderLayout();
			displayGraphicsPanel.setLayout(bl);

			Border etched = BorderFactory.createEtchedBorder();
			TitledBorder titlePanel = BorderFactory.createTitledBorder(etched, "Display:");
			displayGraphicsPanel.setBorder(titlePanel);
		}
		return displayGraphicsPanel;
	}
	
	/**
	 * This method initializes showHistChartDialog
	 */
	private void showHistChartDialog() {
		JDialog jDialog = new JDialog();
		jDialog.setSize(710, 461);
		jDialog.setTitle("Cluster's Histogram");
		jDialog.setName("histogramDialog");
		HistogramChart hist = new HistogramChart("Cluster's Histogram", said_);
		jDialog.setContentPane(hist.getChart());
		centerWithinParent(mainWindow, jDialog);
		jDialog.setResizable(true);
		jDialog.setVisible(true);
	}
	
	// XXX
	private void showHistChart() {
		displayGraphicsPanel.removeAll();
		HistogramChart hist = new HistogramChart("Cluster's Histogram", said_);
		displayGraphicsPanel.add(hist.getChart());
		displayGraphicsPanel.updateUI();
	}
	
	/**
	 * This method initializes showHeatMapChartDialog
	 */
	private void showHeatMapChartDialog() {
		JDialog jDialog = new JDialog();
		jDialog.setSize(710, 461);
		jDialog.setTitle("Cluster's HeatMap");
		jDialog.setName("heatMapDialog");
		HeatMapChart heatMap = new HeatMapChart(said_, heatMapComboBox.getSelectedIndex() - 1, 
				jDialog.getWidth(), jDialog.getHeight());
		jDialog.setContentPane(heatMap.getChart());
		centerWithinParent(mainWindow, jDialog);
		jDialog.setResizable(true);
		jDialog.setVisible(true);
	}
	
	private void showHeatMapChart() {
		displayGraphicsPanel.removeAll();
		HeatMapChart heatMap = new HeatMapChart(said_, heatMapComboBox.getSelectedIndex() - 1, 
				displayGraphicsPanel.getWidth(), displayGraphicsPanel.getHeight());
		displayGraphicsPanel.add(heatMap.getChart());
		displayGraphicsPanel.updateUI();
	}
	
	/**
	private void showDendrogramChartDialog() {
		JDialog jDialog = new JDialog();
		jDialog.setSize(710, 461);
		String[][] values = said_.classifier().hac().getDendrogramData();
		
		Dendrogram dend = new Dendrogram(values);
		dend.setTitle("Dendrograma");
		dend.setBackground(Color.white);
		dend.setScaleYAxis(2);
		dend.setSpacingTitle(20);
		jDialog.setContentPane(dend);
		centerWithinParent(mainWindow, jDialog);
		jDialog.setResizable(true);
		jDialog.setVisible(true);
	}
	
	private void showDendrogramChart() {
		displayGraphicsPanel.removeAll();
		String[][] values = said_.classifier().hac().getDendrogramData();
		
		Dendrogram dend = new Dendrogram(values);
		dend.setTitle("Dendrograma");
		dend.setBackground(Color.white);
		dend.setScaleYAxis(1);
		dend.setSpacingTitle(20);
		displayGraphicsPanel.add(dend);
		displayGraphicsPanel.updateUI();
	}*/
	
	private void centerWithinParent(MainWindow parent, JDialog jDialog) {
		Rectangle r = parent.getBounds();
	    int x = r.x + (r.width  - jDialog.getSize().width)  / 2;
	    int y = r.y + (r.height - jDialog.getSize().height) / 2;
	    jDialog.setLocation(x, y);
	}
}
