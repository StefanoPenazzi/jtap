package config;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "generalConfig")
public class GeneralConfig implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String tempDirectory;
	private String outputDirectory;
	private Integer threads;
	
	public GeneralConfig() {}
	
    public GeneralConfig(String tempDirectory, String outputDirectory,Integer threads) {
    	this.tempDirectory = tempDirectory;
    	this.outputDirectory = outputDirectory;
    	this.threads = threads;
	}
	
    @XmlElement(name = "tempDirectory",required = true)
	public String getTempDirectory() {
		return this.tempDirectory;
	}
    
    @XmlElement(name = "outputDirectory",required = true)
   	public String getOutputDirectory() {
   		return this.outputDirectory;
   	}
    
    @XmlElement(name = "threads",required = true)
   	public Integer getThreads() {
   		return this.threads;
   	}
    
    public void setTempDirectory(String tempDirectory) {
    	this.tempDirectory = tempDirectory;
    }
    
    public void setOutputDirectory(String outputDirectory) {
    	this.outputDirectory = outputDirectory;
    }
    
    public void setThreads(Integer threads) {
    	this.threads = threads;
    }
	
	@Override
    public String toString() {
        return "tempConfig{" +
                "tempDirectory='" + this.tempDirectory + "/n"+
                "outputDirectory='" + this.outputDirectory + "/n"+
                '}';
    }
}
