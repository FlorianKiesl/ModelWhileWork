package ce.modelwhilework.data;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Iterator;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import ce.modelwhilework.data.Process;

public class ProcessManager {

	private static ProcessManager instance;
	private LinkedHashSet<Process> linkedhashSetProcess;
	private Process curProcess;
	private File dirInternal, dirExternal;
	
	private ProcessManager(){
		this.linkedhashSetProcess = new LinkedHashSet<Process>();
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
	
	public File getInternalStoreage() { return this.dirInternal; }
	
	public File getExternalStoreage() { return this.dirExternal; }
	
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
		
		Process p = new Process("defaultLoad");
		File file = new File(getInternalStoreage(), processName);
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
	public boolean exportProcess(String name){
		Process p = getProcess(name);
		if(p == null)
			return false;
		
		return p.storeXML(getExternalStoreage());
	}
	
	//ToDo: XML Zurückgeben (Metasonic)
		public boolean exportProcessMetasonic(){
			return true;
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
}
