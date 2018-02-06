package bean;

import java.util.Date;
import java.util.HashMap;

public class Log {

	public Date startedDateTime;
	public String method;
	public String url;
	public HashMap<String, String> headers;
	
	public Log() {
		headers = new HashMap<String, String>();
	}
	
	
	public String toString() {
		return "Date :" + startedDateTime.toString() + " Method : " + method + " Url : " + url;
	}
	
	public void addHeader(String key, String value) {
		headers.put(key, value);
	}
}
