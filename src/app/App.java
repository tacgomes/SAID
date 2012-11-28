package app;

import model.SaidModel;
import view.MainWindow;

public class App {
	public static void main(String[] args) {
		SaidModel model = new SaidModel("log.txt");
		MainWindow view = new MainWindow(model);
		view.setVisible(true);
		model.flushPrintStream();
	}
}
