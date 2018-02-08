package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

	private JPanel conteneur = new JPanel();
	
	public MainFrame() {
		setTitle("Replay log software");
		setSize(400,400);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		setJMenuBar(new MainMenuBar());
		
		conteneur.setLayout(new BorderLayout());
		conteneur.add(new LogTree(), BorderLayout.WEST);
		conteneur.add(new LogDetailPanel(), BorderLayout.EAST);
		
		setContentPane(conteneur);
		pack();
	}
}
