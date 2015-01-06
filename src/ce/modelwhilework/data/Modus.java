package ce.modelwhilework.data;

import java.util.HashSet;

import ce.modelwhilework.data.contextinfo.ContextInformation;

public abstract class Modus {
	
	private String title;
	private HashSet<ContextInformation> contextInformations;
	
	public Modus(String title) {
		this.title = title;
		this.contextInformations = new HashSet<ContextInformation>();
	}
	
	public String getTitle() { return this.title; }

	public HashSet<ContextInformation> getContextInformations() {
		return contextInformations;
	}

	public void setContextInformations(
			HashSet<ContextInformation> contextInformations) {
		this.contextInformations = contextInformations;
	}
}
