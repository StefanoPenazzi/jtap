package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "activitiesConfig")
public class ActivitiesConfig {
	
	private String activitiesFile;
	private String activitiesLocFile;
	
	@XmlElement(name = "activitiesFile",required = true)
	public String getActivitiesFile() {
		return this.activitiesFile;
	}
	
	@XmlElement(name = "activitiesLocFile",required = true)
	public String getActivitiesLocFile() {
		return this.activitiesLocFile;
	}
	
	public void setActivitiesFile(String activitiesFile) {
    	this.activitiesFile = activitiesFile;
    }
	
	public void setActivitiesLocFile(String activitiesLocFile) {
    	this.activitiesLocFile = activitiesLocFile;
    }

}
