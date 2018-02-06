package api;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import bean.Log;


public class XmlLogParser {

	public static ArrayList<Log> parse(String filePath) {
		ArrayList<Log> logs = new ArrayList<Log>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		
		try {
			String basePath = new File("").getAbsolutePath();
			File fXmlFile = new File(basePath + filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList nRequests = doc.getElementsByTagName("request");
			
			for (int iRequest = 0; iRequest < nRequests.getLength(); iRequest++) {
				Log log = new Log();
				Node nRequest = nRequests.item(iRequest);
				if (nRequest.getNodeType() == Node.ELEMENT_NODE) {
					Element eRequest = (Element) nRequest;
					log.url = eRequest. getElementsByTagName("url").item(0).getTextContent();
					log.method = eRequest.getElementsByTagName("method").item(0).getTextContent();
					log.startedDateTime = sdf.parse(eRequest.getElementsByTagName("sendDateTime").item(0).getTextContent());

					NodeList nHeaders = eRequest.getElementsByTagName("header");
					for (int iHeader = 0; iHeader < nHeaders.getLength(); iHeader++) {
						Node nHeader = nHeaders.item(iHeader);
						if (nHeader.getNodeType() == Node.ELEMENT_NODE) {
							Element eHeader = (Element) nHeader;
							log.addHeader(eHeader.getElementsByTagName("key").item(0).getTextContent(), eHeader.getElementsByTagName("value").item(0).getTextContent());
						}
					}
					
					logs.add(log);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logs;
	}

}