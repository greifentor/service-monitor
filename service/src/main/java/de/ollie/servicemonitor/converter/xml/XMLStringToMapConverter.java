package de.ollie.servicemonitor.converter.xml;

import static de.ollie.servicemonitor.util.Check.ensure;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author ollie (23.11.2021) from blueprints.
 */
@Named
public class XMLStringToMapConverter {

	public Map<String, Object> convert(String xmlString)
			throws ParserConfigurationException, SAXException, IOException {
		ensure(xmlString != null, "XML string cannot be null.");
		if (xmlString.isEmpty()) {
			return new HashMap<>();
		}
		Document xml = convertStringToDocument(xmlString);
		return xml.hasChildNodes() ? getMapFromNode(xml.getFirstChild()) : new HashMap<>();
	}

	private Map<String, Object> getMapFromNode(Node node) {
		HashMap<String, Object> values = new HashMap<String, Object>();
		NodeList childs = node.getChildNodes();
		for (int i = 0; i < childs.getLength(); i++) {
			Node child = childs.item(i);
			if (!child.hasChildNodes()) {
				values.put(child.getNodeName(), child.getTextContent());
			} else if (child.getChildNodes().getLength() == 1) {
				values.put(child.getNodeName(), child.getFirstChild().getTextContent());
			} else {
				values.put(child.getNodeName(), getMapFromNode(child));
			}
		}
		return values;
	}

	private Document convertStringToDocument(String xmlStr)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
			return doc;
	}

}