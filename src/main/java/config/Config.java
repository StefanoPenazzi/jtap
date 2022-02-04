package config;

import java.io.File;

import com.google.inject.Inject;

import data.utils.io.XML;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;


@XmlRootElement(name = "config")
//@XmlAccessorType(XmlAccessType.FIELD)
public final class Config {
	
	private RailConfig railConfig;
	private GeneralConfig generalConfig;
	private GeoLocConfig geoLocConfig;
	private PopulationConfig populationConfig;
	
	@XmlElement(name = "railConfig")
	public RailConfig getRailConfig() {
		return this.railConfig;
	}
	
	@XmlElement(name = "generalConfig")
	public GeneralConfig getGeneralConfig() {
		return this.generalConfig;
	}
	
	@XmlElement(name = "geoLocConfig")
	public GeoLocConfig getGeoLocConfig() {
		return this.geoLocConfig;
	}
	
	@XmlElement(name = "populationConfig")
	public PopulationConfig getPopulationConfig() {
		return this.populationConfig;
	}
	
	public void setRailConfig(RailConfig railConfig) {
		this.railConfig = railConfig;
	}
	
	public void setGeneralConfig(GeneralConfig generalConfig) {
		this.generalConfig = generalConfig;
	}
	public void setGeoLocConfig(GeoLocConfig geoLocConfig) {
		this.geoLocConfig = geoLocConfig;
	}
	public void setPopulationConfig(PopulationConfig populationConfig) {
		this.populationConfig = populationConfig;
	}
	
	@Inject
	public Config() {}
	
	public Config(RailConfig railConfig,GeneralConfig generalConfig,GeoLocConfig geoLocConfig,PopulationConfig populationConfig) {
		this.railConfig = railConfig;
		this.generalConfig = generalConfig;
		this.geoLocConfig = geoLocConfig;
		this.populationConfig = populationConfig;
	}
	
	public static Config of(File file) {
	      return XML.read(file, Config.class);
	}
	
	public void write(File file) throws JAXBException {
		XML.write(file,this);
	}
}
