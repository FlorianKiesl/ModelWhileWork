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
	
	public void setSenderReciver(String sr) { this.senderReciver = sr; }
	public String getSenderReiciver() { return this.senderReciver; }
	
	public void setSender(boolean sender) { this.sender = sender; }
	public boolean isSender() { return this.sender; }

	@Override
	protected Element getElementXML(Document dom, int id) {
		
		Element card = super.getElementXML(dom, id);
		card.setAttribute("communicationPartner", this.getSenderReiciver());
		card.setAttribute("sender", Boolean.toString(this.isSender()));
		
		return card;
	}

	@Override
	protected CardType getCardType() {
		return CardType.Task;
	}	
}
