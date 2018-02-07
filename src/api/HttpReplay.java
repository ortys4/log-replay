package api;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import bean.Log;

public class HttpReplay {

	public void start(ArrayList<Log> logs) {
		Date lastQueryDate = null;
		Long wait = new Long(0);
		
		for (Log log : logs) {
			try {
				if(lastQueryDate != null) {
					wait = log.startedDateTime.getTime() - lastQueryDate.getTime();
				}
				lastQueryDate = log.startedDateTime;

				Thread.sleep(wait);
				
				HttpUriRequest request;
				if(log.method.toUpperCase().equals("GET")) {
					request = makeGetRequest(log);
				} else if(log.method.toUpperCase().equals("POST")) {
					request = makePostRequest(log);
				} else {
					throw new Exception("Method " + log.method + " is not supported");
				}
				
				Thread sender = new Thread(new HttpSender(request));
				sender.start();
				
			} catch(Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
	public HttpUriRequest makeGetRequest(Log log) throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(log.url);

		// add request header
		for(Map.Entry<String, String> header : log.headers.entrySet()) {
			request.addHeader(header.getKey(), header.getValue());
		}
		
		return request;
	}

	// HTTP POST request
	public HttpUriRequest makePostRequest(Log log) throws ClientProtocolException, IOException {

		HttpPost request = new HttpPost(log.url);

		for(Map.Entry<String, String> header : log.headers.entrySet()) {
			request.addHeader(header.getKey(), header.getValue());
		}

//		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
//		urlParameters.add(new BasicNameValuePair("sn", "C02G8416DRJM"));
		request.setEntity(new StringEntity(log.body));
		
		
		
		return request;

	}

}
