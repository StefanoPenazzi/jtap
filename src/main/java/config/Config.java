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
	
	@XmlElement(name = "gtfsConfig")
	public GTFSConfig getGTFSConfig() {
		return this.gtfsConfig;
	}
	
	public void setGTFSConfig(GTFSConfig gtfsConfig) {
		this.gtfsConfig = gtfsConfig;
	}
	
	public Config() {}
	public Config(GTFSConfig gtfsConfig) {
		this.gtfsConfig = gtfsConfig;
	}
	
	public static Config of(File file) {
	      return XML.read(file, Config.class);
	}
	
	public void write(File file) throws JAXBException {
		XML.write(file,this);
	}
}
