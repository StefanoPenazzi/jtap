package config;

import java.io.Serializable;

import jakarta.xml.bind.annotation.*;


@XmlRootElement(name = "gtfsConfig")
public class GTFSConfig implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String gtfsDirectory;
	
	public GTFSConfig() {
		
	}
	
    public GTFSConfig(String gtfsDirectory) {
    	this.gtfsDirectory = gtfsDirectory;
	}
	
    @XmlElement(name = "gtfsDirectory",required = true)
	public String getGTFSDirectory() {
		return this.gtfsDirectory;
	}
    
    public void setGTFSDirectory(String gtfsDirectory) {
    	this.gtfsDirectory = gtfsDirectory;
    }
	
	
	@Override
    public String toString() {
        return "gtfsConfig{" +
                "gtfsDirectory='" + this.gtfsDirectory +
                '}';
    }
	

}
