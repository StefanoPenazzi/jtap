package core.graph.routing;

import java.util.List;

import org.neo4j.driver.AccessMode;

import core.graph.LinkI;
import core.graph.NodeGeoI;
import core.graph.annotations.GraphElementAnnotation.Neo4JLinkElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JNodeElement;
import data.external.neo4j.Neo4jConnection;

public final class RoutingGraph {
	
	private final String id;
	private final List<Class<? extends NodeGeoI>> nodes;
	private final List<Class<? extends LinkI>> links;
	private final String weight;
	
	public RoutingGraph(String id,List<Class<? extends NodeGeoI>> nodes,List<Class<? extends LinkI>> links, String weight) throws Exception {
		this.id = id;
		this.nodes = nodes;
		this.links = links; 
		this.weight = weight;
		
		//check for possible errors nodes
		for(Class<? extends NodeGeoI> cn: nodes) {
			if(!cn.isAnnotationPresent(Neo4JNodeElement.class)) {
				throw new Exception( cn.getName()+ " is not annotated with Neo4JNodeElement");
			}
		}
		//check for possible errors links
		for(Class<? extends LinkI> li: links) {
			if(!li.isAnnotationPresent(Neo4JLinkElement.class)) {
				throw new Exception(li.getName()+ " is not annotated with Neo4JLinkElement");
			}
		}
		
		//check if all the links have the weight property
		for(Class<? extends LinkI> li: links) {
			if(!core.graph.Utils.isAnnotationField(li, weight)) {
				throw new Exception(li.getName()+ " doesn't have the property "+ weight);
			}
		}
		
		
	}
	
	private String graphCachingQuery() throws Exception {
		
		StringBuffer res = new StringBuffer();
		res.append("CALL gds.graph.create.cypher('");
		res.append(this.id);
		
		res.append("','");
		
		res.append("MATCH (n) WHERE ");
		String sn = "";
		for(Class<? extends NodeGeoI> cn: nodes) {
			sn += "n:";
			sn += cn.getAnnotation(Neo4JNodeElement.class).labels()[0];
			sn += " OR ";
		}
		sn = sn.substring(0, sn.length() - 3);
		res.append(sn);
		res.append(" RETURN id(n) AS id, labels(n) AS labels,n.lat AS lat, n.lon AS lon");
		
		res.append("','");
		
		res.append("MATCH (n)-[r:");
		String sl = "";
		for(Class<? extends LinkI> li: links) {
			sl += li.getAnnotation(Neo4JLinkElement.class).label();
			sl += "|";
		}
		sl = sl.substring(0, sl.length() - 1);
		res.append(sl);
		res.append("]->(m) WHERE (");
		res.append(sn);
		res.append(")AND(");
		res.append(sn.replace("n:","m:"));
		res.append(") ");
		res.append("RETURN id(n) AS source, id(m) AS target, type(r) AS type,r.");
		res.append(weight);
		res.append(" AS ");
		res.append(weight);
		res.append("',{validateRelationships:FALSE}) YIELD graphName, nodeCount, relationshipCount, createMillis;");
		return res.toString();
	}
	
	
	public void graphCaching(Neo4jConnection conn,String database) throws Exception {
		String s = graphCachingQuery();
		conn.query(database,s,AccessMode.WRITE );
	}
	
	public void graphCaching(String database) throws Exception {
		try( Neo4jConnection conn = new Neo4jConnection()){  
			String s = graphCachingQuery();
			conn.query(database,s,AccessMode.WRITE );
	    }
	}
	
	public String getId() {
		return this.id;
	}
	
	public List<Class<? extends NodeGeoI>> getNodes(){
		return this.nodes;
	}
	
	public List<Class<? extends LinkI>> getLinks(){
		return this.links;
	}
	public String getWeight() {
		return this.weight;
	};
	

}
