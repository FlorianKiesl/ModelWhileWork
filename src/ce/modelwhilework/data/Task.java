package ce.modelwhilework.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Task extends Card{

	public Task(String title) {
		super(title, CardType.Task);
	}
	
	@Override
	protected Element getElementXML(Document dom, int id) {
		
		return super.getElementXML(dom, id);
	}
	
}
