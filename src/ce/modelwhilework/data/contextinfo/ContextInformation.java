package ce.modelwhilework.data.contextinfo;

public abstract class ContextInformation implements Comparable<ContextInformation> {
	private int id;
	private String path;

	public ContextInformation(int id, String path) {
		this.id = id;
		this.path = path;
	}
	
	public int getID() {
		return id;
	}
	
	public String getPath() {
		return path;
	}

	@Override
	public int compareTo(ContextInformation another) {
		return this.id - another.getID();
	}
}
