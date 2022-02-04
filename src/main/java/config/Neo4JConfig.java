package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "neo4JConfig")
public class Neo4JConfig {
	
	private String database;
	
	public Neo4JConfig() {
		
	}
	
    @XmlElement(name = "database",required = true)
	public String getDatabase() {
		return this.database;
	}
    
        
    public void setDatabase(String database) {
    	this.database = database;
    }
    

}
