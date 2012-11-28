package parser;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 * This is class is used to retrieve the text of PDF files.
 * 
 * @author	Tiago Gomes
 * @version 1.0
 */
class PdfParser {
	/**
	 * Retrieves the text of a given file.
	 * 
	 * @param	path	Path of the file to retrieve the text.
	 * @return	The text of the file.
	 */
	static String getText(String path) {
		PDDocument pdfDocument;
		try {
			pdfDocument = PDDocument.load(path);
			PDFTextStripper stripper = new PDFTextStripper();
			String pdfText = new String(stripper.getText(pdfDocument));
			pdfDocument.close();
			return pdfText;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
