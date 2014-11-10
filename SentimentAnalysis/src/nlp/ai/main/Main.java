package nlp.ai.main;

import javax.swing.UIManager;

import nlp.ai.gui.FileChooser;

public class Main {	
	
	public static void main(String[] args) {

FileChooser sfc = new FileChooser();
		
		try {
			// UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			// for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			// {
			// if ("Nimbus".equals(info.getName())) {
			// UIManager.setLookAndFeel(info.getClassName());
			// break;
			// }
			// }
			UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");

		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
			e.printStackTrace();
		}
		
		sfc.setVisible(true);	 
	}
}