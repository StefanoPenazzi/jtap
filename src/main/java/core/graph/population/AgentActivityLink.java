package core.graph.population;

import com.opencsv.bean.CsvBindByName;

import core.graph.LinkI;
import core.graph.annotations.GraphElementAnnotation.Neo4JLinkElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

@Neo4JLinkElement(label="ActivityAgentLink")
public class AgentActivityLink implements LinkI {
	
	@CsvBindByName(column = "agent_id")
	@Neo4JPropertyElement(key="agent_id",type=Neo4JType.TOINTEGER)
	private Integer agentId;
	
	@CsvBindByName(column = "activity_id")
	@Neo4JPropertyElement(key="activity_id",type=Neo4JType.TOINTEGER)
	private Integer activityId;
	
	@CsvBindByName(column = "perc_of_time_target")
	@Neo4JPropertyElement(key="perc_of_time_target",type=Neo4JType.TOFLOAT)
	private Double ptt;
	
	@CsvBindByName(column = "duration_discomfort")
	@Neo4JPropertyElement(key="duration_discomfort",type=Neo4JType.TOFLOAT)
	private Double durationDiscomfort;
	
	public Integer getAgentId() {
		return this.agentId;
	}
	public Integer getActivityId() {
		return this.activityId;
	}
	public Double getPTT() {
		return this.ptt;
	}
	public Double getDurationDiscomfort() {
		return this.durationDiscomfort;
	}
}
