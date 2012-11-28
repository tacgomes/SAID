package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class ChartPanel extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPopupMenu menu;
	private Image img;
	
	public ChartPanel(Image img) {
		this.img = img;
		this.addMouseListener(this);
		initialize();
	}
	
	protected void paintComponent(Graphics g) { 
		Dimension d = getSize();
		g.drawImage(img, 0, 0, d.width, d.height, null);
    }
	
	private void saveToFile(File outputFile) throws IOException {
        BufferedImage chart = (BufferedImage) img;

        // Save graphic.
        ImageIO.write(chart, "png", outputFile);
    } 	
	
	private void initialize() {
		menu = new JPopupMenu();
		JMenuItem item = new JMenuItem("Save As...");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showSaveDialog(ChartPanel.this);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File path = fc.getSelectedFile();
	                File file = new File(path + "/img.png");
	                try {
						saveToFile(file);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	            } else {
	                // error
	            }
			}
		});
		menu.add(item);
	}

	public void mouseClicked(MouseEvent ev) {
		if (ev.isPopupTrigger())
			menu.show(ev.getComponent(), ev.getX(), ev.getY());
	}

	@Override
	public void mouseEntered(MouseEvent ev) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent ev) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent ev) {
		if (ev.isPopupTrigger()) {
			menu.show(ev.getComponent(), ev.getX(), ev.getY());
		}
	}

	public void mouseReleased(MouseEvent ev) {
		if (ev.isPopupTrigger())
			menu.show(ev.getComponent(), ev.getX(), ev.getY());
	}
}
