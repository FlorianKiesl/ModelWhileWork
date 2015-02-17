package ce.modelwhilework.data;

import org.w3c.dom.*;


public class Message extends Card {

	private String senderReciver;
	private boolean sender;
	
	public Message(String title, String senderReciver, boolean sender) {
		super(title);
		this.senderReciver = senderReciver;
		this.sender = sender;
	}
	
	public String getSenderReciver() { return this.senderReciver; }
	public boolean isSender() { return this.sender; }

	@Override
	protected Element getElementXML(Document dom, int id) {
		
		Element card = super.getElementXML(dom, id);
		card.setAttribute("communicationPartner", this.getSenderReciver());
		card.setAttribute("sender", Boolean.toString(this.isSender()));
		
		return card;
	}

	@Override
	protected CardType getCardType() {
		return CardType.Task;
	}	
}
