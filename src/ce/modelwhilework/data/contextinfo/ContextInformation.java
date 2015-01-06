package ce.modelwhilework.data.contextinfo;

public abstract class ContextInformation implements Comparable<ContextInformation> {
	protected String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	@Override
	public int compareTo(ContextInformation another) {
		return this.title.compareTo(another.getTitle());
	}
}
