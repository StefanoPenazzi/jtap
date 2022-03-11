package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ctapModelConfig")
public class CtapModelConfig {

	private AttractivenessModelConfig attractivenessModelConfig;
	private DatasetConfig datasetConfig;
	private CtapPopulationConfig ctapPopulationConfig; 
    
    @XmlElement(name = "attractivenessModelConfig",required = true)
	public AttractivenessModelConfig getAttractivenessModelConfig() {
		return this.attractivenessModelConfig;
	}
	
    public void setAttractivenessModelConfig(AttractivenessModelConfig attractivenessModelConfig) {
    	this.attractivenessModelConfig = attractivenessModelConfig;
    }
    
    @XmlElement(name = "datasetConfig",required = true)
    public DatasetConfig getDatasetConfig() {
		return datasetConfig;
	}
    public void setDatasetConfig(DatasetConfig datasetConfig) {
		this.datasetConfig = datasetConfig;
	}
    
    @XmlElement(name = "ctapPopulationConfig",required = true)
    public CtapPopulationConfig getCtapPopulationConfig() {
		return ctapPopulationConfig;
	}
    public void setCtapPopulationConfig(CtapPopulationConfig ctapPopulationConfig) {
		this.ctapPopulationConfig = ctapPopulationConfig;
	}
}
