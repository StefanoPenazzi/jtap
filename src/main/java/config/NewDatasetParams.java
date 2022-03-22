package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "newDatasetParams")
public class NewDatasetParams {
	
	private Integer destinationsPopThreshold;
	
	public NewDatasetParams() {}
	
	public NewDatasetParams(Integer destinationsPopThreshold) {
		this.destinationsPopThreshold = destinationsPopThreshold;
	}
	
	@XmlElement(name = "destinationsPopThreshold",required = true)
	public Integer getDestinationsPopThreshold() {
		return destinationsPopThreshold;
	}
	public void setDestinationsPopThreshold(Integer destinationsPopThreshold) {
		this.destinationsPopThreshold = destinationsPopThreshold;
	}

}
