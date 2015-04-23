package ce.modelwhilework.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.util.Log;
import ce.modelwhilework.data.contextinfo.Audio;
import ce.modelwhilework.data.contextinfo.ContextInformation;
import ce.modelwhilework.data.contextinfo.Picture;
import ce.modelwhilework.data.contextinfo.Text;
import ce.modelwhilework.data.contextinfo.Video;

public abstract class Modus implements Comparable<Modus> {
	
	private String title;
	private TreeSet<ContextInformation> contextInformations;
	private TreeSet<Process> storeListener;
	
	public Modus(String title) {
		this.title = title;
		this.contextInformations = new TreeSet<ContextInformation>();
		this.storeListener = new TreeSet<Process>();
	}
	
	public void setTitle(String title) { 
				
		this.title = title;	
		
		//change path of context information
		for(ContextInformation c : contextInformations) {
			
			File oldFile = new File(c.getPath());
			c.setPath(this.getContextInfoFilePath(c.getID()));
			File newFile = new File(c.getPath());
			oldFile.renameTo(newFile);
		}

		fireStoreListener();
	}
	
	public String getTitle() { return this.title; }

	public TreeSet<ContextInformation> getContextInformations() {
		return contextInformations;
	}
	
	public boolean hasContextInformation() { return contextInformations.size() > 0; }

	public void setContextInformations(TreeSet<ContextInformation> contextInformations) {
		this.contextInformations = contextInformations;
		fireStoreListener();
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
	
	public boolean removeContextInfo(int id){
		if (this.contextInformations != null){
			int i = 0;
			Iterator<ContextInformation> iterator = this.contextInformations.iterator();
			ContextInformation remItem = null;
			
			while (iterator.hasNext()){
				remItem = iterator.next();
				if (id == remItem.getID()){
					
					if (this.contextInformations.remove(remItem)){
						File fileCI = new File(remItem.getPath());
						if (fileCI.exists()){
							if (!fileCI.delete()){
								//TODO: View bescheid geben wenn etwas schiefgegangen ist....
								Log.i("Löschvorgang", "Fehlgeschlagen");
							}
						}
						fireStoreListener();
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean addContextInformationText(byte[] data){
		int id = getFreeID();
		String path = this.getContextInfoFilePath(id);
		if (writeByteArrFile(data, path)){
			contextInformations.add(new Text(id, path));	
			fireStoreListener();
			return true;			
		}
		return false;
	}

	public boolean addContextInformationPicture(byte[] data) {
		int id = getFreeID();
		String path = this.getContextInfoFilePath(id);
		if (writeByteArrFile(data, path)){
			contextInformations.add(new Picture(id, path));		
			fireStoreListener();
			return true;			
		}
		return false;
	}
	
	//TODO: Eventuell mit Picture vereinen
	public boolean addContextInformationVideo(byte[] data) {
		
		int id = getFreeID();
		String path = this.getContextInfoFilePath(id);
		if (writeByteArrFile(data, path)){
			contextInformations.add(new Video(id, path));	
			fireStoreListener();
			return true;			
		}
		return false;
	}
	
	public boolean addContextInformationAudio(byte[] data) {
		
		int id = getFreeID();
		String path = this.getContextInfoFilePath(id);
		if (writeByteArrFile(data, path)){
			contextInformations.add(new Audio(id, path));		
			fireStoreListener();
			return true;			
		}
		return false;
	}
	
	private boolean writeByteArrFile(byte[] data, String path){
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
		
		return ProcessManager.getInstance().getInternalStorage() + "/" + ProcessManager.getInstance().getCurrentProcess().getTitle() + "-CI_" + getTypeID() + "_" + id;
	}
	
	@Override
	public int compareTo(Modus another) {
		return this.getTitle().compareTo(another.getTitle());
	}
	
	public boolean addStoreListener(Process p) {
		
		return storeListener.add(p);
	}
	
	public void clearStoreListener() {
		
		storeListener.clear();
	}

	protected void fireStoreListener() {
		
		for(Process p : storeListener)
			p.autoSave();
	}
}
