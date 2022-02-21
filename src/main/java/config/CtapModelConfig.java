package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ctapModelConfig")
public class CtapModelConfig {

	private String attractivenessfiile;
	
	@XmlElement(name = "attractivenessFile",required = true)
	public String getAttractivenessFile() {
		return this.attractivenessfiile;
	}
	
    public void setAttractivenessFile(String attractivenessfiile) {
    	this.attractivenessfiile = attractivenessfiile;
    }
}
