import java.io.IOException;
import java.util.ArrayList;

import api.HttpReplay;
import api.XmlLogParser;
import bean.Log;

public class replay {

	public static void main(String[] args) throws IOException {
		String filename = "files/log-google-10.xml";
		if(args.length == 1) {
			filename = args[0];
		}
		HttpReplay tester = new HttpReplay();

		ArrayList<Log> logs =  XmlLogParser.parse(filename);
		tester.start(logs);
	}

}
