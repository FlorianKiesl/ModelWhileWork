package ce.modelwhilework.data.contextinfo;

public class Text extends ContextInformation {
	StringBuffer text;
	
	public StringBuffer getText() {
		return text;
	}

	public void setText(StringBuffer text) {
		this.text = text;
	}

	public Text(int id, String path) {
		super(id, path);
		this.text = new StringBuffer();
	}
}
