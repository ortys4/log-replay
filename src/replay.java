import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import api.HttpReplay;
import api.XmlLogParser;
import bean.Log;

public class replay {

	public static void main(String[] args) throws IOException {
		String basePath = new File("").getAbsolutePath();
		String filename = basePath + "/files/log-google-10.xml";
		if(args.length == 1) {
			//Case absolute path (C:/ ou D:/)
			if(filename.substring(1,1) != ":") {
				filename = basePath + args[0];
			} else {
				filename = args[0];
			}
		}
		HttpReplay tester = new HttpReplay();

		//ArrayList<Log> logs = JsonLogParser.parse("/files/requests.json");
		ArrayList<Log> logs =  XmlLogParser.parse(filename);
		tester.start(logs);
	}

}
