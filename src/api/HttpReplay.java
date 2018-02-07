package api;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import bean.Log;

public class HttpReplay implements Observer {

	public void start(ArrayList<Log> logs) {
		Date lastQueryDate = null;
		Long wait = new Long(0);

		this.sortLogByDate(logs);
		
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
				}  else if(log.method.toUpperCase().equals("DELETE")) {
					request = makeDeleteRequest(log);
				} else if(log.method.toUpperCase().equals("POST")) {
					request = makePostRequest(log);
				} else if(log.method.toUpperCase().equals("PUT")) {
					request = makePutRequest(log);
				} else {
					throw new Exception("Method " + log.method + " is not supported");
				}

				HttpSender sender = new HttpSender(request);
				this.observe(sender);

				Thread tSender = new Thread(sender);
				tSender.start();

			} catch(Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	public HttpUriRequest makeGetRequest(Log log) throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(log.url);
		return this.fillGetRequest(request, log);
	}
	
	public HttpUriRequest makeDeleteRequest(Log log) throws ClientProtocolException, IOException {
		HttpDelete request = new HttpDelete(log.url);
		return this.fillGetRequest(request, log);
	}

	public HttpUriRequest makePostRequest(Log log) throws ClientProtocolException, IOException {

		HttpPost request = new HttpPost(log.url);
		return this.fillPostRequest(request, log);
	}
	
	public HttpUriRequest makePutRequest(Log log) throws ClientProtocolException, IOException {
		HttpPut request = new HttpPut(log.url);
		return this.fillPostRequest(request, log);
	}
	
	private HttpUriRequest fillGetRequest(HttpRequestBase request, Log log) {
		for(Map.Entry<String, String> header : log.headers.entrySet()) {
			request.addHeader(header.getKey(), header.getValue());
		}
		return request;
	}
	
	
	private HttpUriRequest fillPostRequest(HttpEntityEnclosingRequestBase request, Log log) throws ClientProtocolException, IOException {
		this.fillGetRequest(request, log);
		 
		if(log.contents.size() > 0) {
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> content : log.contents.entrySet()) {
				urlParameters.add(new BasicNameValuePair(content.getKey() ,content.getValue()));
			}
			request.setEntity(new UrlEncodedFormEntity(urlParameters));
		} else {
			request.setEntity(new StringEntity(log.body));	
		}
		return request;
	}

	public void observe(Observable o) {
		o.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		HttpSender sender = (HttpSender) o;
		System.out.println(sender.getTrace().toString());
//		System.out.println(sender.getOutput());
	}
	
	/**
	 * Sort logs by startedDateTime
	 * @param logs
	 */
	public void sortLogByDate(ArrayList<Log> logs) {
		Collections.sort(logs, new Comparator<Log>() {
			@Override public int compare(Log l1, Log l2) {
				return l1.startedDateTime.compareTo(l2.startedDateTime); // Ascending
			}
		});
	}
}
