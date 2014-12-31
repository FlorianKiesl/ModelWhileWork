package ce.modelwhilework.data;


public abstract class Card extends Modus {

	CardType type;
	
	public Card(String title, CardType type) {
		super(title);
		this.type = type;
	}
	
	public boolean isMessage() { return this.type == CardType.Message; }
	public boolean isTask() { return this.type == CardType.Task; }
}
