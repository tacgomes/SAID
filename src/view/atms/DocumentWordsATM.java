package view.atms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import model.Document;
import util.Round;


public class DocumentWordsATM extends AbstractTableModel  {

	private static final long serialVersionUID = 275691013310974620L;
	private static final String[] colHeads_ = { "Word", "Term Frequency", "Weight (%)" };
	private ArrayList<Object> wt_;
	private Document doc_;
	private float totalWeight_;


	public DocumentWordsATM(Document doc, HashMap<String, Float> wt) {
		doc_ = doc;
		wt_ = new ArrayList<Object>(wt.entrySet());
		totalWeight_ = 0;
		for (float weight:wt.values()) {
			totalWeight_ = totalWeight_ + weight;
		}
	}

	@SuppressWarnings("unchecked")
	public Object getValueAt(int rowIndex, int columnIndex) {
		Map.Entry<String ,Float> e = (Map.Entry<String, Float>) wt_.get(rowIndex);
		String word = e.getKey();
		if (columnIndex == 0) {
			return word;
		} else if (columnIndex == 1) {
			return doc_.getTermFrequencyOf(word);
		} else {
			return Round.getRoundedValue(e.getValue() / totalWeight_ * 100, 2);
		}
	}

	public int getRowCount() {
		return doc_.getWords().size();
	}

	public int getColumnCount() {
		return colHeads_.length;
	}

	public String getColumnName(int column) {
		return colHeads_[column];
	}

}
