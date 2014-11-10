package nlp.ai.main;

import javax.swing.UIManager;

import nlp.ai.gui.FileChooser;

public class Main {

	public static void main(String[] args) {


		try {
			UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		FileChooser sfc = new FileChooser();
		sfc.setVisible(true);
	}
}