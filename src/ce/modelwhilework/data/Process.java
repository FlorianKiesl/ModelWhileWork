package ce.modelwhilework.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.TreeSet;
import java.util.UUID;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.xml.sax.*;
import org.w3c.dom.*;

import ce.modelwhilework.data.contextinfo.Audio;
import ce.modelwhilework.data.contextinfo.ContextInformation;
import ce.modelwhilework.data.contextinfo.Location;
import ce.modelwhilework.data.contextinfo.Picture;
import ce.modelwhilework.data.contextinfo.Text;
import ce.modelwhilework.data.contextinfo.Video;
import ce.modelwhilework.data.xml.XmlHelper;



public class Process extends Modus {
	
	private final static String fileExtension = ".mwyw";
	
	private Stack<Card> mainStack;
	private Stack<Card> sideStack;
	private Task taskCard;
	private Message messageCard;
	private String userRole;
	private File autoSavePath = null;
	
	public Process(String title, String role, File autoSavePath) {
		super(title);
		
		mainStack = new Stack<Card>();
		sideStack = new Stack<Card>();	
		taskCard = new Task("");	
		taskCard.addStoreListener(this);		
		messageCard = new Message("", "", true);
		messageCard.addStoreListener(this);
		this.addStoreListener(this);
		userRole = role;
		activateAutoSave(autoSavePath);
	}
	
	public boolean activateAutoSave(File autoSavePath)
	{
		this.autoSavePath = autoSavePath;
		return autoSave();
	}
	
	protected boolean autoSave() { 
		
		if(this.autoSavePath != null)
			return storeXML(this.autoSavePath); 
		return true;
	}
	
	public boolean setTopCardTitleMainStack(String title) {
		
		if(title.length() <= 0)
			return false;
		else if(this.getTopCardMainStack().getTitle().equals(title))
			return true;

		if(isTitleUnique(title)) {
			this.getTopCardMainStack().setTitle(title);
			return true;
		}
		
		return false;
	}
	
	public boolean setTopCardSenderReceiverMainStack(String name) { 
		
		if(name.length() <= 0)
			return false;
		
		if(this.getTopCardMainStack() instanceof Message) {
			((Message)this.getTopCardMainStack()).setSenderReceiver(name);
			return true;
		}
		
		return false;
	}
	
	public boolean setTopCardTitleSideStack(String title) {

		if(title.length() <= 0)
			return false;
		else if(this.getTopCardSideStack().getTitle().equals(title))
			return true;		
		
		if(isTitleUnique(title)) {
			this.getTopCardSideStack().setTitle(title);
			return true;
		}
		
		return false;
	}
	
	public boolean setTopCardSenderReceiverSideStack(String name) { 
		
		if(name.length() <= 0)
			return false;
		
		if(this.getTopCardSideStack() instanceof Message) {
			((Message)this.getTopCardSideStack()).setSenderReceiver(name);
			return true;
		}
		
		return false;
	}
	
	private boolean isTitleUnique(String title) {
		
		for(Card i : mainStack) {
			if(i.getTitle().equals(title))
				return false;
		}
		
		for(Card i : sideStack) {
			if(i.getTitle().equals(title))
				return false;					
		}
		
		return true;
	}
	
	public String getFileTitle() { return getTitle() + fileExtension; }
	public static String getFileExtension() { return fileExtension; }
	
	public boolean addCard(Card card) { 
		
		int test = card.getContextInformations().size();
		
		if(isTitleUnique(card.getTitle())) {
			if(mainStack.add(card))
			{
				mainStack.peek().addStoreListener(this);
				return autoSave();
			}
		}		
		
		return false;		
	}
	
