package ce.modelwhilework.data;

import org.w3c.dom.*;

import ce.modelwhilework.data.xml.XmlHelper;

public abstract class Card extends Modus {

	public Card(String title) {
		super(title);
	}
	
	public boolean isMessage() { return this instanceof Message; }
	public boolean isTask() { return this instanceof Task; }

	abstract protected CardType getCardType();
	
	protected Element getElementXML(Document dom, int id) {
		
		Element card = dom.createElement("Card");

		card.setAttribute("pos", Integer.toString(id));
		card.setAttribute("type", this.getCardType().toString());
		card.setAttribute("title", this.getTitle());
		return card;
	}
	
	protected Element getMetasonicElementXML(Document dom, int id, String elemName){
		Element card = dom.createElement(elemName);
		
		card.appendChild(XmlHelper.createXMLTextNode(dom, "UUID", Integer.toString(id)));
		card.appendChild(XmlHelper.createXMLTextNode(dom, "name", this.getTitle()));
		//ToDo: ContextInfo Location (Was ist angle)
		card.appendChild(XmlHelper.createXMLTextNode(dom, "x", "0.0"));	
		card.appendChild(XmlHelper.createXMLTextNode(dom, "y", "0.0"));
		card.appendChild(XmlHelper.createXMLTextNode(dom, "angle", "0.0"));

		if(this.isMessage()){
			Message messageObj = (Message) this;
			card.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			if(messageObj.isSender()){
				card.setAttribute("xsi:type", "XmlSendElement");
				if (elemName.compareToIgnoreCase("endPoint1") != 0 ){
					card.appendChild(this.getMsgXMLElement(dom));
				}
			}
			else{
				card.setAttribute("xsi:type", "XmlReceiveElement");
				if (elemName.compareToIgnoreCase("endPoint1") != 0){
					card.appendChild(this.getMessagesXMLElement(dom));
				}
				else{
					card.appendChild(dom.createElement("messages"));
				}
			}
		}
		
		return card;
	}
	
	private Element getMessagesXMLElement(Document dom){
		Element messagesElem = dom.createElement("messages");
		messagesElem.appendChild(this.getMsgXMLElement(dom));
		return messagesElem;
	}
	
	protected Element getMsgXMLElement(Document dom){
		Message messageObj = (Message) this;
		Element msgElem = dom.createElement("msg");
		msgElem.appendChild(XmlHelper.createXMLTextNode(dom, "message", messageObj.getTitle()));
		String recipient = "";
		String sender = "";
		if (messageObj.isSender()){
			recipient = messageObj.getSenderReceiver();
			sender = "[UserName]";
		}
		else{
			recipient = "[UserName]";
			sender = messageObj.getSenderReceiver();
		}
		msgElem.appendChild(XmlHelper.createXMLTextNode(dom, "recipient", recipient));
		msgElem.appendChild(XmlHelper.createXMLTextNode(dom, "sender", sender));
		return msgElem;
	}
	
	protected String getTypeID() {
		
		return "CARD_" + this.getTitle();
	}
}
