package ce.modelwhilework.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.TreeSet;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import ce.modelwhilework.data.Process;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class ProcessManager {

	private static ProcessManager instance;
	private LinkedHashSet<Process> linkedhashSetProcess;
	private LinkedHashSet<Task> favoriteTasks;
	private LinkedHashSet<Message> favoriteMessages;
	private Process curProcess;
	private File dirInternal, dirExternal, dirExternalCache;
	
	private ProcessManager(){
		this.linkedhashSetProcess = new LinkedHashSet<Process>();
		this.favoriteTasks = new LinkedHashSet<Task>();
		this.favoriteMessages = new LinkedHashSet<Message>();
		
		///only for testing!!!
		favoriteTasks.add(new Task("default favorite 1"));
		favoriteTasks.add(new Task("default favorite 2"));
		favoriteTasks.add(new Task("default favorite 3"));
		favoriteTasks.add(new Task("default favorite 4"));
		favoriteTasks.add(new Task("default favorite 5"));
		
		favoriteMessages.add(new Message("default faforite msg 1", "person", true));
		favoriteMessages.add(new Message("default faforite msg 2", "person", false));
		favoriteMessages.add(new Message("default faforite msg 3", "person", true));
		favoriteMessages.add(new Message("default faforite msg 4", "person", false));
		favoriteMessages.add(new Message("default faforite msg 5", "person", true));
	}
	
	public static ProcessManager getInstance(){
		if (instance == null){
			instance = new ProcessManager();
		}
		return instance;
	}
	
	public void setInternalDir(File dir) {
		this.dirInternal = dir;
	}
	
	public void setExternalDir(File dir) {
		this.dirExternal = dir;
	}
	public void setExternalCacheDir(File dir){
		this.dirExternalCache = dir;
	}
	
	public File getInternalStoreage() { return this.dirInternal; }
	
	public File getExternalStoreage() { return this.dirExternal; }

	public File getExternalCacheStorage(){
		return this.dirExternalCache;
	}
	
	public LinkedHashSet<Process> getProcesses() {
		return linkedhashSetProcess;
	}
	
	public Process getCurrentProcess(){
		return this.curProcess;
	}
	
	public int getCurrentProcessPos(){

		int pos = 0;
		Iterator<Process> iterator = this.linkedhashSetProcess.iterator();
		while(iterator.hasNext()){
		   if(iterator.next().equals(this.curProcess))
			   return pos;
			pos++;
		}
		
		return -1;
	}
	
	public boolean addProcess(Process process){
		if(this.linkedhashSetProcess.add(process)) {
			this.curProcess = this.getProcess(process.getTitle());
			return true;
		}
		
		return false;
	}
	
	public boolean openProcess(String processName){
		
		Process p = new Process(processName);
		File file = new File(getInternalStoreage().toString());
		if(p.loadXML(file)) {
			
			if(this.linkedhashSetProcess.add(p)) {
				this.curProcess = this.getProcess(p.getTitle());
				return true;
			}
		}
		return false;		
	}
	
	/*
	 * Set the current Process to the given title.
	 */
	public Process setCurrentProcess(int position){
		this.curProcess = this.getProcess(position);
		return curProcess;
	}
	
	//ToDo: XML Zurückgeben (mwyw)
	public boolean exportProcessExternal(String name){
		Process p = getProcess(name);
		if(p == null)
			return false;
		
		return p.storeXML(getExternalStoreage());
	}
	
	public boolean exportProcessMetasonicExternal(String name){
		Process p = getProcess(name);
		if(p == null)
			return false;
		return p.storeMetasonicXML(new File(getExternalStoreage(), p.getTitle() + ".xml"));
	}
	
	public boolean uploadProcess(String name){
		Process p = getProcess(name);
		if(p == null)
			return false;
		try{
			boolean erg = p.storeXML(this.getExternalCacheStorage());
			File file = new File(this.getExternalCacheStorage(), p.getTitle() + ".mwyw");
			if (erg){
				erg = this.uploadData(file);
			}
			file.delete();
			return erg;
		} catch (Exception exc){
			exc.printStackTrace();
		}
		return false;
	}
	
	//ToDo: XML Zurückgeben (Metasonic)
	public boolean uploadProcessMetasonic(String name){
		Process p = getProcess(name);
		if (p != null){
			try{
				File file = new File(this.getExternalCacheStorage().getPath() + "/metasonicExportMWYW.xml");
//				if(!file.exists()){
//					file.mkdir();
//				}
				boolean erg = p.storeMetasonicXML(file);
				
				if (erg) {
					erg = this.uploadData(file);
				}
				file.delete();
				
				return erg;	
			}catch(Exception exc){
				exc.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean uploadData(File file){
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.stefanoppl.net/fellner/upload_xml.php");

			MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
//			multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			FileBody fb = new FileBody(file);
//			ToDo: Mit Inputstream funktioniert upload nicht, nur mit FileBody
//			InputStream in = new FileInputStream(file);
//			InputStreamBody inb = new InputStreamBody(in, getTitle() + ".mwyw");
			multipartEntity.addPart("file", fb);

			httpPost.setEntity(multipartEntity.build());

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
	
	public boolean closeProcess(int position){
		return this.linkedhashSetProcess.remove(this.getProcess(position));
	}
	
	public boolean closeAllProcesses(){
		return this.linkedhashSetProcess.removeAll(this.linkedhashSetProcess);
	}
	
	public Process getProcess(String title){
		Process process;
		Iterator<Process> iterator = this.linkedhashSetProcess.iterator();
		while(iterator.hasNext()){
			process = iterator.next();
			if (process.getTitle().compareTo(title)==0){
				return process;
			}
		}
		return null;
	}
	
	public Process getProcess(int position){
		Process process = null;
		int iPos = 0;
		Iterator<Process> iterator = this.linkedhashSetProcess.iterator();
		while(iterator.hasNext() && iPos <= position){
			process = iterator.next();
			iPos++;
		}
		return process;
	}
	
	public ArrayList<Task> getFavoriteTasks() { return new ArrayList<Task>(this.favoriteTasks); }
	public ArrayList<Message> getFavoriteMessages() { return new ArrayList<Message>(this.favoriteMessages); }
}
