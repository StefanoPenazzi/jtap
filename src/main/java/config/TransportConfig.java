package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "transportConfig")
public class TransportConfig {
	
	private CtapTransportLinkConfig ctapTransportLinkConfig;
	
	@XmlElement(name = "ctapTransportLinkConfig",required = true)
	public CtapTransportLinkConfig getCtapTransportLinkConfig() {
		return ctapTransportLinkConfig;
	}
	
	public void setCtapTransportLinkConfig(CtapTransportLinkConfig ctapTransportLinkConfig) {
		this.ctapTransportLinkConfig = ctapTransportLinkConfig;
	}

}
