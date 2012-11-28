package parser;

import java.util.Scanner;

/**
 * This is class implements an parser that support several different types
 * of files such as plain text files, pdf's, and Microsof and Open Office documents.
 * 
 * @author	Tiago Gomes
 * @version	1.0
 */
public class Parser {

	/** Minimum lenght of a word. */
	private static final int MIN_LENGHT_ = 3;
	
	/** Maximum lenght of a word. */
	private static final int MAX_LENGHT_ = 20;
	
	/** Delimiter characters. */
	private static final String DELIMITER_ = "[^0-9a-zA-ZÀ-ÃÇ-ÊÌ-ÎÒ-ÕÙ-ÛÝà-ãç-êì-îò-õù-ûý]";

	/** Last word readed. */
	private String lastWord;

	/** Java Scanner used to read the text. */
	private Scanner scanner_;


	/**
	 * Creates a Parser instance for a given file.
	 * 
	 * @param	path	Path of the file to read.
	 */
	public Parser(String path) {
		scanner_ = new Scanner(getText(path));
		scanner_.useDelimiter(DELIMITER_);
	}

	/**
	 * Returns the next token from the text.
	 *
	 * @return	The next token.
	 */
	public String next() {
		return lastWord.toLowerCase();
	}

	/**
	 * Checks if there are more tokens to read.
	 *
	 * @return	True if there are more tokens to read. Otherwise false.
	 */
	public boolean hasNext() {
		while (true) {
			if (scanner_.hasNext()) {
				lastWord = scanner_.next();
				if ((lastWord.length() >= MIN_LENGHT_)
				 && (lastWord.length() <= MAX_LENGHT_)
				 && (lastWord.matches(".*([0-9]+).*") == false)
				 && (StopWords.isStopWord(lastWord) == false)) {
						return true;
				}
			}
			else {
				scanner_.close();
				return false;
			}
		}
	}

	/**
	 * Retrieves all text of a given file.
	 * 
	 * @param	path	Path of the file to retrieve the text.
	 * 
	 * @return	A java String with the text.
	 */
	public static String getText(String path) {
		int index = path.lastIndexOf(".");

		if (index > 0) {
			String ext = path.substring(index + 1).toLowerCase();

			if (ext.equals("txt")) {
				return TxtParser.getText(path);
			}
			else if (ext.equals("pdf")) {
				return PdfParser.getText(path);
			}
			else if (ext.equals("doc") || ext.equals("docx")
				  || ext.equals("ppt") || ext.equals("pptx")
				  || ext.equals("xls") || ext.equals("xlsx")) {
						return MsOfficeParser.getText(path);
			}
			else if (ext.equals("odt") || ext.equals("odp") || ext.equals("ods")) {
				return OdfParser.getText(path);
			}
		}

		return null;
	}

	/**
	 * Checks if a given file have a supported file extension.
	 * 
	 * @param	path	Path of the file to check the extension.
	 * 
	 * @return	True if the extension of the file is supported. False otherwise.
	 */
	public static boolean checkExtension(String path) {
		int index = path.lastIndexOf(".");
		if (index > 0) {
			String ext = path.substring(index + 1).toLowerCase();
			if (ext.equals("txt") || ext.equals("pdf")
			 || ext.equals("doc") || ext.equals("docx")
			 || ext.equals("ppt") || ext.equals("pptx")
			 || ext.equals("xls") || ext.equals("xlsx")
			 || ext.equals("odt") || ext.equals("odp") || ext.equals("ods")) {
				return true;
			}
		}
		return false;
	}

}
