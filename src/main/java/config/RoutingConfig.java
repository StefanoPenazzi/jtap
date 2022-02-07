package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "routingConfig")
public class RoutingConfig {

	private String jsonFile;
	
	public RoutingConfig() {
		
	}
	
    @XmlElement(name = "jsonFile",required = false)
	public String getJsonFile() {
		return this.jsonFile;
	}
   
    public void setJsonFile(String jsonFile) {
    	this.jsonFile = jsonFile;
    }
    
}
