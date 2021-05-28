package data.external;

import org.junit.jupiter.api.Test;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;


import data.external.neo4j.Neo4jConnection;


class Neo4jConnectionTest {

	@Test
	void test() throws Exception {
		
		try( Neo4jConnection conn = new Neo4jConnection()){  
			
			try ( Session session = conn.getDriver().session( SessionConfig.forDatabase( "zurich" ) ) )
			{
			  session.run( "CREATE (a:Greeting {message: 'Hello, Example-Database'}) RETURN a" ).
			consume();
			}
			
			SessionConfig sessionConfig = SessionConfig.builder()
					  .withDatabase( "zurich" )
					  .withDefaultAccessMode( AccessMode.READ )
					  .build();
			try ( Session session = conn.getDriver().session( sessionConfig ) )
			{
			  String msg = session.run( "MATCH (a:Greeting) RETURN a.message as msg" ).single().get( "msg"
			).asString();
			  System.out.println(msg);
			}
	    }
	}

}
