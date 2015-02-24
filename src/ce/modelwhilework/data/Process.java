package ce.modelwhilework.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.UUID;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.xml.sax.*;
import org.w3c.dom.*;

import ce.modelwhilework.data.xml.XmlHelper;



public class Process extends Modus {
	
	private final String fileExtension = ".mwyw";
	
	private Stack<Card> mainStack;
	private Stack<Card> sideStack;
	private Task taskCard;
	private Message messageCard;
	
	public Process(String title) {
		super(title);
		
		mainStack = new Stack<Card>();
		sideStack = new Stack<Card>();	
		taskCard = new Task("");
		messageCard = new Message("", "", true);
	}
	
	public String getFileTitle() { return getTitle() + fileExtension; }
	
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
	
	public Task getTaskCard() { 
		return taskCard;
	}
	
	public void setTaskCard(Task t) { 
		taskCard = t;
	}
	
	public Message getMessageCard() { 
		return messageCard;
	}
	
	public void setMessageCard(Message m) { 
		messageCard = m;
	}
	
	public Card getTopCard(String title) {
		
		if(messageCard.getTitle().equals(title))
			return messageCard;
		else if(taskCard.getTitle().equals(title))
			return taskCard;
		else if(getTopCardMainStack() != null && getTopCardMainStack().getTitle().equals(title))
			return getTopCardMainStack();
		else if(getTopCardSideStack() != null && getTopCardSideStack().getTitle().equals(title))
			return getTopCardSideStack();

		return null;
	}
	
