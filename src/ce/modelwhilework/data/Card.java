package ce.modelwhilework.data;

import org.xmlpull.v1.XmlSerializer;

public abstract class Card extends Modus {

	private CardType type;

	public Card(String title, CardType type) {
		super(title);
		this.type = type;
	}
	
	public boolean isMessage() { return this.type == CardType.Message; }
	public boolean isTask() { return this.type == CardType.Task; }

	protected CardType getCardType() { return this.type; }
	protected abstract XmlSerializer getXML(XmlSerializer xmlSerializer, int id) throws Exception;
}
