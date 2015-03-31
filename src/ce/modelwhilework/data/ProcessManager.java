package ce.modelwhilework.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import ce.modelwhilework.business.RestWebService;
import ce.modelwhilework.data.Process;
import ce.modelwhilework.data.contextinfo.ContextInformation;

import org.apache.commons.io.FileUtils;
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
	private TreeSet<Process> processSet;
	private TreeSet<Task> favoriteTaskSet;
	private TreeSet<Message> favoriteMessageSet;
	private Process curProcess;
	private File dirInternal, dirExternal, dirExternalCache;
	
	private ProcessManager(){
		this.processSet = new TreeSet<Process>();
		this.favoriteTaskSet = new TreeSet<Task>();
		this.favoriteMessageSet = new TreeSet<Message>();
		
		///only for testing!!!
		favoriteTaskSet.add(new Task("default favorite 1"));
		favoriteTaskSet.add(new Task("default favorite 2"));
		favoriteTaskSet.add(new Task("default favorite 3"));
		favoriteTaskSet.add(new Task("default favorite 4"));
		favoriteTaskSet.add(new Task("default favorite 5"));
		
		favoriteMessageSet.add(new Message("default favorite msg 1", "person", true));
		favoriteMessageSet.add(new Message("default favorite msg 2", "person", false));
		favoriteMessageSet.add(new Message("default favorite msg 3", "person", true));
		favoriteMessageSet.add(new Message("default favorite msg 4", "person", false));
		favoriteMessageSet.add(new Message("default favorite msg 5", "person", true));
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
	
	public File getInternalStorage() { return this.dirInternal; }
	
	public File getExternalStorage() { return this.dirExternal; }

	public File getExternalCacheStorage(){
		return this.dirExternalCache;
	}
	
	public TreeSet<Process> getProcesses() {
		return processSet;
	}
	
	public TreeSet<String> getProcessesFromInternalStoreage() {
		
		TreeSet<String> processes = new TreeSet<String>();
		
		File[] files = getInternalStorage().listFiles();
		for(File f : files) {
			if(f.getName().endsWith(Process.getFileExtension()))
				processes.add(f.getName());
		}
		
		return processes;
	}
	
	public TreeSet<String> getProcessesFromExternalStoreage() {
		
		TreeSet<String> processes = new TreeSet<String>();
		
		File[] files = getExternalStorage().listFiles();
		for(File f : files) {
			if(f.getName().endsWith(Process.getFileExtension()))
				processes.add(f.getName());
		}
		
		return processes;
	}
	
	public Process getCurrentProcess(){
		return this.curProcess;
	}
	
	public int getCurrentProcessPos(){

		int pos = 0;
		Iterator<Process> iterator = this.processSet.iterator();
		while(iterator.hasNext()){
		   if(iterator.next().equals(this.curProcess))
			   return pos;
			pos++;
		}
		
		return -1;
	}
	
	public boolean addProcess(Process process){
		
		String titleUC = process.getFileTitle();
		titleUC = titleUC.toUpperCase(Locale.getDefault());
		
		File[] files = getInternalStorage().listFiles();
		for(File f : files) {
			if(f.getName().toUpperCase(Locale.getDefault()).equals(titleUC))
				return false;
		}
		 
		if(this.processSet.add(process)) {
			this.curProcess = this.getProcess(process.getTitle());
			return true;
		}
		
		return false;
	}
	
	public boolean openProcess(String processName){
		
		Process p = new Process(processName, Settings.getInstance().getUser());
		File file = new File(getInternalStorage().toString());
		if(p.loadXML(file)) {
			
			if(this.processSet.add(p)) {
				this.curProcess = this.getProcess(p.getTitle());
				return true;
			}
		}
		return false;		
	}
	
	public boolean importProcess(String processName){
		
		ArrayList<String> tmpProcesses = new ArrayList<String>(ProcessManager.getInstance().getProcessesFromInternalStoreage());
		
		if(tmpProcesses.contains(processName + Process.getFileExtension()))
			return false;
		
		Process p = new Process(processName, Settings.getInstance().getUser());
		File file = new File(getExternalStorage().toString());
		if(!p.loadXML(file))
			return false;
		
		String strExport;
		File sourceFile, destFile;
		FileChannel source = null, destination = null;
		TreeSet<ContextInformation> tmpCI = p.getAllContextInformations();
		for(ContextInformation ci : tmpCI) {
			strExport = ci.getPath();
			strExport = strExport.replace(getInternalStorage().toString(), getExternalStorage().toString());
			
			destFile = new File(ci.getPath());
			sourceFile = new File(strExport);

		    try {
		    	if(!destFile.exists()) {
			        destFile.createNewFile();
			    }
		    	
		        source = new FileInputStream(sourceFile).getChannel();
		        destination = new FileOutputStream(destFile).getChannel();
		        destination.transferFrom(source, 0, source.size());
		    } catch (IOException e) {
				e.printStackTrace();
			}
		    finally {
		        if(source != null) {
		            try {
						source.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		        if(destination != null) {
		            try {
						destination.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    }
		}
		
		if(this.processSet.add(p)) {
			this.curProcess = this.getProcess(p.getTitle());
			return this.curProcess.storeXML(this.getInternalStorage());
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
		
		String strExport;
		File sourceFile, destFile;
		FileChannel source = null, destination = null;
		TreeSet<ContextInformation> tmpCI = p.getAllContextInformations();
		for(ContextInformation ci : tmpCI) {
			strExport = ci.getPath();
			strExport = strExport.replace(getInternalStorage().toString(), getExternalStorage().toString());
			
			sourceFile = new File(ci.getPath());
			destFile = new File(strExport);

		    try {
		    	if(!destFile.exists()) {
			        destFile.createNewFile();
			    }
		    	
		        source = new FileInputStream(sourceFile).getChannel();
		        destination = new FileOutputStream(destFile).getChannel();
		        destination.transferFrom(source, 0, source.size());
		    } catch (IOException e) {
				e.printStackTrace();
			}
		    finally {
		        if(source != null) {
		            try {
						source.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		        if(destination != null) {
		            try {
						destination.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    }
		}
		
		return p.storeXML(getExternalStorage());
	}
	
	public boolean exportProcessMetasonicExternal(String name){
		Process p = getProcess(name);
		if(p == null)
			return false;
		String fileName = Settings.getInstance().getUser() + "_" 
			+ p.getUserRole() + "_" + p.getTitle() + ".xml";
		return p.storeMetasonicXML(new File(getExternalStorage(), fileName));
	}
	
	public boolean exportProcessWebservice(String name, Context ctx){
		Process p = getProcess(name);
		if (p == null)
			return false;
		RestWebService rWS = new RestWebService();
		rWS.invokeExportXML(p.getMetasonicXML(), ctx);
//		rWS.postExp/ortXML(p.getMetasonicXML());
		return true;
	}
	
	public boolean uploadProcess(String name){
		Process p = getProcess(name);
		if(p == null)
			return false;
		try{
			boolean erg = p.storeXML(this.getExternalCacheStorage());
			File file = new File(this.getExternalCacheStorage(), p.getFileTitle());
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
				String fileName = Settings.getInstance().getUser() + "_" 
						+ p.getUserRole() + "_" + p.getTitle() + ".xml";
				File file = new File(this.getExternalCacheStorage().getPath() + "/" + fileName);
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
			HttpPost httpPost = new HttpPost(Settings.getInstance().getServerMetasonic() + "/upload_xml.php");

			MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
//			multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			FileBody fb = new FileBody(file);
//			ToDo: Mit Inputstream funktioniert upload nicht, nur mit FileBody
//			InputStream in = new FileInputStream(file);
//			InputStreamBody inb = new InputStreamBody(in, getFileTitle());
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
		if(this.getProcess(position) == null) { return false; }
		return this.processSet.remove(this.getProcess(position));
	}
	
	public boolean closeAllProcesses(){
		return this.processSet.removeAll(this.processSet);
	}
	
	public Process getProcess(String title){
		Process process;
		Iterator<Process> iterator = this.processSet.iterator();
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
		Iterator<Process> iterator = this.processSet.iterator();
		while(iterator.hasNext() && iPos <= position){
			process = iterator.next();
			iPos++;
		}
		return process;
	}
	
	public ArrayList<Task> getFavoriteTasks() { return new ArrayList<Task>(this.favoriteTaskSet); }
	
	public Task getFavoriteTask(String title) { 
		
		for(Task t : favoriteTaskSet) {
			if(t.getTitle().equals(title))
				return t;
		}
		
		return null;
	}
	
	public ArrayList<Message> getFavoriteMessages() { return new ArrayList<Message>(this.favoriteMessageSet); }
	
	public Message getFavoriteMessage(String title) { 
		
		for(Message m : favoriteMessageSet) {
			if(m.getTitle().equals(title))
				return m;
		}
		
		return null;
	}
}
