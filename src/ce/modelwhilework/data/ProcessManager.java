package ce.modelwhilework.data;

import java.util.LinkedHashSet;
import java.util.Iterator;

import ce.modelwhilework.data.Process;

public class ProcessManager {

	private static ProcessManager instance;
	private LinkedHashSet<Process> linkedhashSetProcess;
	private Process curProcess;
	
	private ProcessManager(){
		this.linkedhashSetProcess = new LinkedHashSet<Process>();
	}
	
	public static ProcessManager getInstance(){
		if (instance == null){
			instance = new ProcessManager();
		}
		return instance;
	}
	
	public LinkedHashSet<Process> getProcesses() {
		return linkedhashSetProcess;
	}
	
	public Process getCurrentProcess(){
		return this.curProcess;
	}
	
	public boolean addProcess(Process process){
		boolean ret = this.linkedhashSetProcess.add(process);
		this.curProcess = this.getProcess(process.getTitle());
		return ret;
	}
	
	/*
	 * Set the current Process to the given title.
	 */
	public Process setCurrentProcess(int position){
		this.curProcess = this.getProcess(position);
		return curProcess;
	}
	
	//ToDo: XML Zurückgeben (Unseres)
	public boolean saveProcess(String title){
		return true;
	}
	
	public boolean saveAllProcesses(){
		return true;
	}
	
	//ToDo: XML Zurückgeben (Metasonic)
	public boolean exportProcess(){
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
