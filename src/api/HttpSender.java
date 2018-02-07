package api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;

public class HttpSender implements Runnable {

	private HttpUriRequest request;
	private ArrayList<String> trace;
	private String output;

	/**
	 * 
	 * @param request
	 */
	public HttpSender(HttpUriRequest request) {
		this.request = request;
		this.trace = new ArrayList<String>();
	}

	@Override
	public void run(){	
		try {
			HttpClient httpclient = HttpClients.createDefault();
			Long senDate = new Date().getTime();

			trace.add("Send at [" + senDate + "] ");
			HttpResponse response = httpclient.execute(request);

			Long during = new Date().getTime() - senDate;
			trace.add("During [" + during + "ms] ");
			trace.add("Code [" + response.getStatusLine().getStatusCode() + "] ");
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			setOutput(rd.lines().collect(Collectors.joining()));
			

		} catch (Exception e) {
			trace.add("Exception "+e.getMessage());
		}
		trace.add("Method [" + request.getMethod() + "] ");
		trace.add("URL [" + request.getURI() + "]");
		

		for (String line : trace) {
			System.out.print(line);
		}
		System.out.println();
		System.out.println(this.output);
		
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}


}
