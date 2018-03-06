package org.cloudbus.intercloudsim.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.cloudbus.cloudsim.Log;


/**
 * 
 * Loading/managing InterCloud Gateway configuration.
 * 
 * @author Alexandre di Costanzo
 */
public class InterCloudSimConfiguration {

	
	public static String DEFAULT_PATH ="/Users/yaser/Documents/workspaceTest/paper4/default.properties";
	private static InterCloudSimConfiguration singleton;
	private Properties properties;

	private InterCloudSimConfiguration() {
		// 1. Load default properties
		loadDefaultProperties();
	}


	private void loadDefaultProperties() {
		Properties default_properties = new Properties();

		
		try {
			InputStream in =new FileInputStream(DEFAULT_PATH);
			default_properties.load(in);
			in.close();
		} catch (IOException ex) {
			//Log.logger.warning("Cannot open default properties file");
		     Log.print("Cannot open default properties file");
			System.exit(2);
		}catch(Exception e){
			e.printStackTrace();
		}
		this.properties = new Properties(default_properties);
	}
	
	/**
	 * Get an ICG's property.
	 * 
	 * @param key
	 *            the property's key
	 * @return the value associated to that key.
	 */
	protected String getProperty(String key) {
		synchronized (this.properties) {
			return this.properties.getProperty(key);
		}
	}
	
	
	/**
	 * @return the singleton reference.
	 */
	public static InterCloudSimConfiguration getInstance() {
		if (singleton == null) {
			singleton = new InterCloudSimConfiguration();
		}
		return singleton;
	}
	
	/**
	 * Set an ICG's property.
	 * 
	 * @param key
	 *            the property's key to set.
	 * @param value
	 *            the new value for the property.
	 * @return the previous value associated to that key.
	 */
	protected Object setProperty(String key, String value) {
		synchronized (this.properties) {
			return this.properties.setProperty(key, value);
		}
	}

}
