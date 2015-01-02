package ce.modelwhilework.data;

import java.util.HashSet;

import ce.modelwhilework.data.Process;

public class ProcessManager {

	private static ProcessManager instance;
	private HashSet<Process> listprocess;
	
	private ProcessManager(){
		this.listprocess = new HashSet<Process>();
	}
	
	public ProcessManager getInstance(){
		if (instance == null){
			instance = new ProcessManager();
		}
		return instance;
	}
	
	public boolean addNewProcess(Process process){
		return this.listprocess.add(process);
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
	
	public boolean closeProcess(String title){
		return true;
	}
	
	public boolean closeAllProcesses(){
		return true;
	}

}
