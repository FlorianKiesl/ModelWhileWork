package ce.modelwhilework.data;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

public class Message extends Card {

	private String senderReciver;
	private boolean sender;
	
	public Message(String title, String senderReciver, boolean sender) {
		super(title, CardType.Message);
		this.senderReciver = senderReciver;
		this.sender = sender;
	}
	
	public String getSenderReciver() { return this.senderReciver; }
	public boolean isSender() { return this.sender; }

	@Override
	protected XmlSerializer getXML(XmlSerializer xmlSerializer, int id) throws Exception {
		
		xmlSerializer.text("    ");
		xmlSerializer.startTag("", "Card");
		xmlSerializer.attribute("", "ID", Integer.toString(id));
		xmlSerializer.attribute("", "type", this.getCardType().toString());
		xmlSerializer.attribute("", "title", this.getTitle());
		xmlSerializer.attribute("", "communicationPartner", this.getSenderReciver());
		xmlSerializer.attribute("", "sender", Boolean.toString(this.isSender()));
		xmlSerializer.endTag("", "Card");
		xmlSerializer.text("\n");
		
		return xmlSerializer;
	}	
}
