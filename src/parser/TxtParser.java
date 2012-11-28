package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This is class is used to retrive the text of plain text files.
 * 
 * @author	Tiago Gomes
 * @version 1.0
 */
class TxtParser {
	/**
	 * Retrieves the text of a given file.
	 * 
	 * @param	path	Path of the file to retrieve the text.
	 * 
	 * @return	The text of the file.
	 */
	static String getText(String path) {
		StringBuilder text = new StringBuilder();
		BufferedReader input;
		try {
			// use buffering, reading one line at a time
			input =  new BufferedReader(new FileReader(path));
			String line;
			while ((line = input.readLine()) != null) {
				text.append(line + System.getProperty("line.separator"));
			}
			return text.toString();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
