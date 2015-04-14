package ce.modelwhilework.data;
import org.w3c.dom.*;

public class FavoriteCard implements Comparable<FavoriteCard> {
	
	private int count;
	private Card card;
	
	public FavoriteCard(Card card, int count){
		this.card = card;
		this.count = count;
	}
	
	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public int compareTo(FavoriteCard another) {
		
		if (another.getCount() - this.getCount() == 0){
			return  this.card.getTitle().compareTo(another.getCard().getTitle());
		}
		return another.getCount() - this.getCount();
		
	}
	
	protected Element getXmlElement(Document dom){
		Element elemCard;
		
		elemCard = dom.createElement("Card");
		elemCard.setAttribute("type", this.card.getCardType().toString());
		elemCard.setAttribute("title", this.card.getTitle());
		elemCard.setAttribute("count", String.valueOf(this.count));
		
		if (this.card.getCardType() == CardType.Message){
			Message msg = (Message) this.card;
			elemCard.setAttribute("communicationPartner", msg.getSenderReceiver());
			elemCard.setAttribute("sender", Boolean.toString(msg.isSender()));
		}
		
		return elemCard;
	}
	
}
