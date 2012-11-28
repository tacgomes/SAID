package parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;

/**
 * This is class is used to retrieve the text of Microsoft Office files.	
 *
 * @author	Tiago Gomes
 * @version 1.0
 */
class MsOfficeParser {
	/**
	 * Retrives the text of a given file.
	 * 
	 * @param	path	Path of the file to retrive the text.
	 * @return	The text of the file.
	 */
	static String getText(String path) {
		FileInputStream fs;
		try {
			fs = new FileInputStream(path);
			return ExtractorFactory.createExtractor(fs).getText();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OpenXML4JException e) {
			e.printStackTrace();
		} catch (XmlException e) {
			e.printStackTrace();
		}
		return null;
	}
}
