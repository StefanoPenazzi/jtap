package config;

import java.io.File;

import data.utils.io.XML;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;


@XmlRootElement(name = "config")
@XmlAccessorType(XmlAccessType.FIELD)
public final class Config {
	
	private GTFSConfig gtfsConfig;
	private GeneralConfig generalConfig;
	private GeoLocConfig geoLocConfig;
	
	@XmlElement(name = "gtfsConfig")
	public GTFSConfig getGTFSConfig() {
		return this.gtfsConfig;
	}
	
	//@XmlElement(name = "generalConfig")
	public GeneralConfig getGeneralConfig() {
		return this.generalConfig;
	}
	
	public GeoLocConfig getGeoLocConfig() {
		return this.geoLocConfig;
	}
	
	public void setGTFSConfig(GTFSConfig gtfsConfig) {
		this.gtfsConfig = gtfsConfig;
	}
	
	public void setGeneralConfig(GeneralConfig generalConfig) {
		this.generalConfig = generalConfig;
	}
	public void setGeoLocConfig(GeoLocConfig geoLocConfig) {
		this.geoLocConfig = geoLocConfig;
	}
	
	public Config() {}
	public Config(GTFSConfig gtfsConfig,GeneralConfig generalConfig,GeoLocConfig geoLocConfig) {
		this.gtfsConfig = gtfsConfig;
		this.generalConfig = generalConfig;
		this.geoLocConfig = geoLocConfig;
	}
	
	public static Config of(File file) {
	      return XML.read(file, Config.class);
	}
	
	public void write(File file) throws JAXBException {
		XML.write(file,this);
	}
}
