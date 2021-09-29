package config;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "populationConfig")
public class PopulationConfig implements Serializable {
	
    private static final long serialVersionUID = 1L;
	
	private String agentFile;
	private String residenceFile;
	private String familyFile;
	private String friendFile;
	
	public PopulationConfig() {
		
	}
	
    public PopulationConfig(String agentFile) {
    	this.agentFile = agentFile;
	}
	
    @XmlElement(name = "agentFile",required = true)
	public String getAgentFile() {
		return this.agentFile;
	}
    
    @XmlElement(name = "residenceFile",required = true)
	public String getResidenceFile() {
		return this.residenceFile;
	}
    
    @XmlElement(name = "familyFile",required = true)
	public String getFamilyFile() {
		return this.familyFile;
	}
    
    @XmlElement(name = "friendFile",required = true)
	public String getFriendFile() {
		return this.friendFile;
	}
    
    public void setAgentFile(String agentFile) {
    	this.agentFile = agentFile;
    }
    
    public void setResidenceFile(String residenceFile) {
    	this.residenceFile = residenceFile;
    }
    
    public void setFamilyFile(String familyFile) {
    	this.familyFile = familyFile;
    }
    
    public void setFriendFile(String friendFile) {
    	this.friendFile = friendFile;
    }
	
	
	@Override
    public String toString() {
        return "populationConfig{" +
                "populationDirectory='" + this.agentFile +
                '}';
    }

}
