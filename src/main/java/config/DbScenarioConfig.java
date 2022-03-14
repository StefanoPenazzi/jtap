package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dbScenarioConfig")
public class DbScenarioConfig {
	
	private RailConfig railConfig;
	private GeoLocConfig geoLocConfig;
	private PopulationConfig populationConfig;
	private ActivitiesConfig activitiesConfig;
	
	@XmlElement(name = "railConfig")
	public RailConfig getRailConfig() {
		return this.railConfig;
	}
	
	@XmlElement(name = "geoLocConfig")
	public GeoLocConfig getGeoLocConfig() {
		return this.geoLocConfig;
	}
	
	@XmlElement(name = "populationConfig")
	public PopulationConfig getPopulationConfig() {
		return this.populationConfig;
	}
	
	@XmlElement(name = "activitiesConfig")
	public ActivitiesConfig getActivitiesConfig() {
		return this.activitiesConfig;
	}
	
	public void setRailConfig(RailConfig railConfig) {
		this.railConfig = railConfig;
	}
	public void setGeoLocConfig(GeoLocConfig geoLocConfig) {
		this.geoLocConfig = geoLocConfig;
	}
	public void setPopulationConfig(PopulationConfig populationConfig) {
		this.populationConfig = populationConfig;
	}
	public void setActivitiesConfig(ActivitiesConfig activitiesConfig) {
		this.activitiesConfig = activitiesConfig;
	}
}
