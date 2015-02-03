package ce.modelwhilework.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Xml;
import ce.modelwhilework.data.contextinfo.*;;

public class Process extends Modus implements Comparable<Process>{
	
	private Stack<Card> mainStack;
	private Stack<Card> sideStack;
	

	public Process(String title) {
		super(title);
		
		mainStack = new Stack<Card>();
		sideStack = new Stack<Card>();	
		
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
	
	public boolean loadXML(File filePath) {
		
		try {  
			File file = new File(filePath, getTitle() + ".mwyw");
			FileReader reader = new FileReader(file);
		    BufferedReader content = new BufferedReader(reader);
		    
		    String line;
		    line = content.readLine();
		    
		    while(line != null) {
		    	
		    	//todo: xml auswerten und in aktuellen process schreiben
		    	
		    	line = content.readLine();
		    }		     
		    reader.close();  
		}  
		catch(Exception e) {
		     e.printStackTrace();
		     return false;
		}
		
		return true;
	}
	
	public boolean storeXML(File filePath) {
				
		try {
			File file = new File(filePath, getTitle() + ".mwyw");
			if(file.exists()) {
				if(!file.delete())
					return false;
			}
			
			FileWriter writer = new FileWriter(file ,true);    
	        
	        XmlSerializer xmlSerializer = Xml.newSerializer();
	        xmlSerializer.setOutput(writer);
	        
			Iterator<Card> stackIterator;
			int id = 0;
			
			xmlSerializer.startDocument("UTF-8", true);
			xmlSerializer.text("\n");
			xmlSerializer.startTag("", "Process");
			xmlSerializer.text("\n");
			
			//store main stack
			xmlSerializer.startTag("", "Stack");
			xmlSerializer.attribute("", "ID", "Main");
			xmlSerializer.text("\n");
			
			stackIterator = mainStack.iterator();
			while(stackIterator.hasNext()){
				xmlSerializer = stackIterator.next().getXML(xmlSerializer, id);
				id++;
			}
			xmlSerializer.endTag("", "Stack");
			
			//store side stack
			xmlSerializer.startTag("", "Stack");
			xmlSerializer.attribute("", "ID", "Side");
			xmlSerializer.text("\n");
			
			id = 0;
			stackIterator = sideStack.iterator();
			while(stackIterator.hasNext()){
				xmlSerializer = stackIterator.next().getXML(xmlSerializer, id);
				id++;
			}
			
			xmlSerializer.endTag("", "Stack");
			xmlSerializer.text("\n");
			
			xmlSerializer.endTag("", "Process");
			xmlSerializer.text("\n");
			
			xmlSerializer.endDocument();
			
			writer.close();
		}
		catch(Exception e) {
		     e.printStackTrace();
		     return false;
		}
		
		return true;			
	}
}
