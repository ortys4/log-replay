package api;

import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;

public class HttpSender implements Runnable {

	private HttpUriRequest request;
	private ArrayList<String> output;

	public HttpSender(HttpUriRequest request) {
		this.request = request;
		this.output = new ArrayList<String>();
	}

	@Override
	public void run(){	
		try {
			HttpClient httpclient = HttpClients.createDefault();
			Long senDate = new Date().getTime();

			output.add("Send at [" + senDate + "] ");
			HttpResponse response = httpclient.execute(request);

			Long during = new Date().getTime() - senDate;
			output.add("During [" + during + "ms] ");
			output.add("Response [" + response.getStatusLine().getStatusCode() + "] ");

		} catch (Exception e) {
			output.add("Exception "+e.getMessage());
		}
		output.add("Method [" + request.getMethod() + "] ");
		output.add("URL [" + request.getURI() + "]");

		for (String line : output) {
			System.out.print(line);
		}
		System.out.println();
	}


}
