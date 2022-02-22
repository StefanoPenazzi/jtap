package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ctapModelConfig")
public class CtapModelConfig {

	private Integer populationThreshold;
	private AttractivenessModelConfig attractivenessModelConfig;
	
	@XmlElement(name = "populationThreshold",required = true)
	public Integer getPopulationThreshold() {
		return this.populationThreshold;
	}
	
    public void setPopulationThreshold(Integer populationThreshold) {
    	this.populationThreshold = populationThreshold;
    }
    
    @XmlElement(name = "attractivenessModelConfig",required = true)
	public AttractivenessModelConfig getAttractivenessModelConfig() {
		return this.attractivenessModelConfig;
	}
	
    public void setAttractivenessModelConfig(AttractivenessModelConfig attractivenessModelConfig) {
    	this.attractivenessModelConfig = attractivenessModelConfig;
    }
}
