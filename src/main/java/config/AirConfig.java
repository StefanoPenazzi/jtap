package config;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "airConfig")
public class AirConfig implements Serializable{
	
	private String airportsDirectory;
	private String connectionsDirectory;
	
	public AirConfig() {}
	
	public AirConfig(String airportsDirectory,String connectionsDirectory) {
		this.airportsDirectory = airportsDirectory;
		this.connectionsDirectory = connectionsDirectory;
	}
	 
	@XmlElement(name = "airportsDirectory",required = true)
	public String getAirportsDirectory() {
		return airportsDirectory;
	}
	
	@XmlElement(name = "connectionsDirectory",required = true)
	public String getConnectionsDirectory() {
		return connectionsDirectory;
	}
	
	public void setAirportsDirectory(String airportsDirectory) {
		this.airportsDirectory = airportsDirectory;
	}
	
	public void setConnectionsDirectory(String connectionsDirectory) {
		this.connectionsDirectory = connectionsDirectory;
	}
}
