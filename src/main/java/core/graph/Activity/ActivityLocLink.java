package core.graph.Activity;

import com.opencsv.bean.CsvBindByName;

import core.graph.LinkI;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

public class ActivityLocLink implements LinkI {
	
	@CsvBindByName(column = "activity_id")
	@Neo4JPropertyElement(key="activity_id",type=Neo4JType.TOINTEGER)
	private Long activityId;
	
	@CsvBindByName(column = "city_id")
	@Neo4JPropertyElement(key="city_id",type=Neo4JType.TOINTEGER)
	private Long cityId;
	
	
	public ActivityLocLink () {
		
	}
	
	protected ActivityLocLink (Long activityId,Long cityId) {
		this.activityId = activityId;
		this.cityId = cityId;
	}
}