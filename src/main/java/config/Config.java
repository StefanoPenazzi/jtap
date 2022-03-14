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
	
	
	private GeneralConfig generalConfig;
	private Neo4JConfig neo4JConfig;
	private CtapModelConfig ctapModelConfig;
	private DbScenarioConfig dbScenarioConfig;
	
	@XmlElement(name = "generalConfig")
	public GeneralConfig getGeneralConfig() {
		return this.generalConfig;
	}
	
	@XmlElement(name = "neo4JConfig")
	public Neo4JConfig getNeo4JConfig() {
		return this.neo4JConfig;
	}
	
	@XmlElement(name = "ctapModelConfig")
	public CtapModelConfig getCtapModelConfig() {
		return this.ctapModelConfig;
	}
	
	@XmlElement(name = "dbScenarioConfig")
	public DbScenarioConfig getDbScenarioConfig() {
		return dbScenarioConfig;
	}
	
	public void setGeneralConfig(GeneralConfig generalConfig) {
		this.generalConfig = generalConfig;
	}
	
	public void setNeo4JConfig(Neo4JConfig neo4JConfig) {
		this.neo4JConfig = neo4JConfig;
	}
	public void setCtapModelConfig(CtapModelConfig ctapModelConfig) {
		this.ctapModelConfig = ctapModelConfig;
	}
	public void setDbScenarioConfig(DbScenarioConfig dbScenarioConfig) {
		this.dbScenarioConfig = dbScenarioConfig;
	}
	
	@Inject
	public Config() {}
	
	public Config(GeneralConfig generalConfig,Neo4JConfig neo4JConfig,
			CtapModelConfig ctapModelConfig,DbScenarioConfig dbScenarioConfig) {
	
		this.generalConfig = generalConfig;
		this.neo4JConfig = neo4JConfig;
		this.ctapModelConfig = ctapModelConfig;
		this.dbScenarioConfig = dbScenarioConfig;
	}
	
	public static Config of(File file) {
	      return XML.read(file, Config.class);
	}
	
	public void write(File file) throws JAXBException {
		XML.write(file,this);
	}
}
