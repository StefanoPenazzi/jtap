package core.graph.population;

import com.opencsv.bean.CsvBindByName;

import core.graph.LinkI;
import core.graph.annotations.GraphElementAnnotation.Neo4JLinkElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

@Neo4JLinkElement(label="AgentFamilyLink")
public class AgentFamilyLink implements LinkI {
	
	@CsvBindByName(column = "agent_from_id")
	@Neo4JPropertyElement(key="agent_from_id",type=Neo4JType.TOINTEGER)
	private Integer agentFromId;
	
	@CsvBindByName(column = "agent_to_id")
	@Neo4JPropertyElement(key="agent_to_id",type=Neo4JType.TOINTEGER)
	private Integer agentToId;
	
	@CsvBindByName(column = "share")
	@Neo4JPropertyElement(key="share",type=Neo4JType.TOFLOAT)
	private Double share;
	

}
