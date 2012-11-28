package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Util {

	static final int M_CLUSTERS_ZERO     = 1;
	static final int M_CLUSTERS_TOO_MANY = 2;
	static final int M_TRIALS_ZERO       = 3;
	static final int M_NORMALIZE_ERROR   = 4;
	static final int M_DISPERSION_ZERO   = 5;
	static final int M_SIZE_ZERO         = 6;
	static final int M_FILE_OVERWRITE    = 7;
	static final int M_CLEAR_LOG         = 8;
	
	
	/**
	 * Shows an JOptionPane with an appropriate message based on a
	 * given parameter.
	 * 
	 * @param	parentComponent	The component that call this function.
	 * @param 	msgId				Identifies which message to show.				
	 * 
	 * @return 	The value returned by the option pane if returned anything.
	 *         	Else returns 0. 
	 */
	static int showOptionPane(Component parentComponent, int msgId) {
		String message  = "";
		String title    = "Error";
		int messageType = JOptionPane.ERROR_MESSAGE;
		
		switch(msgId) {
		case M_CLUSTERS_ZERO:
			message = "The start number of clusters (K) cannot be 0."; 
			break;
		
		case M_CLUSTERS_TOO_MANY:
			message = "The number of clusters (K) cannot be\n"
				    + "greater than the number of documents."; 
			break;
			
		case M_TRIALS_ZERO:
			message = "The number of trials cannot be 0."; 
			break;
			
		case M_NORMALIZE_ERROR:
			message = "It was not possible to normalize all clusters.\n"
			        + "Probably the minimum cluster size is " 
			 		+ "to close of the maximum cluster size."; 
			title   = "Warning";
			break;
			
		case M_DISPERSION_ZERO:
			message = "The dispersion field cannot be 0."; 
			break;
			
		case M_SIZE_ZERO:
			message = "The size field cannot be 0."; 
			break;
			
		case M_FILE_OVERWRITE:
			message = "Overwrite existing file ?";
			title   = "Confirm Overwrite"; 
			return JOptionPane.showConfirmDialog (
						parentComponent,
						message, 
						title,
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
			
		case M_CLEAR_LOG:
			message = "Are you sure that you want to clear the log?"; 
			title   = "Confirm";
			return JOptionPane.showConfirmDialog (
						parentComponent,
						message, 
						title,
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				
		}
				
		JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
		return 0;
	}
	
	/**
	 * Centers the window on the screen.
	 * 
	 * @param	window	The window to center.
	 */
	static void centerWindowOnScreen(JFrame window) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth()  - window.getWidth())  / 2);
		int y = (int) ((dimension.getHeight() - window.getHeight()) / 2);
		window.setLocation(x, y);
	}

	/**
	 * Centers the dialog within its parent.
	 * 
	 * @param	dialog	The dialog to center.
	 */
	static void centerWithinParent(JDialog dialog) {		
		Rectangle r = dialog.getParent().getBounds();
		int x = (int) (r.getX() + (r.getWidth()  - dialog.getWidth())  / 2);
		int y = (int) (r.getY() + (r.getHeight() - dialog.getHeight()) / 2);
		dialog.setLocation(x, y);
		
		// ensures that the dialog is all visible
		r = dialog.getBounds();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		if (x + r.getWidth() > dimension.getWidth()) {
			x = x - (int) (x + r.getWidth() - dimension.getWidth());
		}
		if (y + r.getHeight() > dimension.getHeight()) {
			y = y - (int) (y + r.getHeight() - dimension.getHeight());
		}
		dialog.setLocation(x, y);
	}

}
