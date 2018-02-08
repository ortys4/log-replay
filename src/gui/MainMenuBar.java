package gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainMenuBar extends JMenuBar {

	
	public MainMenuBar() {
		JMenu fileItem = new JMenu("Fichier");
		
		JMenuItem openfileItem = new JMenuItem("Ouvrir");
		fileItem.add(openfileItem);
		
		add(fileItem);
	}

}
