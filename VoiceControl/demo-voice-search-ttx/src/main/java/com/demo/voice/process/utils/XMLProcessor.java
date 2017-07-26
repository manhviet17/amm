package com.demo.voice.process.utils;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMLProcessor {
	public Element createXML(String xml){
		xml = "<Root>" + xml + "</Root>";	// Add root tag
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Element rootElement = null;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xml)));
			rootElement = document.getDocumentElement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootElement;
	}
	
	public String getStringFromXMl(String tagName, Element element) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            NodeList subList = list.item(0).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue().trim();
            }
        }
        return null;
    }
}
