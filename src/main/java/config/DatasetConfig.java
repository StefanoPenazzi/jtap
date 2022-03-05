package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "datasetConfig")
public class DatasetConfig {
	
	private String paramsDirectory;
	
	@XmlElement(name = "paramsDirectory",required = false)
	public String getParamsDirectory() {
		return paramsDirectory;
	}
	public void setParamsDirectory(String paramsDirectory) {
		this.paramsDirectory = paramsDirectory;
	}

}
