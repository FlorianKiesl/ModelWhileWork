package ce.modelwhilework.data.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class XmlHelper {
	
	public static Element createXMLTextNode(Document dom, String name, String value){
		Element elem;
		Text textNode;
		elem = dom.createElement(name);
		textNode = dom.createTextNode(value);
		elem.appendChild(textNode);
		return elem;
	}
}
