package projects.CTAP.graphElements;

import com.opencsv.bean.CsvBindByName;

import core.graph.Activity.ActivityLocLink;
import core.graph.annotations.GraphElementAnnotation.Neo4JLinkElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

@Neo4JLinkElement(label="ActivityLocLink")
public class ActivityCityLink extends ActivityLocLink {
	
	@CsvBindByName(column = "activity_cost")
	@Neo4JPropertyElement(key="activity_cost",type=Neo4JType.TOINTEGER)
	private Float activityCost;
	
	public ActivityCityLink () {
		
	}
	
	ActivityCityLink (Long activityId,Long cityId,Float activityCost) {
		super(activityId,cityId);
		this.activityCost = activityCost; 
	}
}