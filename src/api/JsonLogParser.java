package api;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import bean.Log;

public class JsonLogParser {

	public static ArrayList<Log> parse(String filePath) {
		ArrayList<Log> logs = new ArrayList<Log>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

		try {
			FileReader reader = new FileReader(filePath);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

			JSONArray requests= (JSONArray) jsonObject.get("requests");
			Iterator iRequests = requests.iterator();
			while (iRequests.hasNext()) {
				JSONObject innerObj = (JSONObject) iRequests.next();

				Log log = new Log();
				log.startedDateTime = sdf.parse(innerObj.get("startedDateTime").toString());
				log.method = (String) innerObj.get("method");
				log.url = (String) innerObj.get("url");
					

		        // getting phoneNumbers
		        JSONArray headers = (JSONArray) innerObj.get("headers");
		        Iterator iHeaders = headers.iterator();
				while (iHeaders.hasNext()) {
					JSONObject header = (JSONObject) iHeaders.next();
					
					log.addHeader(header.get("key").toString(), header.get("value").toString());
				}
		        
				logs.add(log);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return logs;
	}
}
