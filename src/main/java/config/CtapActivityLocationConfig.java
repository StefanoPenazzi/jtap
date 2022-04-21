package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ctapActivityLocationConfig")
public class CtapActivityLocationConfig {
	
	private String destinationsProbDistFile;
	private String activitiesProbDistFile;
	
	@XmlElement(name = "activitiesProbDistFile",required = true)
	public String getActivitiesProbDistFile() {
		return activitiesProbDistFile;
	}
	
	public void setActivitiesProbDistFile(String activitiesProbDistFile) {
		this.activitiesProbDistFile = activitiesProbDistFile;
	}
	
	@XmlElement(name = "destinationsProbDistFile",required = true)
	public String getDestinationsProbDistFile() {
		return destinationsProbDistFile;
	}
	
	public void setDestinationsProbDistFile(String destinationsProbDistFile) {
		this.destinationsProbDistFile = destinationsProbDistFile;
	}

}
