package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "activitiesConfig")
public class ActivitiesConfig {
	
	private String activitiesFile;
	
	@XmlElement(name = "activitiesFile",required = true)
	public String getActivitiesFile() {
		return this.activitiesFile;
	}
	
	public void setActivitiesFile(String activitiesFile) {
    	this.activitiesFile = activitiesFile;
    }

}
