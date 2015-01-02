package ce.modelwhilework.data;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Stack;

import org.xmlpull.v1.XmlSerializer;

import android.R.xml;
import android.util.Xml;

public class Process extends Modus implements Comparable<Process>{
	
	private Stack<Card> mainStack;
	private Stack<Card> sideStack;
	
	public Process(String title) {
		super(title);
		
		mainStack = new Stack<Card>();
		sideStack = new Stack<Card>();		
	}
	
	public Process(String title, String mxlFilePath) {
		this(title);
		//todo: handle xml input
	}
	
	public boolean addCard(Card card) { return mainStack.add(card);	}
	
	public boolean putCardAside() {
		
		try {
			sideStack.add(mainStack.pop());			
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean putBackFromAside() {
		
		try { mainStack.add(sideStack.pop()); }
		catch (Exception e) { return false;	}
		return true;
	}
	
	public boolean removeCardFromMainStack() {
		
		try { mainStack.pop();}
		catch (Exception e) { return false;	}
		return true;
	}
	
	public boolean removeCardFromSideStack() {
		
		try { sideStack.pop();}
		catch (Exception e) { return false;	}
		return true;
	}
	
	public boolean isMainStackEmpty() { return mainStack.size() == 0; }
	public boolean isSideStackEmpty() { return sideStack.size() == 0; }
	
	public Card getTopCardMainStack() { 
		if(isMainStackEmpty()) { return null; }
		return mainStack.peek();
	}
	
	public Card getTopCardSideStack() { 
		if(isSideStackEmpty()) { return null; }
		return sideStack.peek();
	}

	@Override
	public int compareTo(Process another) {
		return this.getTitle().compareTo(another.getTitle());
	}
	
	
	protected XmlSerializer writeXML(XmlSerializer xmlSerializer) throws Exception{

		String namespace = "ce.modelwhilework.xml";
		
		
		xmlSerializer.startDocument("UTF-8", true);
		xmlSerializer.startTag(namespace, "Process");
		xmlSerializer.startTag(namespace, "Stack");
		xmlSerializer.attribute(namespace, "currentCardId", "");
		
		Card card;
		Iterator<Card> stackIterator = mainStack.iterator();
		while(stackIterator.hasNext()){
			card = stackIterator.next();
			xmlSerializer = card.writeXMLElem(xmlSerializer, namespace);
		}
		
		xmlSerializer.endTag(namespace, "Stack");
		xmlSerializer.endTag(namespace, "Process");
		
		xmlSerializer.endDocument();
		
		return xmlSerializer;
	}
}
