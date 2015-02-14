package ce.modelwhilework.data;

import org.w3c.dom.*;

import ce.modelwhilework.business.XmlHelper;

public abstract class Card extends Modus {

	private CardType type;

	public Card(String title, CardType type) {
		super(title);
		this.type = type;
	}
	
	public boolean isMessage() { return this instanceof Message; }
	public boolean isTask() { return this instanceof Task; }

	protected CardType getCardType() { return this.type; }
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
		card.appendChild(XmlHelper.createXMLTextNode(dom, "x", ""));	
		card.appendChild(XmlHelper.createXMLTextNode(dom, "y", ""));
		card.appendChild(XmlHelper.createXMLTextNode(dom, "angle", ""));

		if(this.isMessage()){
			Message messageObj = (Message) this;
			card.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			if(messageObj.isSender()){
				card.setAttribute("xsi:type", "XmlSendElement");
			}
			else{
				card.setAttribute("xsi:type", "XmlReceiveElement");
			}
			card.appendChild(this.getMsgXMLElement(dom));
		}
		
		return card;
	}
	
	private Element getMsgXMLElement(Document dom){
		Message messageObj = (Message) this;
		Element msgElem = dom.createElement("msg");
		msgElem.appendChild(XmlHelper.createXMLTextNode(dom, "message", messageObj.getTitle()));
		String recipient = "";
		String sender = "";
		if (messageObj.isSender()){
			recipient = messageObj.getSenderReciver();
			sender = "[UserName]";
		}
		else{
			recipient = "[UserName]";
			sender = messageObj.getSenderReciver();
		}
		msgElem.appendChild(XmlHelper.createXMLTextNode(dom, "recipient", recipient));
		msgElem.appendChild(XmlHelper.createXMLTextNode(dom, "sender", sender));
		return msgElem;
	}

}
