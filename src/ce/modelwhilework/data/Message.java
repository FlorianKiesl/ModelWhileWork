package ce.modelwhilework.data;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

public class Message extends Card {

	public Message(String title) {
		super(title, CardType.Message);
	}

	@Override
	protected XmlSerializer writeXMLConcreteElem(XmlSerializer xmlSerializer, String namespace) throws IllegalArgumentException, IllegalStateException, IOException {
		
		
		return xmlSerializer;
	}
	
}
