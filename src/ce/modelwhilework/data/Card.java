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

	protected XmlSerializer writeXMLElem(XmlSerializer xmlSerializer, String namespace) throws Exception{
		xmlSerializer.startTag(namespace, "Card");
		xmlSerializer.attribute(namespace, "type", this.type.toString());
		this.writeXMLConcreteElem(xmlSerializer, namespace);
		xmlSerializer.endTag(namespace, "Card");
		return xmlSerializer;
	}
	
	protected abstract XmlSerializer writeXMLConcreteElem(XmlSerializer xmlSerializer, String namespace) throws Exception;
}
