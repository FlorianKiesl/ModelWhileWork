package ce.modelwhilework.data;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

public class Task extends Card{

	public Task(String title) {
		super(title, CardType.Task);
	}

	@Override
	protected XmlSerializer writeXMLConcreteElem(XmlSerializer xmlSerializer, String namespace) throws IllegalArgumentException, IllegalStateException, IOException {
		
		return xmlSerializer;
	}

}
