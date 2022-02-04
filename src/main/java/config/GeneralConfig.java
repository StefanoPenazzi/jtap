package config;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "generalConfig")
public class GeneralConfig implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String tempDirectory;
	private String outputDirectory;
	
	public GeneralConfig() {}
	
    public GeneralConfig(String tempDirectory, String outputDirectory) {
    	this.tempDirectory = tempDirectory;
    	this.outputDirectory = outputDirectory;
	}
	
    @XmlElement(name = "tempDirectory",required = true)
	public String getTempDirectory() {
		return this.tempDirectory;
	}
    
    @XmlElement(name = "outputDirectory",required = true)
   	public String getOutputDirectory() {
   		return this.outputDirectory;
   	}
    
    public void setTempDirectory(String tempDirectory) {
    	this.tempDirectory = tempDirectory;
    }
    
    public void setOutputDirectory(String outputDirectory) {
    	this.outputDirectory = outputDirectory;
    }
	
	@Override
    public String toString() {
        return "tempConfig{" +
                "tempDirectory='" + this.tempDirectory + "/n"+
                "outputDirectory='" + this.outputDirectory + "/n"+
                '}';
    }
}
