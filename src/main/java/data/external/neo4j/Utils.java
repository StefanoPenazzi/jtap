package data.external.neo4j;

import java.util.List;

import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;

public class Utils {
	
	public static List<Record> run(String database, String query, AccessMode am ) throws Exception {
		List<Record> res = null;
		try( Neo4jConnection conn = new Neo4jConnection()){  
			res = conn.query(database,query,am);
		}
		return res;
	}
}
