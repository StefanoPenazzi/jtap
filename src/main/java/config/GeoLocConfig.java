package config;

import jakarta.xml.bind.annotation.XmlElement;

public class GeoLocConfig {
	
private static final long serialVersionUID = 1L;
	
	private String citiesFile;
	
	public GeoLocConfig() {
		
	}
	
    public GeoLocConfig(String citiesFile) {
    	this.citiesFile = citiesFile;
	}
	
    @XmlElement(name = "citiesFile",required = true)
	public String getCitiesFile() {
		return this.citiesFile;
	}
    
    public void setCitiesFile(String citiesFile) {
    	this.citiesFile = citiesFile;
    }
	
	
	@Override
    public String toString() {
        return "GeoLocConfig{" +
                "citiesFile='" + this.citiesFile +
                '}';
    }

}
