package ce.modelwhilework.data.contextinfo;

public class Text extends ContextInformation {
	StringBuffer text;
	
	public StringBuffer getText() {
		return text;
	}

	public void setText(StringBuffer text) {
		this.text = text;
	}

	public Text(){
		this.text = new StringBuffer();
	}
}
