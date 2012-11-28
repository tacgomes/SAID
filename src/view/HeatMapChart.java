package view;

import java.awt.Color;

import java.awt.Font;

import javax.swing.JPanel;

import model.SaidModel;

import org.tc33.jheatchart.HeatChart;

import classifier.Classifier;

public class HeatMapChart extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int itemSelected_;
	private double[][] matrix_;
	private SaidModel said_;
	private int width_;
	private int height_;
	private ChartPanel chartPanel;
	
	public HeatMapChart(SaidModel said, int itemSelected, int width, int height) {
		super();
		this.said_         = said;
		this.itemSelected_ = itemSelected;
		this.width_        = width;
		this.height_       = height;
		HeatChart chart    = createChart();
		chartPanel         = new ChartPanel(chart.getChartImage());
	}
	
	public ChartPanel getChart() {
		return chartPanel;
	}
	
	private HeatChart createChart() {
		if(itemSelected_ == -1) matrix_ = said_.classifier().getDoubleProximityMatrix();
		else matrix_ = said_.classifier().getClusters().get(itemSelected_).getDoubleProximityMatrix();
		
		HeatChart chart = new HeatChart(matrix_);
		if(itemSelected_ == -1) chart.setTitle("All Clusters");
		else chart.setTitle("Cluster " + itemSelected_);
		chart.setTitleColour(Color.BLUE);
		chart.setTitleFont(new Font("MONOSPACED", Font.BOLD, 24));
		
		String[] dp;
		if(itemSelected_ == -1) dp = said_.classifier().getLabelList(); 
		else dp = said_.classifier().getClusters().get(itemSelected_).getLabelList();
		
		// Customize Chart
		int cellHeight = height_ / matrix_.length;
		int cellWidth = width_ / matrix_.length;
		chart.setCellHeight(cellHeight);
		chart.setCellWidth(cellWidth);
		chart.setXValues(dp);
		chart.setYValues(dp);
		chart.setChartMargin(5);
		if(Classifier.getProximityMeasure().toString().equals("CosSim")) {
			chart.setHighValueColour(Color.BLACK);
			chart.setLowValueColour(Color.WHITE);
		} else {
			chart.setHighValueColour(Color.WHITE);
			chart.setLowValueColour(Color.BLACK);
		}
		
		return chart;
	}
}

