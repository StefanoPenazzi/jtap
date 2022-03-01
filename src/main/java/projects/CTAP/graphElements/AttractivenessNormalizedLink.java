package projects.CTAP.graphElements;

import com.opencsv.bean.CsvBindByName;

import core.graph.LinkI;
import core.graph.annotations.GraphElementAnnotation.Neo4JLinkElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

@Neo4JLinkElement(label="AttractivenessNormalizedLink")
public class AttractivenessNormalizedLink implements LinkI {
	
	@CsvBindByName(column = "agent_id")
	@Neo4JPropertyElement(key="agent_id",type=Neo4JType.TOINTEGER)
	private Long agentId;
	
	@CsvBindByName(column = "activity_id")
	@Neo4JPropertyElement(key="activity_id",type=Neo4JType.TOINTEGER)
	private Long activityId;
	
	@CsvBindByName(column = "city")
	@Neo4JPropertyElement(key="city",type=Neo4JType.TOINTEGER)
	private String cityId;
	
	@CsvBindByName(column = "time")
	@Neo4JPropertyElement(key="time",type=Neo4JType.TOFLOAT)
	private Double time;
	
	@CsvBindByName(column = "attractiveness")
	@Neo4JPropertyElement(key="attractiveness",type=Neo4JType.TOFLOAT)
	private Double attractiveness;
	
	public AttractivenessNormalizedLink() {
		
	}
	
	public AttractivenessNormalizedLink(Long agentId,String cityId,Long activityId,Double time,Double attractiveness) {
		this.agentId = agentId;
		this.activityId = activityId;
		this.cityId = cityId; 
		this.time = time;
		this.attractiveness = attractiveness;
	}
	
	public String getCity() {
		return cityId;
	}
	
	public void setCity(String cityId) {
		this.cityId = cityId;
	}
	
	public Long getAgentId() {
		return agentId;
	}
	
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	
	public Double getTime() {
		return time;
	}
	
	public void setTime(Double time) {
		this.time = time;
	}
	
	public Double getAttractiveness() {
		return attractiveness;
	}
	
	public void setAttractiveness(Double attractiveness) {
		this.attractiveness = attractiveness;
	}
	
	public Long getActivityId() {
		return activityId;
	}
	
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

}