	public boolean loadXML(File filePath) {
		
		File file = new File(filePath, getFileTitle());
		Document dom;
		Element e;
		NodeList childs;
		Stack<Card> main, side;
		
		main = new Stack<Card>();
		side = new Stack<Card>();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			// parse  XML file
			dom = db.parse(file);
			
			Element doc = dom.getDocumentElement();
			NodeList nlStacks = doc.getElementsByTagName("Stack");
			
			for(int i = 0; i < nlStacks.getLength(); i++) {
				
				e = (Element)nlStacks.item(i);
				
				if(e.getAttribute("id").equals("Main")) {
					
					childs = e.getChildNodes();
					for(int n = 0; n < childs.getLength(); n++) {
						
						if (childs.item(n).getNodeType() == Node.ELEMENT_NODE) {
							if(!addElement2Stack(main, (Element)childs.item(n)))
								return false;		
						}										
					}	
				}
				else if(e.getAttribute("id").equals("Side")) {
					
					childs = e.getChildNodes();
					for(int n = 0; n < childs.getLength(); n++) {
						
						if (childs.item(n).getNodeType() == Node.ELEMENT_NODE) {
							if(!addElement2Stack(side, (Element)childs.item(n)))
								return false;	
						}																			
					}					
				}
            }

        } catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
            return false;
        } catch (SAXException se) {
            System.out.println(se.getMessage());
            return false;
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            return false;
        }

		mainStack = main;
		sideStack = side;
		
        return true;
	}
	
	private boolean addElement2Stack(Stack<Card> s, Element e) {
		
		if(e.getAttribute("type").equals(CardType.Task.toString()))
			return s.add(new Task(e.getAttribute("title")));
		else if(e.getAttribute("type").equals(CardType.Message.toString()))
			return s.add(new Message(e.getAttribute("title"), e.getAttribute("communicationPartner"), Boolean.parseBoolean(e.getAttribute("sender"))));

		return false;
	}
	
	public boolean storeXML(File filePath) {
			
		//http://stackoverflow.com/questions/7373567/java-how-to-read-and-write-xml-files
		
		Iterator<Card> stackIterator;
		int id = 0;
		Document dom;
	    Element elStack = null, elProcess = null;

	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    
	    try {

	        DocumentBuilder db = dbf.newDocumentBuilder();
	        dom = db.newDocument();

	        elProcess = dom.createElement("Process");

	        //add main stack
	        elStack = dom.createElement("Stack");
	        elStack.setAttribute("id", "Main");
	        elProcess.appendChild(elStack);
	        
	        stackIterator = mainStack.iterator();
			while(stackIterator.hasNext()){
				
				elStack.appendChild(stackIterator.next().getElementXML(dom, id));
				id++;
			}
			
			//add side stack
	        elStack = dom.createElement("Stack");
	        elStack.setAttribute("id", "Side");
	        elProcess.appendChild(elStack);
	        
	        stackIterator = sideStack.iterator();
			while(stackIterator.hasNext()){
				
				elStack.appendChild(stackIterator.next().getElementXML(dom, id));
				id++;
			}

	        dom.appendChild(elProcess);
	        return this.writeXML(dom, new File(filePath, getFileTitle()));
	        
	    } catch (ParserConfigurationException pce) {
	    	pce.printStackTrace();
	        return false;
	    }			
	}
	
	protected boolean storeMetasonicXML(File file){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    Document dom;
	    Element elProcess;
	    Element elSubject;
	    ArrayList<Card> alCards;
		try{
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        dom = db.newDocument();
	        elProcess = dom.createElement("EntireModel");
	        elProcess.setAttribute("Version", "1.3.3");
	        elProcess.setAttribute("ProcessName", this.getTitle());
	        
	        elSubject = dom.createElement("Subject");
	        elSubject.setAttribute("RealName", "[UserName]");
	        elSubject.setAttribute("SubjectName", "[UserName]");

	        alCards = this.getCardsAsList();
	        Iterator<Card> iteratorCards = alCards.iterator();
	        int id = 1;
	        while (iteratorCards.hasNext()){
	        	elSubject.appendChild(iteratorCards.next().getMetasonicElementXML(dom, id, "Element"));
	        	id++;
	        }

	        if (alCards.size() > 1){
	        	Element connectionElem;
	        	id = 0;
	        	while (id < alCards.size()-1){
		        	connectionElem = dom.createElement("Connection");
		        	connectionElem.appendChild(XmlHelper.createXMLTextNode(dom, "UUID", UUID.randomUUID().toString()));
		        	String name = "";
		        	if (alCards.get(id).isTask()){
		        		name = alCards.get(id).getTitle() + " erledigt";
		        	}
		        	connectionElem.appendChild(XmlHelper.createXMLTextNode(dom, "Name", name));
		        	connectionElem.appendChild(XmlHelper.createXMLTextNode(dom, "directed1", "false"));
		        	connectionElem.appendChild(XmlHelper.createXMLTextNode(dom, "directed2", "true"));
		        	
		        	connectionElem.appendChild(alCards.get(id).getMetasonicElementXML(dom, id +1, "endPoint1"));
		        	connectionElem.appendChild(alCards.get(id+1).getMetasonicElementXML(dom, id + 2, "endPoint2"));
		        	if (alCards.get(id).isMessage()){
		        		connectionElem.appendChild(alCards.get(id).getMsgXMLElement(dom));
		        	}
		        	elSubject.appendChild(connectionElem);	 
		        	id++;
	        	}
	        }
	        
	        elProcess.appendChild(elSubject);
	        
	        dom.appendChild(elProcess);
	        return writeXML(dom, file);
	    }catch (ParserConfigurationException pce) {
	        return false;
	    }
	}
	
	private boolean writeXML(Document dom, File file){
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // write DOM to file
            tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(file)));
            return true;

        } catch (TransformerException te) {
        	te.printStackTrace();
        	return false;
        } catch (IOException ioe) {
        	ioe.printStackTrace();
        	return false;
        }		
	}
	
	private ArrayList<Card> getCardsAsList(){
		ArrayList<Card> alCards = new ArrayList<Card>();
		Object[] arrCards;
		int i = 0;
        arrCards = mainStack.toArray();
        if (arrCards != null){
        	i = 0;
        	while (i <= arrCards.length - 1){
        		alCards.add((Card) arrCards[i]);
        		i++;
        	}
        }
        
        arrCards = sideStack.toArray();
        if (arrCards != null){
        	i = arrCards.length - 1;
        	while (i >= 0){
        		alCards.add((Card) arrCards[i]);
        		i--;
        	}
        }		
        
        return alCards;
	}

	@Override
	protected String getTypeID() {
		
		return "PROCESS_" + this.getTitle();
	}
	

}
