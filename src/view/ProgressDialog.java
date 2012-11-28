package view;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import model.SaidModel;
import parser.Parser;


public class ProgressDialog extends JDialog implements PropertyChangeListener  {

	private static final long serialVersionUID = 6292234659282143119L;
	private JPanel progressContentPanel = null;  //  @jve:decl-index=0:visual-constraint="365,10"
	private JLabel actionLabel = null;
	private JProgressBar progressBar = null;

	private SaidModel said_;
	private int 	  taskId_;
	private int 	  args_[];
	
	// time determinate tasks
	static final int T_NEW_ANALYSIS         = 1;
	static final int T_STREAM_CLUST         = 2;
	static final int T_ADD_DOCS             = 3;
    // time indeterminate tasks
	static final int T_RUN_KMEANS           = 11;
	static final int T_RUN_N_KMEANS_ITERS   = 12;
	static final int T_RUN_1_KMEANS_ITER    = 13;
	static final int T_RUN_KMEANS_CONVERGE  = 14;
	static final int T_RUN_BSKMEANS         = 15;
	static final int T_RUN_1_BSKMEANS_ITER  = 16;
	static final int T_RUN_HAC              = 17;	
	static final int T_RUN_1_HAC_ITER       = 18;
	static final int T_NORMALIZE_SIZE       = 19;


	public ProgressDialog(JFrame parent, SaidModel said, int taskId, int[] args) {
		super(parent);
		initialize();
		Util.centerWithinParent(this);
		said_ = said;
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		switch (taskId) {
		case 1:
			NewAnalysisTask naTask = new NewAnalysisTask();
			naTask.addPropertyChangeListener(this);
			naTask.execute();
			break;
		case 2:
			args_ = args;
			StreamingClusteringTask scTask = new StreamingClusteringTask();
			scTask.addPropertyChangeListener(this);
			scTask.execute();
			break;
		case 3:
			AddMoreDocumentsTask amdTask = new AddMoreDocumentsTask();
			amdTask.addPropertyChangeListener(this);
			amdTask.execute();
			break;
		default:
			taskId_ = taskId;
			args_ = args;
			IndeterminateTimeTask tiTask = new IndeterminateTimeTask();
			tiTask.execute();
		}
	}

	/**
	 * This method initializes this.
	 */
	private void initialize() {
		this.setSize(363, 82);
		this.setTitle("Progress");
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setModal(true);
		this.setName("progressDialog");
		this.setContentPane(getProgressContentPanel());
	}

