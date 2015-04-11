package ce.modelwhilework.data;

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
	
}
