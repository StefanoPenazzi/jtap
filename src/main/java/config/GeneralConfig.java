package config;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "generalConfig")
public class GeneralConfig implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String tempDirectory;
	
	public GeneralConfig() {}
	
    public GeneralConfig(String tempDirectory) {
    	this.tempDirectory = tempDirectory;
	}
	
    @XmlElement(name = "tempDirectory",required = true)
	public String getTempDirectory() {
		return this.tempDirectory;
	}
    
    public void setTempDirectory(String tempDirectory) {
    	this.tempDirectory = tempDirectory;
    }
	
	@Override
    public String toString() {
        return "tempConfig{" +
                "tempDirectory='" + this.tempDirectory +
                '}';
    }
}