	public boolean putCardAside() {
		
		try {
			sideStack.add(mainStack.pop());
			return autoSave();
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean putBackFromAside() {
		
		try { 
			mainStack.add(sideStack.pop());
			return autoSave();
		}
		catch (Exception e) { return false;	}
	}
	
	public boolean removeCardFromMainStack() {
		
		try { 
			mainStack.pop().clearStoreListener();
			return autoSave();
		}
		catch (Exception e) { return false;	}
	}
	
	public boolean removeCardFromSideStack() {
		
		try { 
			sideStack.pop().clearStoreListener();
			return autoSave();
		}
		catch (Exception e) { return false;	}
	}
	
	public boolean isMainStackEmpty() { return mainStack.size() == 0; }
	public boolean isSideStackEmpty() { return sideStack.size() == 0; }
	
	public Card getTopCardMainStack() { 
		if(isMainStackEmpty()) { return null; }
		return mainStack.peek();
	}
	
	public Card getCardAfterTopCardMainStack() { 
		if(getMainStackCount() < 2) { return null; }
		Card f = mainStack.pop();
		Card s = mainStack.peek();
		mainStack.add(f);
		return s;
	}
	
	public Card getTopCardSideStack() { 
		if(isSideStackEmpty()) { return null; }
		return sideStack.peek();
	}
	
	public Card getCardAfterTopCardSideStack() { 
		if(getSideStackCount() < 2) { return null; }
		Card f = sideStack.pop();
		Card s = sideStack.peek();
		sideStack.add(f);
		return s;
	}
	
	public int getMainStackCount() { return mainStack.size(); }
	public int getSideStackCount() { return sideStack.size(); }
	
	public String getUserRole() {
		return userRole;
	}

	public boolean setUserRole(String userRole) {
		this.userRole = userRole;
		return autoSave();
	}

	public Task getTaskCard() { 
		return taskCard;
	}
	
	public boolean setTaskCard(Task t) { 
		taskCard = t;
		return autoSave();
	}
	
	public boolean setTaskCardTitle(String t) { 
		if(isTitleUnique(t))
			taskCard.setTitle(t);
		else
			return false;
		
		return true;
	}
	
	public Message getMessageCard() { 
		return messageCard;
	}
	
	public boolean setMessageCard(Message m) { 
		messageCard = m;
		return autoSave();
	}
	
	public boolean setMessageCardTitle(String t) { 
		if(isTitleUnique(t))
			messageCard.setTitle(t);
		else
			return false;
		
		return true;
	}
	
	public boolean setMessageCardSenderReceiver(String name) { 
		messageCard.setSenderReceiver(name);
		return true;
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
		Stack<Card> main, side, inputTask, inputMsg;
		
		main = new Stack<Card>();
		side = new Stack<Card>();
		inputTask = new Stack<Card>();
		inputMsg = new Stack<Card>();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			// parse  XML file
			dom = db.parse(file);
			
			Element doc = dom.getDocumentElement();
			
			NodeList nlProcess = doc.getElementsByTagName("Process");
			
			boolean ok = false;
			for(int i = 0; i < nlProcess.getLength(); i++) {
				
				e = (Element)nlProcess.item(i);
				addContextInformations2Element(this, e);
				this.setUserRole(e.getAttribute("role"));
				ok = true;
            }		
			
			if(!ok)
				return false;
			
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
				} else if(e.getAttribute("id").equals("InputMessage")) {
					
					childs = e.getChildNodes();
					for(int n = 0; n < childs.getLength(); n++) {
						
						if (childs.item(n).getNodeType() == Node.ELEMENT_NODE) {
							if(!addElement2Stack(inputMsg, (Element)childs.item(n)))
								return false;
						}
					}
				}
				else if(e.getAttribute("id").equals("InputTask")) {
					
					childs = e.getChildNodes();
					for(int n = 0; n < childs.getLength(); n++) {
						
						if (childs.item(n).getNodeType() == Node.ELEMENT_NODE) {
							if(!addElement2Stack(inputTask, (Element)childs.item(n)))
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
		
		if(inputTask.size() > 0) {
			taskCard.clearStoreListener();
			taskCard = (Task) inputTask.get(inputTask.size() - 1);
			taskCard.addStoreListener(this);
		}
			
		
		if(inputMsg.size() > 0) {
			messageCard.clearStoreListener();
			messageCard = (Message) inputMsg.get(inputTask.size() - 1);
			messageCard.addStoreListener(this);
		}
		
        return true;
	}
	
	private boolean addElement2Stack(Stack<Card> s, Element e) {
		
		Card card;
		
		if(e.getAttribute("type").equals(CardType.Task.toString()))
			card = new Task(e.getAttribute("title"));
		else if(e.getAttribute("type").equals(CardType.Message.toString()))
			card = new Message(e.getAttribute("title"), e.getAttribute("communicationPartner"), Boolean.parseBoolean(e.getAttribute("sender")));
		else
			return false;

		card.addStoreListener(this);
		addContextInformations2Element(card, e);
		
		return s.add(card);
	}
	
	private boolean addContextInformations2Element(Modus m, Element modus) {
		
		boolean bRet = true;
		
		TreeSet<ContextInformation> contextInformations = new TreeSet<ContextInformation>();
		//NodeList childs = modus.getElementsByTagName("ContextInformation");
		NodeList childs = modus.getChildNodes();
		for(int n = 0; n < childs.getLength(); n++) {
			
			if (childs.item(n).getNodeType() == Node.ELEMENT_NODE) {
				
				Element e = (Element)childs.item(n);
				
				if(e.getTagName().equals("ContextInformation")) {
					if(e.getAttribute("type").equals(Picture.class.getName())) {
						if(!contextInformations.add(new Picture(Integer.parseInt(e.getAttribute("id")), e.getAttribute("path"))))
							bRet = false;
					}
					else if(e.getAttribute("type").equals(Video.class.getName())) {
						if(!contextInformations.add(new Video(Integer.parseInt(e.getAttribute("id")), e.getAttribute("path"))))
							bRet = false;
					}
					else if(e.getAttribute("type").equals(Text.class.getName())) {
						if(!contextInformations.add(new Text(Integer.parseInt(e.getAttribute("id")), e.getAttribute("path"))))
							bRet = false;
					}
					else if(e.getAttribute("type").equals(Audio.class.getName())) {
						if(!contextInformations.add(new Audio(Integer.parseInt(e.getAttribute("id")), e.getAttribute("path"))))
							bRet = false;
					}
					else if(e.getAttribute("type").equals(Location.class.getName())) {
						if(!contextInformations.add(new Location(Integer.parseInt(e.getAttribute("id")), e.getAttribute("path"))))
							bRet = false;
					}
					else
						bRet = false;					
				}
				else if(e.getTagName().equals("Card"))
					break;				
			}										
		}
		
		m.setContextInformations(contextInformations);
		return bRet;
	}
	
	protected boolean storeXML(File filePath) {
			
		//http://stackoverflow.com/questions/7373567/java-how-to-read-and-write-xml-files
		
		Iterator<Card> stackIterator;
		int id = 0;
		Document dom;
	    Element elStack = null, elProcess = null;

	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    
	    try {

	        DocumentBuilder db = dbf.newDocumentBuilder();
	        dom = db.newDocument();

	        Element elProcessesElement = dom.createElement("Processes");
	        elProcess = dom.createElement("Process");
	        elProcess.setAttribute("user", Settings.getInstance().getUser());
	        elProcess.setAttribute("role", this.getUserRole());
	        super.getContextInformationsXML(dom, elProcess);
	        
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
	        
	        id = 0;
	        stackIterator = sideStack.iterator();
			while(stackIterator.hasNext()){
				
				elStack.appendChild(stackIterator.next().getElementXML(dom, id));
				id++;
			}
			
			//add input task stack
	        elStack = dom.createElement("Stack");
	        elStack.setAttribute("id", "InputTask");
	        elStack.appendChild(taskCard.getElementXML(dom, 0));
	        elProcess.appendChild(elStack);
	        
	        //add input message stack
	        elStack = dom.createElement("Stack");
	        elStack.setAttribute("id", "InputMessage");
	        elStack.appendChild(messageCard.getElementXML(dom, 0));
	        elProcess.appendChild(elStack);

			elProcessesElement.appendChild(elProcess);
	        dom.appendChild(elProcessesElement);
	        return this.writeXML(dom, new File(filePath, getFileTitle()));
	        
	    } catch (ParserConfigurationException pce) {
	    	pce.printStackTrace();
	        return false;
	    }			
	}
	
	protected boolean storeMetasonicXML(File file){
		Document dom;
		try{
			dom = this.getXMLMetasonicDoc();
			if(dom != null){
				return writeXML(dom, file);
			}
			return false;
	    }catch (Exception exc) {
	        return false;
	    }
	}
	
	protected String getMetasonicXML(){
		Document doc = this.getXMLMetasonicDoc();
		
		if (doc != null){
			try {
				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer transformer = tf.newTransformer();
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				StringWriter sw = new StringWriter();
				transformer.transform(new DOMSource(doc), new StreamResult(sw));
				String output = sw.getBuffer().toString().replaceAll("\n|\r", "");
				return output;
			} catch (TransformerException e) {
				e.printStackTrace();
			}			
		}

		
		return "";
	}
	
	private Document getXMLMetasonicDoc(){
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
	        elSubject.setAttribute("RealName", Settings.getInstance().getUser());
	        elSubject.setAttribute("SubjectName", Settings.getInstance().getUser());

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
		        	String name = "<Name>";
		        	if (alCards.get(id).isTask()){
		        		name = alCards.get(id).getTitle() + " erledigt";
		        	}
		        	connectionElem.appendChild(XmlHelper.createXMLTextNode(dom, "name", name));
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
	        
	        return dom;
	    }catch (ParserConfigurationException pce) {
	        return null;
	    }
	}
	
	private boolean writeXML(Document dom, File file){
        try {        	
        	
        	Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
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

	public TreeSet<ContextInformation> getAllContextInformations() {
		
		TreeSet<ContextInformation> ci = new TreeSet<ContextInformation>();
		
		ci.addAll(this.getContextInformations());
		
		for(Card c : this.mainStack)
			ci.addAll(c.getContextInformations());
		
		for(Card c : this.sideStack)
			ci.addAll(c.getContextInformations());
				
		ci.addAll(taskCard.getContextInformations());
		ci.addAll(messageCard.getContextInformations());
		
		return ci;
	}
	
	public static boolean deleteProcess(String name) {
		
		boolean bRet = true;
		
		Process p = new Process(name, "?", ProcessManager.getInstance().getInternalStorage());
		if(!p.loadXML(ProcessManager.getInstance().getInternalStorage()))
			return false;
		
		File file = new File(ProcessManager.getInstance().getInternalStorage(), p.getFileTitle());
		if(!file.delete())
			return false;
		
		TreeSet<ContextInformation> tmpCI = p.getAllContextInformations();
		for(ContextInformation ci : tmpCI) {
			file = new File(ci.getPath());
			if(!file.delete())
				bRet = false;
		}
		
		return bRet;
	}
	
	@Override
	protected String getTypeID() {
		
		return "PROCESS_" + this.getTitle();
	}
	

}
