package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "attractivenessModelConfig")
public class AttractivenessModelConfig {

    private String modelName;
    private AttractivenessNormalizedConfig attractivenessNormalizedConfig;
	
	@XmlElement(name = "modelName",required = true)
	public String getModelName() {
		return this.modelName;
	}
	
    public void setModelName(String modelName) {
    	this.modelName = modelName;
    }
    
    @XmlElement(name = "attractivenessNormalizedConfig",required = false)
	public AttractivenessNormalizedConfig getAttractivenessNormalizedConfig() {
		return this.attractivenessNormalizedConfig;
	}
	
    public void setAttractivenessNormalizedConfig(AttractivenessNormalizedConfig attractivenessNormalizedConfig) {
    	this.attractivenessNormalizedConfig = attractivenessNormalizedConfig;
    }
	
}
