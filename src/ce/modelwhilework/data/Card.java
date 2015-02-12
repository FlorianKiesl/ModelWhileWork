package ce.modelwhilework.data;

import org.w3c.dom.*;

public abstract class Card extends Modus {

	private CardType type;

	public Card(String title, CardType type) {
		super(title);
		this.type = type;
	}
	
	public boolean isMessage() { return this.type == CardType.Message; }
	public boolean isTask() { return this.type == CardType.Task; }

	protected CardType getCardType() { return this.type; }
	protected Element getElementXML(Document dom, int id) {
		
		Element card = dom.createElement("Card");

		card.setAttribute("pos", Integer.toString(id));
		card.setAttribute("type", this.getCardType().toString());
		card.setAttribute("title", this.getTitle());
		return card;
	}
}
