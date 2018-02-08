import java.io.IOException;
import java.lang.annotation.Repeatable;
import java.util.ArrayList;

import api.HttpReplay;
import api.XmlLogParser;
import bean.Log;
import gui.MainFrame;

public class Replay {

//	private HttpReplay tester;
//	private ArrayList<Log> logs;
//	private MainFrame gui;
	
//	public Replay() {
//		tester = new HttpReplay();
//		gui = new MainFrame();
//	}
//	
//	public void start() {
//		gui.setVisible(true);
//	}
	
	
	public static void main(String[] args) throws IOException {
		String filename = "files/log-google-10.xml";
		if(args.length == 1) {
			filename = args[0];
		}
		
//		Replay replay = new Replay();
//		replay.start();
		
		HttpReplay tester = new HttpReplay();

		ArrayList<Log> logs =  XmlLogParser.parse(filename);
		tester.start(logs);
		
	}

}
