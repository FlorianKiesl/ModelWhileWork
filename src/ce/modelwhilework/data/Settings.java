package ce.modelwhilework.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


public class Settings {

	private static Settings instance;
	private Properties configuration;
    private String configurationFile;

    private Settings(String configurationFile){
		configuration = new Properties();
	}
	
	public static Settings getInstance(){
		if (instance == null){
			instance = new Settings(ProcessManager.getInstance().getInternalStoreage().toString());
		}
		return instance;
	}

    private boolean load() {
        boolean retval = false;

        try {
            configuration.load(new FileInputStream(this.configurationFile));
            retval = true;
        } catch (IOException e) {
            System.out.println("Configuration error: " + e.getMessage());
        }

        return retval;
    }

    private boolean store() {
        boolean retval = false;

        try {
            configuration.store(new FileOutputStream(this.configurationFile), null);
            retval = true;
        } catch (IOException e) {
            System.out.println("Configuration error: " + e.getMessage());
        }

        return retval;
    }

    private boolean set(String key, String value) {
    	
    	if(load()) {
    	
    		configuration.setProperty(key, value);
    		return store();
    	}
        return false;        
    }

    private String get(String key, String strDefault) {
    	
    	if(load()) {
    		String str = configuration.getProperty(key);
    		if(str != null)
    			return str;
    	}
    	
    	return strDefault;
    }
    
    public boolean setUser(String user) {
    	return set("user", user);
    }
    
    public String getUser() {
    	return get("user", "Error");
    }
    
    public boolean setServerMetasonic(String server) {
    	return set("server", server);
    }
    
    public String getServerMetasonic() {
    	return get("server", "Error");
    }
    
    public boolean setOffsetX(String offset) {
    	return set("offset_x", offset);
    }
    
    public String getOffsetX() {
    	return get("offset_x", "Error");
    }
    
    public boolean setOffsetY(String offset) {
    	return set("offset_y", offset);
    }
    
    public String getOffsetY() {
    	return get("offset_y", "Error");
    }
}
