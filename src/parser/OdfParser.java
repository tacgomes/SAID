package parser;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;

/**
 * This is class is used to retrieve the text of Open Document Format files.
 * 
 * @author	Tiago Gomes
 * @version 1.0
 */
class OdfParser {
	/** Holds the text of the file. */
	private static StringBuilder TextBuffer;

	/**
	 * Process text elements recursively.
	 * 
	 * @param	o	The text element to process.
	 */
	@SuppressWarnings("unchecked")
	private static void processElement(Object o) {

		if (o instanceof Element) {

			Element e = (Element) o;
			String elementName = e.getQualifiedName();

			if (elementName.startsWith("text")) {

				if (elementName.equals("text:tab")) {
					TextBuffer.append("\t");
				} else if (elementName.equals("text:s")) {
					TextBuffer.append(" ");
				} else {
					List children = e.getContent();
					Iterator iterator = children.iterator();

					while (iterator.hasNext()) {

						Object child = iterator.next();
						//If Child is a Text Node, then append the text
						if (child instanceof Text) {
							Text t = (Text) child;
							TextBuffer.append(t.getValue());
						} else {
							processElement(child); // Recursively process the child element
						}
					}
				}
				if (elementName.equals("text:p")) {
					TextBuffer.append("\n");
				}
			}
			else {
				List non_text_list = e.getContent();
				Iterator it = non_text_list.iterator();
				while (it.hasNext()) {
					Object non_text_child = it.next();
					processElement(non_text_child);
				}
			}
		}
	}

	/**
	 * Retrieves the text of a given file.
	 * 
	 * @param	path	Path of the file to retrieve the text.
	 * @return	The text of the file.
	 */
	@SuppressWarnings("unchecked")
	static String getText(String path) {
		TextBuffer = new StringBuilder();
		ZipFile	zipFile;
		try {
			//Unzip the openOffice Document
			zipFile = new ZipFile(path);
			Enumeration entries = zipFile.entries();
			ZipEntry entry;
			while (entries.hasMoreElements()) {
				entry = (ZipEntry) entries.nextElement();
				if (entry.getName().equals("content.xml")) {
					TextBuffer = new StringBuilder();
					SAXBuilder sax = new SAXBuilder();
					Document doc = sax.build(zipFile.getInputStream(entry));
					Element rootElement = doc.getRootElement();
					processElement(rootElement);
					break;
				}
			}
			return TextBuffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		return null;
	}

}
