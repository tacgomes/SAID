package view;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import classifier.Classifier;


public class ProgressBarSilhouette extends JDialog implements PropertyChangeListener  {
	
	private static final long serialVersionUID = 6292234659282143119L;
	private Classifier classif_;
	private int value_;
	private boolean flag_;
	private JPanel progressContentPanel;
	private JLabel actionLabel;
	private JProgressBar progressBar;
	private Task task;
	public JFreeChart chart;
	
	public ProgressBarSilhouette(JFrame parent, Classifier classif, int value, boolean flag) {
		super();
		
		this.classif_ = classif;
		this.value_ = value;
		this.flag_ = flag;
		
		initialize();
		centerWithinParent(parent);
		runTask();
	}
	
	public ChartPanel getChart() {
		ChartPanel chartPanel = new ChartPanel(chart);
		return chartPanel;
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(365, 88);
		this.setTitle("Progress");
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.setModal(true);
		this.setName("progressDialog");
		this.setContentPane(getProgressContentPanel());
	}
	
	private void centerWithinParent(JFrame parent) {
		Rectangle r = parent.getBounds();
	    int x = r.x + (r.width  - this.getSize().width)  / 2;
	    int y = r.y + (r.height - this.getSize().height) / 2;
	    this.setLocation(x, y);
	}

	/**
	 * This method initializes progressContentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getProgressContentPanel() {
		if (progressContentPanel == null) {
			actionLabel = new JLabel();
			actionLabel.setBounds(new Rectangle(12, 5, 340, 23));
			actionLabel.setText("");
			progressContentPanel = new JPanel();
			progressContentPanel.setLayout(null);
			progressContentPanel.setOpaque(true);
			progressContentPanel.setSize(new Dimension(418, 253));
			progressContentPanel.add(getProgressBar(), null);
			progressContentPanel.add(actionLabel, null);
		}
		return progressContentPanel;
	}

	/**
	 * This method initializes progressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getProgressBar() {
		if (progressBar == null) {
			progressBar = new JProgressBar();
			progressBar.setBounds(new Rectangle(13, 30, 338, 23));
			progressBar.setStringPainted(true);
		}
		return progressBar;
	}

	class Task extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		
		public Void doInBackground() {
			XYSeriesCollection dataset = new XYSeriesCollection();
			float silhCoefsAll[] = new float[classif_.getNumDataPoints() - 2];
			
			setProgress(0);
			progressBar.setIndeterminate(false);
			for (int i = 0; i < value_; i++) {
				actionLabel.setText(
						"Iteration " + i + " - Create the coefficients...");
				float silhCoefs[] = classif_.getSilhouetteCoefficients();
				actionLabel.setText(
						"Iteration " + i + " - Create the dataset to build the graphic...");
				XYSeries series = new XYSeries("");
				for (int k = 0; k < silhCoefs.length; k++) {
					silhCoefsAll[k] = silhCoefsAll[k] + silhCoefs[k];
					series.add(k + 2, silhCoefs[k]);
				}
				dataset.addSeries(series);
				int progress = (int) (((float)(i+1)/value_) * 100);
				setProgress(progress);
			}
			
			if(flag_ == false) {
				dataset = new XYSeriesCollection();
				XYSeries serie = new XYSeries("");
				actionLabel.setText(
					"Calculate the average of the coefficients for each cluster...");
				setProgress(0);
				progressBar.setIndeterminate(false);
				for (int i = 0; i < silhCoefsAll.length; i ++) {
					serie.add(i + 2, silhCoefsAll[i] / value_);
					int progress = (int) (((float)(i+1)/silhCoefsAll.length) * 100);
					setProgress(progress);
				}
				dataset.addSeries(serie);
			}
			
			chart = ChartFactory.createXYLineChart(
					"",
					"Number of Clusters (K)",
					"Silhuette Coefficients",
					dataset,
					PlotOrientation.VERTICAL,
					false,
					false,
					false
			);
		    return null; 
	    }

		
		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			setCursor(null); // turn off the wait cursor
		    ProgressBarSilhouette.this.dispose();
		}
	}
	 
	/**
	  * Invoked when task's progress property changes.
	  */
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
	        progressBar.setValue(progress);
	    }
	}

	private void runTask() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		task = new Task();
		task.addPropertyChangeListener(this);
		task.execute();
	}
}
	