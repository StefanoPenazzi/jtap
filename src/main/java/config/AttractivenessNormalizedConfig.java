package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "attractivenessNormalized")
public class AttractivenessNormalizedConfig {
	
    private String attractivenessFile;
    private Integer initialTime; 
    private Integer finalTime; 
    private Integer intervalTime; 
    
	
	@XmlElement(name = "attractivenessFile",required = true)
	public String getAttractivenessFile() {
		return this.attractivenessFile;
	}
	
    public void setAttractivenessFile(String attractivenessFile) {
    	this.attractivenessFile = attractivenessFile;
    }
    
    @XmlElement(name = "initialTime",required = true)
    public Integer getInitialTime() {
	   return initialTime;
    }
   
    public void setInitialTime(Integer initialTime) {
	   this.initialTime = initialTime;
    }
   
    @XmlElement(name = "finalTime",required = true)
    public Integer getFinalTime() {
	   return finalTime;
    }
   
    public void setFinalTime(Integer finalTime) {
	   this.finalTime = finalTime;
    }
   
    @XmlElement(name = "intervalTime",required = true)
    public Integer getIntervalTime() {
	   return intervalTime;
    }
   
    public void setIntervalTime(Integer intervalTime) {
	   this.intervalTime = intervalTime;
    }

}
