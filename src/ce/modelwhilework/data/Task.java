package ce.modelwhilework.data;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

public class Task extends Card{

	public Task(String title) {
		super(title, CardType.Task);
	}

	@Override
	protected XmlSerializer getXML(XmlSerializer xmlSerializer, int id) throws Exception {
		xmlSerializer.text("    ");
		xmlSerializer.startTag("", "Card");
		xmlSerializer.attribute("", "ID", Integer.toString(id));
		xmlSerializer.attribute("", "type", this.getCardType().toString());
		xmlSerializer.attribute("", "title", this.getTitle());
		xmlSerializer.endTag("", "Card");
		xmlSerializer.text("\n");
		
		return xmlSerializer;
	}

}
