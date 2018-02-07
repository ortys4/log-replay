package api;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

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
			XmlLogParser.validateXMLSchema("files/template.xsd", filePath);
			
			File fXmlFile = new File(filePath);
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

					NodeList nBody;
					nBody = eRequest.getElementsByTagName("content-raw");
					if(nBody.getLength() > 0) {
						log.body = nBody.item(0).getTextContent();	
					} else {
						nBody = eRequest.getElementsByTagName("content-list");
						for (int iContent = 0; iContent < nBody.getLength(); iContent++) {
							Node nContent = nBody.item(iContent);
							if (nContent.getNodeType() == Node.ELEMENT_NODE) {
								Element eContent = (Element) nContent;
								log.addContent(eContent.getElementsByTagName("key").item(0).getTextContent(), eContent.getElementsByTagName("value").item(0).getTextContent());
							}
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

	public static void validateXMLSchema(String xsdPath, String xmlPath) throws Exception{
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = factory.newSchema(new File(xsdPath));
		Validator validator = schema.newValidator();
		validator.validate(new StreamSource(new File(xmlPath)));	
	}

}