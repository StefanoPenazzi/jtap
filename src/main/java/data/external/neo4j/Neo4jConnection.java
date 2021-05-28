package data.external.neo4j;

import org.neo4j.driver.*;

import com.google.inject.Inject;

import static org.neo4j.driver.Values.parameters;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//https://we-yun.com/doc/neo4j-doc/4.0/neo4j-driver-manual-4.0-java.pdf

public class Neo4jConnection implements AutoCloseable {
	
	private final Driver driver;
	private final String uri;
	private final String username;
	private final String password;
	
	@Inject
	public Neo4jConnection()
    {
		Properties properties = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("database.properties");
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.uri = properties.getProperty("neo4j_uri");
	    this.username = properties.getProperty("neo4j_username");
	    this.password = properties.getProperty("neo4j_password");
        driver = GraphDatabase.driver( uri, AuthTokens.basic( username, password ) );
    }
	
	public Driver getDriver() {
		return this.driver;
	}
	
	@Override
    public void close() throws Exception
    {
        driver.close();
    }

}
