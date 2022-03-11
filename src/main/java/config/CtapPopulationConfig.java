package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ctapPopulationConfig")
public class CtapPopulationConfig {
	
	private CtapAgentConfig ctapAgentConfig;
	
	@XmlElement(name = "ctapAgentConfig",required = true)
    public CtapAgentConfig getCtapAgentConfig() {
		return this.ctapAgentConfig;
	}
    public void setCtapAgentConfig(CtapAgentConfig ctapAgentConfig) {
		this.ctapAgentConfig = ctapAgentConfig;
	}

}
