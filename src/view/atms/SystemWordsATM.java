package view.atms;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import model.SaidModel;
import model.WordDataBase;
import util.Round;


public class SystemWordsATM extends AbstractTableModel {

	private static final long serialVersionUID = 2542075395112766566L;
	private static final String[] colHeads_ = { 
			"Word", "Collection Frequency", "Document Frequency", "Weight (%)"};
	private ArrayList<Object> wt_;
	private WordDataBase wdb_;
	private float totalWeight_;


	@SuppressWarnings("unchecked")
	public SystemWordsATM(SaidModel said) {
		wdb_ = said.wdb();
		wt_  = new ArrayList<Object>(said.getSystemWeightTable().entrySet());
		totalWeight_ = 0;
		for (int i = 0; i < wt_.size(); i++) {
			Map.Entry<String ,Float> e = (Map.Entry<String, Float>) wt_.get(i);
			totalWeight_ = totalWeight_ + e.getValue();
		}
	}

	@SuppressWarnings("unchecked")
	public Object getValueAt(int rowIndex, int columnIndex) {
		Map.Entry<String ,Float> e = (Map.Entry<String, Float>) wt_.get(rowIndex);
		String word = e.getKey();
		if (columnIndex == 0) {
			return word;
		} else if (columnIndex == 1) {
			return wdb_.getCollectionFreqOf(word);
		} else if (columnIndex == 2) {
			return wdb_.getDocumentFreqOf(word);
		} else {
			return Round.getRoundedValue(e.getValue() / totalWeight_ * 100, 3);
		}
	}

	public int getRowCount() {
		return wdb_.getWords().size();
	}


	public int getColumnCount() {
		return colHeads_.length;
	}

	public String getColumnName(int column) {
		return colHeads_[column];
	}
	
}
