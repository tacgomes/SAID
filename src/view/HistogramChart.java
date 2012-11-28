package view;

import java.awt.Color;
import java.awt.GradientPaint;
import java.util.ArrayList;

import model.SaidModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class HistogramChart {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SaidModel said_;
	private String title_;
	private ChartPanel chartPanel;

	public HistogramChart(String title, SaidModel said) {
		this.title_ = title;
		this.said_ = said;

		DefaultCategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        chartPanel = new ChartPanel(chart);
	}
	
	/**
     * Returns a chartPanel.
     * 
     * @return The chartPanel.
     */	
	public ChartPanel getChart() {
		return chartPanel;
	}
	
	/**
     * Returns a dataset.
     * 
     * @return The dataset.
     */	
	private DefaultCategoryDataset createDataset() {

		ArrayList<String> listClusters = new ArrayList<String>();
		
		for ( int i = 0; i < said_.classifier().getClusters().size(); i++ )
			listClusters.add("Cluster " + i);
		
		String cluster = "Number of clusters";
		
		// create the dataset...
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = 0; i < listClusters.size(); i++)
			dataset.addValue(said_.classifier().getClusters().get(i).size(), 
								cluster, listClusters.get(i));
		return dataset;
	}
		
	/**
     * Creates a chart.
     * 
     * @param dataset the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createChart(final CategoryDataset dataset) {
		// create the chart...
	    JFreeChart chart = ChartFactory.createBarChart(
	        title_,							// chart title
	        "Clusters",					// domain axis label
	        "Number of documents",		// range axis label
	        dataset,					// data
	        PlotOrientation.VERTICAL,	// orientation
	        false,						// include legend
	        false,						// tooltips?
	        false						// URLs?
	    );
	
	    // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	
	    // set the background color for the chart...
	    chart.setBackgroundPaint(Color.white);
	
	    // get a reference to the plot for further customization...
	    final CategoryPlot plot = chart.getCategoryPlot();
	    plot.setBackgroundPaint(Color.lightGray);
	    plot.setDomainGridlinePaint(Color.white);
	    plot.setRangeGridlinePaint(Color.white);
	
	    // set the range axis to display integers only...
	    final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	
	    // disable bar outlines...
	    final BarRenderer renderer = (BarRenderer) plot.getRenderer();
	    renderer.setDrawBarOutline(false);
	    
	    // set up gradient paints for series...
	    final GradientPaint gp0 = new GradientPaint(
	        0.0f, 0.0f, Color.blue, 
	        0.0f, 0.0f, Color.lightGray
	    );
	    renderer.setSeriesPaint(0, gp0);
	    
	    final CategoryAxis domainAxis = plot.getDomainAxis();
	    domainAxis.setCategoryLabelPositions(
	        CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
	    );
	    // OPTIONAL CUSTOMISATION COMPLETED.
	    
	    return chart;
	}
}
