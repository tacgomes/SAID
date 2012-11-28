package view.atms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import util.Round;


public class ClusterWordsATM extends AbstractTableModel {

	private static final long serialVersionUID = 7447372756478143762L;
	private static final String[] colHeads_ = { "Word", "Weight (%)" };
	private ArrayList<Object> wt_;
	private float totalWeight_;


	public ClusterWordsATM(HashMap<String, Float> wt) {
		wt_ = new ArrayList<Object>(wt.entrySet());
		totalWeight_ = 0;
		for (float weight: wt.values()) {
			totalWeight_ = totalWeight_ + weight;
		}
	}

	@SuppressWarnings("unchecked")
	public Object getValueAt(int rowIndex, int columnIndex) {
		Map.Entry<String ,Float> e = (Map.Entry<String, Float>) wt_.get(rowIndex);
		if (columnIndex == 0) {
			return e.getKey();
		} else {
			return Round.getRoundedValue(e.getValue() / totalWeight_ * 100, 2);
		}
	}

	public int getRowCount() {
		return wt_.size();
	}

	public int getColumnCount() {
		return colHeads_.length;
	}

	public String getColumnName(int column) {
		return colHeads_[column];
	}

}
