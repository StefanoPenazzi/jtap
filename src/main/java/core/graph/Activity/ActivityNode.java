package core.graph.Activity;

import com.opencsv.bean.CsvBindByName;

import core.graph.NodeI;
import core.graph.annotations.GraphElementAnnotation.Neo4JNodeElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

@Neo4JNodeElement(labels={"ActivityNode"})
public class ActivityNode implements NodeI {
	
	@CsvBindByName(column = "activity_id")
	@Neo4JPropertyElement(key="activity_id",type=Neo4JType.TOINTEGER)
	private Integer activtiyId;
	
	@CsvBindByName(column = "activity_name")
	@Neo4JPropertyElement(key="activity_name",type=Neo4JType.TOSTRING)
	private String activtiyName;
	
	public Integer getActivityId() {
		return this.activtiyId;
	}
	
	public String getActivityName() {
		return this.activtiyName;
	}

}
