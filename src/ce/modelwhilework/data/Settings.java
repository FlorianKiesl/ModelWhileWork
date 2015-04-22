package ce.modelwhilework.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


public class Settings {

	private static Settings instance;
	private Properties configuration;
    private String configurationFile;
    private String version = "V01.00.000";

    private Settings(String configurationPath){
		configuration = new Properties();
		this.configurationFile = configurationPath + ".settings";
	}
	
	public static Settings getInstance(){
		if (instance == null){
			instance = new Settings(ProcessManager.getInstance().getInternalStorage().toString());
		}
		return instance;
	}

    private boolean load() {
        boolean retval = false;

        try {
        	File configFile = new File(this.configurationFile);
        	if (!configFile.exists()){
        		retval = configFile.createNewFile();
        	}
            configuration.load(new FileInputStream(configFile));
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
    	return get("user", "DefaultUser");
    }
    
    public boolean setServerMetasonic(String server) {
    	return set("server", server);
    }
    
    public String getServerMetasonic() {
    	return get("server", "http://www.stefanoppl.net/fellner");
    }
    
    public boolean setWebservice(String server) {
    	return set("webservice", server);
    }
    
    public String getWebservic() {
    	return get("webservice", "<IP>:8080/import/exml/store");
    }
//    
//    public boolean setParameterWebservice(String name){
//    	return set("parameterWS", name);
//    }
//    
//    public String getParameterWebservice(){
//    	return get("parameterWS", "xmlFileData");
//    }
//    
    public boolean setOffsetX(String offset) {
    	return set("offset_x", offset);
    }
    
    public String getOffsetX() {
    	return get("offset_x", "0");
    }
    
    public boolean setOffsetY(String offset) {
    	return set("offset_y", offset);
    }
    
    public String getOffsetY() {
    	return get("offset_y", "100");
    }
    
    public String getVersion() { return version; }
}
