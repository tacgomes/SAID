package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The purpose of this class is to keep the information about the collection and 
 * document frequency of all valid words that were readed. The collection frequency 
 * is the numbe of times that a word appeared on the document corpus. Document frequency
 * is the number of documents were the word appeared.
 * 
 * @author	Tiago Gomes
 * @version 1.0
 */
public class WordDataBase {
	/**
	 * Class that saves the collection and document frequency of a word.
	 */
	class WordInfo {
		/** Collection frequency */
		int cf_;
		/** Document frequency */
		int df_;
		/** Constructor for a new word */
		public WordInfo() {
			cf_ = 0;
			df_ = 0;
		}
	}

	/** HashMap containing information about the words. */
	private HashMap<String, WordInfo> words_;

	/** Number of valid words that were readed. */
	private int nValidWords_;


	/** Creates a new instance of this class. */
	public WordDataBase() {
		words_       = new HashMap<String, WordInfo>();
		nValidWords_ = 0;
	}

	/**
	 * Increments the collection frequency of a given word.
	 * 
	 * @param	word	The word to increase the collection frequency.
	 */
	public void incCollectionFreqOf(String word) {
		WordInfo wordInfo = words_.get(word);
		wordInfo.cf_++;
		nValidWords_++;
	}

	/**
	 * Returns the collection frequency for a given word.
	 * 
	 * @param	word	The word to get the collection frequency.
	 * 
	 * @return			Collection frequency of the word.
	 */
	public int getCollectionFreqOf(String word) {
		WordInfo wordInfo = words_.get(word);
		return wordInfo.cf_;
	}

	/**
	 * Increments the document frequency of a given word.
	 * 
	 * @param	word	The word to increase the document frequency.
	 */
	public void incDocumentFreqOf(String word) {
		WordInfo wordInfo = words_.get(word);
		if (wordInfo == null) {
			wordInfo = new WordInfo();
			words_.put(word, wordInfo);
		}
		wordInfo.df_++;
	}

	/**
	 * Returns the document frequency for a given word.
	 * 
	 * @param	word	The word to get the collection frequency.
	 * 
	 * @return			Collection frequency of the word.
	 */
	public int getDocumentFreqOf(String word) {
		WordInfo wordInfo = words_.get(word);
		return wordInfo.df_;
	}

	/**
	 * Returns a list of all words that were readed from the document corpus.
	 * 
	 * @return	List of all words that were readed from the document corpus.
	 */
	public ArrayList<String> getWords() {
		return new ArrayList<String>(words_.keySet());
	}

	/**
	 * Checks if a given word is already exists in the data base.
	 * 
	 * @param	word	The word to check if exists.
	 * @return			True if exists. False otherwise.
	 */
	public boolean constainsWord(String word) {
		return words_.containsKey(word);
	}

	/**
	 * Returns the number of valid words that were readed from the document corpus.
	 * 
	 * @return	Number of valid words that were readed from the document corpus.
	 */
	public int getNumValidWords() {
		return nValidWords_;
	}

	/**
	 * Returns the number of different words that were readed from the document corpus.
	 * 
	 * @return	Number of different words that were readed from the document corpus.
	 */
	public int getNumDifferentWords() {
		return words_.size();
	}

}
