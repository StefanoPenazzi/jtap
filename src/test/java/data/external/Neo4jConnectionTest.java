package data.external;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import data.external.neo4j.Neo4jConnection;

class Neo4jConnectionTest {

	@Test
	void test() throws Exception {
		
		try( Neo4jConnection conn = new Neo4jConnection()){  
			
			conn.query("zurich", "CREATE (a:Greeting {message: 'Hello, Example-Database'}) RETURN a",AccessMode.WRITE );
			List<Record> res = conn.query("zurich", "MATCH (a:Greeting) RETURN a.message as msg" );
			System.out.println(res.get(0).toString());
	    }
	}
	

}
