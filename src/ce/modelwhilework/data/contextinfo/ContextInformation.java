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
	
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public int compareTo(ContextInformation another) {
		int ret = this.path.compareTo(another.path);
		if(ret == 0)
			ret = this.id - another.getID();			
		return ret;
	}
}
