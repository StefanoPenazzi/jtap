package core.graph.population;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import core.graph.LinkI;
import core.graph.annotations.GraphElementAnnotation.Neo4JLinkElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

@Neo4JLinkElement(label="AgentGeoLink")
public class AgentGeoLink implements LinkI{
	
	@CsvBindByName(column = "agent_id")
	@Neo4JPropertyElement(key="agent_id",type=Neo4JType.TOSTRING)
	private String agentId;
	
	@CsvBindByName(column = "city")
	@Neo4JPropertyElement(key="city",type=Neo4JType.TOSTRING)
	private String city;
	
	@CsvBindByName(column = "size")
	@Neo4JPropertyElement(key="size",type=Neo4JType.TOINTEGER)
	private Integer size;
	
	public AgentGeoLink() {}
	
	public String getAgentId() {
		return this.agentId;
	}
	public String getCity() {
		return this.city;
	}
	public Integer getSize() {
		return this.size;
	}

}
