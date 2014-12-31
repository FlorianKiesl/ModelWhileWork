package ce.modelwhilework.data;

import java.util.Stack;

public class Process extends Modus{

	private Stack<Card>  mainStack, sideStack;
	
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
}
