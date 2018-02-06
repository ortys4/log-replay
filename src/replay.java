import java.io.IOException;
import java.util.ArrayList;

import api.HttpReplay;
import api.JsonLogParser;
import api.XmlLogParser;
import bean.Log;

public class replay {

	public static void main(String[] args) throws IOException {
		
		HttpReplay tester = new HttpReplay();
		
		//ArrayList<Log> logs = JsonLogParser.parse("/files/requests.json");
		ArrayList<Log> logs =  XmlLogParser.parse("/files/log_100.xml");
		tester.start(logs);
	}

}
