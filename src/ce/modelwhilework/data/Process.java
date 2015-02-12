package ce.modelwhilework.data;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Stack;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.xml.sax.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.*;



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
		
		File file = new File(filePath, getTitle() + ".mwyw");
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
						
						if(!addElement2Stack(main, (Element)childs.item(n)))
							return false;													
					}	
				}
				else if(e.getAttribute("id").equals("Side")) {
					
					childs = e.getChildNodes();
					for(int n = 0; n < childs.getLength(); n++) {
						
						if(!addElement2Stack(side, (Element)childs.item(n)))
							return false;													
					}					
				}
            }

        } catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (SAXException se) {
            System.out.println(se.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
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

	        try {
	            Transformer tr = TransformerFactory.newInstance().newTransformer();
	            tr.setOutputProperty(OutputKeys.INDENT, "yes");
	            tr.setOutputProperty(OutputKeys.METHOD, "xml");
	            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	            tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
	            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	            // write DOM to file
	            File file = new File(filePath, getTitle() + ".mwyw");
	            tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(file)));

	        } catch (TransformerException te) {
	        	te.printStackTrace();
	        	return false;
	        } catch (IOException ioe) {
	        	ioe.printStackTrace();
	        	return false;
	        }
	    } catch (ParserConfigurationException pce) {
	        return false;
	    }
		
		return true;			
	}
	
	public boolean uploadData(File filepath){
		try{
			File file = new File(filepath, getTitle() + ".mwyw");
			InputStream inputStream = new FileInputStream(file);

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.stefanoppl.net/fellner/upload_xml.php");

			MultipartEntity multipartEntity = new MultipartEntity();
			ContentBody cbFile = new FileBody(file);
			
			multipartEntity.addPart("file", cbFile);
			httpPost.setEntity(multipartEntity);

  			HttpResponse httpResponse = httpClient.execute(httpPost);
  			
		    if(httpResponse != null) {
		    	String response = EntityUtils.toString(httpResponse.getEntity());
		    	return true;
		    } else { // Error, no response.
		    	return false;
		    }
			
		}catch(IOException exc){
			exc.printStackTrace();
		}catch(Exception exc){
			exc.printStackTrace();
		} 
		return false;
	}
}