	/**
	 * This method initializes progressContentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getProgressContentPanel() {
		if (progressContentPanel == null) {
			actionLabel = new JLabel();
			actionLabel.setBounds(new Rectangle(10, 0, 340, 20));
			actionLabel.setText("");
			progressContentPanel = new JPanel();
			progressContentPanel.setLayout(null);
			progressContentPanel.setSize(new Dimension(418, 253));
			progressContentPanel.add(actionLabel, null);
			progressContentPanel.add(getProgressBar(), null);
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
			progressBar.setBounds(new Rectangle(10, 21, 340, 23));
			progressBar.setStringPainted(true);
		}
		return progressBar;
	}

	private class NewAnalysisTask extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {
			System.out.println("\n-------------------------------------------------------------------");
			System.out.println(" STARTING A NEW ANALYSIS\n " + Calendar.getInstance().getTime());
			System.out.println("-------------------------------------------------------------------");
			
			//System.err.println("NEW ANALYSIS");
			//System.err.println("mem stats before starting:  " + MemoryInformation.getStatistics());
			//Calendar cal = Calendar.getInstance();
			//long time = cal.getTimeInMillis();

			progressBar.setIndeterminate(true);
			progressBar.setStringPainted(false);

			actionLabel.setText("preparing system to a new analysis");
			ArrayList<File> fileList = said_.getSortedFileList();

			if (said_.isDictionaryEnabled()) {
				actionLabel.setText("reading the dictionary");
				said_.readDictionary();
			}

			progressBar.setIndeterminate(false);
			progressBar.setStringPainted(true);
			setProgress(0); // Initialize progress property

			System.out.println("Reading documents:");
			actionLabel.setText("reading documents");
			int nFiles = fileList.size();
			for (int i = 0; i < nFiles; i++) {
				if (fileList.get(i).isFile() && Parser.checkExtension(fileList.get(i).getPath())) {
					//System.err.println(i + ":  " + fileList.get(i).getName());
					said_.addDocument(fileList.get(i));
				}
				int progress = (int) ((float)(i+1)/nFiles * 100);
				setProgress(progress);
			}
			System.out.println();

			progressBar.setIndeterminate(true);
			progressBar.setStringPainted(false);

			if (said_.isDictionaryEnabled() && said_.dictionary().isDecayEnabled()) {
				actionLabel.setText("replacing dead words");
				said_.dictionary().replaceDeadWords(said_.getSystemWeightTable());
			}

			actionLabel.setText("calculating vectors");
			said_.calculateVectors();

			if (said_.isLatentSemanticIndexingEnabled()) {
				actionLabel.setText("applying latent semantic index");
				said_.applyLatentSemanticIndexing();
			}

			//System.err.println("mem stats after parsing:   " + MemoryInformation.getStatistics());
			//cal = Calendar.getInstance();
			//System.err.println("documents parsing time:    " +
			//		Round.getRoundedValue((float) ((cal.getTimeInMillis() - time)/1000.0), 1) + "s\n");

			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			setCursor(null); // turn off the wait cursor
			ProgressDialog.this.dispose();
		}

	}

	private class StreamingClusteringTask extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {
			System.out.println("\n-------------------------------------------------------------------");
			System.out.println(" STARTING STREAMING CLUSTERING\n " + Calendar.getInstance().getTime());
			System.out.println("-------------------------------------------------------------------");

			//System.err.println(" STREAMING CLUSTERING");
			//System.err.println("mem stats before starting:  " + MemoryInformation.getStatistics());
			//Calendar cal = Calendar.getInstance();
			//long time = cal.getTimeInMillis();

			progressBar.setIndeterminate(true);
			progressBar.setStringPainted(false);

			actionLabel.setText("preparing system to perform streaming clustering");
			ArrayList<File> fileList = said_.getSortedFileList();

			if (said_.isDictionaryEnabled()) {
				actionLabel.setText("reading the dictionary");
				said_.readDictionary();
			}

			progressBar.setIndeterminate(false);
			progressBar.setStringPainted(true);
			setProgress(0); // Initialize progress property

			System.out.println("Reading documents:");
			actionLabel.setText("reading documents");
			int nFiles = fileList.size();
			for (int i = 0; i < nFiles; i++) {
				if (fileList.get(i).isFile() && Parser.checkExtension(fileList.get(i).getPath())) {
					said_.addDocument(fileList.get(i));
				}
				int progress = (int) ((float)(i+1)/nFiles * 100);
				setProgress(progress);
			}
			System.out.println();

			progressBar.setIndeterminate(true);
			progressBar.setStringPainted(false);

			actionLabel.setText("calculating vectors");
			said_.calculateVectors();

			said_.classifier().streamingClustering().start(args_[0]);
			
			//System.err.println("mem stats after running streaming clustering:   " 
			//		+ MemoryInformation.getStatistics());
			//cal = Calendar.getInstance();
			//System.err.println("streaming clustering time:    " +
			//		Round.getRoundedValue((float)((cal.getTimeInMillis() - time)/1000.0), 1) + "s\n");

			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			setCursor(null); // turn off the wait cursor
			ProgressDialog.this.dispose();
		}

	}
	
	private class AddMoreDocumentsTask extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {
			System.out.println("ADDING MORE DOCUMENTS");

			progressBar.setIndeterminate(true);
			progressBar.setStringPainted(false);

			actionLabel.setText("preparing system to add more documents");
			ArrayList<File> fileList = said_.getSortedFileList();
			
			progressBar.setIndeterminate(false);
			progressBar.setStringPainted(true);
			setProgress(0); // Initialize progress property

			System.out.println("Reading documents:");
			actionLabel.setText("reading documents");
			int nFiles = fileList.size();
			for (int i = 0; i < nFiles; i++) {
				if (fileList.get(i).isFile() && Parser.checkExtension(fileList.get(i).getPath())) {
					said_.addStreamingClusteringDocument(fileList.get(i));
				}
				int progress = (int) ((float)(i+1)/nFiles * 100);
				setProgress(progress);
			}
			System.out.println();
			

			progressBar.setIndeterminate(true);
			progressBar.setStringPainted(false);
			
			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			setCursor(null); // turn off the wait cursor
			ProgressDialog.this.dispose();
		}

	}
	
	private class IndeterminateTimeTask extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {
			progressBar.setIndeterminate(true);
			progressBar.setStringPainted(false);
			
			switch (taskId_) {
			case T_RUN_KMEANS:
				said_.classifier().kmeans().run(args_[0]);
				break;

			case T_RUN_N_KMEANS_ITERS:
				said_.classifier().kmeans().run(args_[0], args_[1]);
				break;
				
			case T_RUN_1_KMEANS_ITER:
				said_.classifier().kmeans().runOneIteration();
				break;

			case T_RUN_KMEANS_CONVERGE:
				said_.classifier().kmeans().runUntilConverges();
				break;

			case T_RUN_BSKMEANS:
				said_.classifier().bisectingKmeans().run(args_[0], args_[1], args_[2], args_[3]);
				break;
				
			case T_RUN_1_BSKMEANS_ITER:
				said_.classifier().bisectingKmeans().runOneBisectingKmeansIteration();
				break;

			case T_RUN_HAC:
				said_.classifier().hac().run(args_[0], args_[1]);
				break;

			case T_RUN_1_HAC_ITER:
				said_.classifier().hac().runOneIteration();
				break;

			case T_NORMALIZE_SIZE:
				said_.classifier().normalizeClusterSize(args_[0], args_[1]);
				break;
			}

			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			setCursor(null); // turn off the wait cursor
			ProgressDialog.this.dispose();
		}

	}

	/**
	 * Invoked when task's progress property changes.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			progressBar.setValue((Integer) evt.getNewValue());
		}
	}

}  //  @jve:decl-index=0:visual-constraint="14,9"
