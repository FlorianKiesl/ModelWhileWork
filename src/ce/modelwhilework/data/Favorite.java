package ce.modelwhilework.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Favorite {
	private TreeSet<FavoriteCard> favoriteTaskSet;
	private TreeSet<FavoriteCard> favoriteMessageSet;
	
	private static final String FILENAME = "favorites.xml";
	
	public Favorite(){
		this.favoriteTaskSet = new TreeSet<FavoriteCard>();
		this.favoriteMessageSet = new TreeSet<FavoriteCard>();
	}
	
	public TreeSet<FavoriteCard> getFavoriteTaskSet() {
		return favoriteTaskSet;
	}
	public void setFavoriteTaskSet(TreeSet<FavoriteCard> favoriteTaskSet) {
		this.favoriteTaskSet = favoriteTaskSet;
	}
	
	public TreeSet<FavoriteCard> getFavoriteMessageSet() {
		return favoriteMessageSet;
	}
	public void setFavoriteMessageSet(TreeSet<FavoriteCard> favoriteMessageSet) {
		this.favoriteMessageSet = favoriteMessageSet;
	}
	
	public boolean setFavoriteTaskCard(Task card){
		
		FavoriteCard favCard = this.findExistingFavoriteTask(card.getTitle());
		if (favCard != null){
			this.favoriteTaskSet.remove(favCard);
			favCard.setCount(favCard.getCount() + 1);
			this.favoriteTaskSet.add(favCard);
		}
		else{
			favCard = new FavoriteCard(new Task(card.getTitle()), 1);
			this.favoriteTaskSet.add(favCard);
		}
		
		return true;
	}
	
	public boolean setFavoriteMessageCard(Message card){
		
		FavoriteCard favCard = this.findExistingFavoriteMessage(card.getTitle());
		if (favCard != null){
			this.favoriteMessageSet.remove(favCard);
			favCard.setCount(favCard.getCount() + 1);
			this.favoriteMessageSet.add(favCard);
		}
		else{
			favCard = new FavoriteCard(new Message(card.getTitle(), card.getSenderReceiver(), card.isSender()), 1);
			this.favoriteMessageSet.add(favCard);
		}
		
		return true;
	}
	
	private FavoriteCard findExistingFavoriteTask(String title){
		for(FavoriteCard f : this.favoriteTaskSet){
			if(f.getCard().getTitle().compareTo(title) == 0){
				return f;
			}
		}	
		return null;
	}
	
	private FavoriteCard findExistingFavoriteMessage(String title){
		for(FavoriteCard f : this.favoriteMessageSet){
			if(f.getCard().getTitle().compareTo(title) == 0){
				return f;
			}
		}	
		return null;
	}

	protected boolean loadXML(File filePath){
		File file = new File(filePath, FILENAME);
		Document dom;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Element elem;
//		NodeList childs;
		NodeList nlFavoriteCards;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			// parse  XML file
			dom = db.parse(file);
			
			Element doc = dom.getDocumentElement();
			if (doc != null){
				nlFavoriteCards = doc.getElementsByTagName("Card");
				for(int i = 0; i < nlFavoriteCards.getLength(); i++) {
					
					elem = (Element)nlFavoriteCards.item(i);
					if (nlFavoriteCards.item(i).getNodeType() == Node.ELEMENT_NODE) {
						if(!addElement2FavoriteLists((Element)nlFavoriteCards.item(i)))
							return false;		
					}
				}
				return true;				
			}
			return false;
		}catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
            return false;
        } catch (SAXException se) {
            System.out.println(se.getMessage());
            return false;
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            return false;
        }
	}
	
	private boolean addElement2FavoriteLists(Element e) {
		
		Card card;
		int count;
		
		if(e.getAttribute("type").equals(CardType.Task.toString())){
			card = new Task(e.getAttribute("title"));
			
			count = Integer.valueOf(e.getAttribute("count"));
			return this.favoriteTaskSet.add(new FavoriteCard(card, count));	
		}

		else if(e.getAttribute("type").equals(CardType.Message.toString())){
			card = new Message(e.getAttribute("title"), e.getAttribute("communicationPartner"), Boolean.parseBoolean(e.getAttribute("sender")));
			
			count = Integer.valueOf(e.getAttribute("count"));
			return this.favoriteMessageSet.add(new FavoriteCard(card, count));	
		}

		else{
			return false;
		}

	}
	
	protected boolean storeXML(File filePath){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    Document dom;
	    try{
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        dom = db.newDocument();
	        Element elProcessesElement = dom.createElement("FavoriteCards");
	        for (FavoriteCard item : this.favoriteTaskSet){
	        	elProcessesElement.appendChild(item.getXmlElement(dom));
	        }
	        for (FavoriteCard item : this.favoriteMessageSet){
	        	elProcessesElement.appendChild(item.getXmlElement(dom));
	        }  
	        dom.appendChild(elProcessesElement);
	        return this.writeXML(dom, new File(filePath, FILENAME));
	    	
	    } catch (ParserConfigurationException pce) {
	    	pce.printStackTrace();
	        return false;
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
}
