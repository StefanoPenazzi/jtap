package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ctapModelConfig")
public class CtapModelConfig {

	private AttractivenessModelConfig attractivenessModelConfig;
	private DatasetConfig datasetConfig;
	private CtapPopulationConfig ctapPopulationConfig; 
	private TransportConfig transportConfig;
	private CtapActivityLocationConfig ctapActivityLocationConfig;
    
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
    
    @XmlElement(name = "transportConfig",required = true)
    public TransportConfig getTransportConfig() {
		return transportConfig;
	}
    
    public void setTransportConfig(TransportConfig transportConfig) {
		this.transportConfig = transportConfig;
	}
    
    @XmlElement(name = "ctapActivityLocationConfig",required = true)
    public CtapActivityLocationConfig getCtapActivityLocationConfig() {
		return ctapActivityLocationConfig;
	}
    
    public void setCtapActivityLocationConfig(CtapActivityLocationConfig ctapActivityLocationConfig) {
		this.ctapActivityLocationConfig = ctapActivityLocationConfig;
	}
}
