package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import parser.Parser;
import util.WeightTableComparator;

/**
 * This class implements the usage of a dictionary to specify what words will compose
 * the vocabulary of terms.
 * 
 * It also supports aging of words: words that takes too many time units
 * (a time unit occur whenever a word is readed from a file) to reappear
 * again, are replaced by the words that have the highest weights in the system.
 * 
 * @author	Tiago Gomes
 * @version	1.0
 */ 
public class Dictionary {
	
	/** List of words that compose the dictionary. */
	private ArrayList<String> words_;
	
	/** Ages of the words contained on the dictionary. */
	private ArrayList<Integer> ages_;
	
	/** Time unit that a word can be on dictionary without reappear. */
	private int maxAge_;
	
	/** Indicates if word aging is or is not enabled. */
	private boolean wordAging_;
	
	/** List of words and corresponding weights. */
	private ArrayList<Object> sysWeightTbl_;
	
	/** Number of replaced words. */ 
	private int nReplacedWords_;

	
	/**
	 * Returns a new instance of this class.
	 *
	 * @param	dictionaryPath	Path of the file that have the dictionary words.			 
	 */
	public Dictionary(String dictionaryPath) {
		words_     = new ArrayList<String>();
		wordAging_ = false;
		Parser parser = new Parser(dictionaryPath);
		while (parser.hasNext()) {
			words_.add(parser.next());
		}
	}
	
	/**
	 * Returns a new instance of this class with word aging support.
	 *
	 * @param	dictionaryPath	Path of the file that have the dictionary words.
	 * @param	maxAge			Maximum age that a word can be on the dictionary
	 * 							without reappear. 	 
	 */
	public Dictionary(String dictionaryPath, int maxAge) {
		words_      = new ArrayList<String>();
		ages_       = new ArrayList<Integer>();
		wordAging_  = true;
		maxAge_     = maxAge;
		Parser parser = new Parser(dictionaryPath);
		while (parser.hasNext()) {	
			words_.add(parser.next());
			ages_.add(1);
		}
	}	
	
	/**
	 * Increments the ages of all words by one time unit 
	 * except the age of the given word which will be zero.
	 *
	 * @param	word	The word which age will be zero.	 
	 */
	public void updateAges(String word) {
		for (int i = 0; i < words_.size(); i++) {
			if (word.equalsIgnoreCase(words_.get(i))) {
				ages_.set(i, 0);
			}
			else {
				ages_.set(i, ages_.get(i) + 1);
			}
		}
	}
	
	/**
	 * Replaces the words which age is greater than the maximum age.
	 * 
	 * @param	sysWeightTbl	A list of all words in the system and their 
	 * 							corresponding weights. 
	 */
	public void replaceDeadWords(HashMap<String, Float> sysWeightTbl) {
		System.out.println("Updating dictionary:");
		sysWeightTbl_   = new ArrayList<Object>(sysWeightTbl.entrySet());
		Collections.sort(sysWeightTbl_, new WeightTableComparator());
		nReplacedWords_ = 0;
		for (int i = 0; i < words_.size(); i++) {
			if (ages_.get(i) > maxAge_) {
				String bestWord = getBestWord();
				System.out.println("        replacing \"" + words_.get(i) 
						+ "\" by \"" + bestWord + "\"");
				words_.remove(i);
				ages_.remove(i);
				words_.add(bestWord);
				ages_.add(1);
				nReplacedWords_++;
			}
		}
		System.out.println("        number of replaced words: " + nReplacedWords_ + "\n");
	}

	/**
	 * Returns the word with the highest weight on the system
	 * and that isn't already on the dictionary.
	 *	
	 * @return	The best word to be added to the dictionary.
	 */
	@SuppressWarnings("unchecked")
	private String getBestWord() {
		String bestCandidate = null;
		int startPos = 0;
		while (true) {
		    Map.Entry<String ,Float> e = (Map.Entry<String, Float>)sysWeightTbl_.get(startPos);
			bestCandidate = e.getKey();
		    if (words_.contains(bestCandidate) == false) {
				break;
		    }
			startPos++;
		}
		return bestCandidate;
	}
	
	/**
	 * Returns a list of words of the dictionary
	 *
	 * @return	List of words of the dictionary.
	 */
	public ArrayList<String> getWords() {
		return words_;
	}
	
	/**
	 * Returns whether word aging is enabled or not.
	 * 
	 * @return	True if word aging is enabled. False otherwise.
	 */
	public boolean isDecayEnabled() {
		return wordAging_;
	}
	
	/**
	 * Returns the number of words that were replaced.
	 *
	 * @return	Number of words that were replaced.
	 * 
	 */
	public int getNumReplacedWords() {
		return nReplacedWords_;
	}
	
	/**
	 * Returns the number of words on the dictionary.
	 * 
	 * @return	Size of the dictionary.
	 */
	public int size() {
		return words_.size();
	}
	
}
