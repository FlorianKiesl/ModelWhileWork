package ce.modelwhilework.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ce.modelwhilework.data.contextinfo.Audio;
import ce.modelwhilework.data.contextinfo.ContextInformation;
import ce.modelwhilework.data.contextinfo.Picture;
import ce.modelwhilework.data.contextinfo.Video;

public abstract class Modus implements Comparable<Modus> {
	
	private String title;
	private TreeSet<ContextInformation> contextInformations;
	
	public Modus(String title) {  //todo: unique check
		this.title = title;
		this.contextInformations = new TreeSet<ContextInformation>();
	}
	
	public void setTitle(String title) { this.title = title; } //todo: unique check
	public String getTitle() { return this.title; }

	public TreeSet<ContextInformation> getContextInformations() {
		return contextInformations;
	}
	
	public boolean hasContextInformation() { return contextInformations.size() > 0; }

	public void setContextInformations(
			TreeSet<ContextInformation> contextInformations) {
		this.contextInformations = contextInformations;
	}
	
	protected Element getContextInformationsXML(Document dom, Element parent) {
		
		if(this.hasContextInformation()) {
			
			Element contextInfoElement;
			ContextInformation ci;
			
			Iterator<ContextInformation> it;
			it = this.getContextInformations().iterator();
			while(it.hasNext()){
				
				ci = it.next();
				contextInfoElement = dom.createElement("ContextInformation");
				contextInfoElement.setAttribute("id", Integer.toString(ci.getID()));
				contextInfoElement.setAttribute("path", ci.getPath());
				contextInfoElement.setAttribute("type", ci.getClass().getName());
				parent.appendChild(contextInfoElement);
			}
		}
		
		return parent;
	}
	
	abstract protected String getTypeID();
	
	private int getFreeID() {
		
		if(contextInformations.size() == 0)
			return 0;
		
		return  contextInformations.last().getID() + 1;
	}

	public boolean addContextInformationPicture(byte[] data) {
		int id = getFreeID();
		String path = this.getContextInfoFilePath(id);
		if (writeByteArrFile(data, path, id)){
			contextInformations.add(new Picture(id, path));		
			return true;			
		}
		return false;
	}
	
	//TODO: Eventuell mit Picture vereinen
	public boolean addContextInformationVideo(byte[] data) {
		
		int id = getFreeID();
		String path = this.getContextInfoFilePath(id);
		if (writeByteArrFile(data, path, id)){
			contextInformations.add(new Video(id, path));		
			return true;			
		}
		return false;
	}
	
	public boolean addContextInformationAudio(byte[] data) {
		
		int id = getFreeID();
		String path = this.getContextInfoFilePath(id);
		if (writeByteArrFile(data, path, id)){
			contextInformations.add(new Audio(id, path));		
			return true;			
		}
		return false;
	}
	
	private boolean writeByteArrFile(byte[] data, String path, int id){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			fos.write(data);			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}	
		return true;
	}
	
	private String getContextInfoFilePath(int id){
		
		return ProcessManager.getInstance().getInternalStoreage() + "\\" + ProcessManager.getInstance().getCurrentProcess().getTitle() + "\\CI_" + getTypeID() + "_" + id;
	}
	
	@Override
	public int compareTo(Modus another) {
		return this.getTitle().compareTo(another.getTitle());
	}
}
