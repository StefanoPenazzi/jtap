package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ctapAgentConfig")
public class CtapAgentConfig {
	
	private Integer planSize;
	private Integer maxPlans;
	
	@XmlElement(name = "maxPlans",required = true)
	public Integer getMaxPlans() {
		return maxPlans;
	}
	public void setMaxPlans(Integer maxPlans) {
		this.maxPlans = maxPlans;
	}
	@XmlElement(name = "planSize",required = true)
	public Integer getPlanSize() {
		return planSize;
	}
	public void setPlanSize(Integer planSize) {
		this.planSize = planSize;
	}

}
