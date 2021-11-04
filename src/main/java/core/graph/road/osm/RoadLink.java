package core.graph.road.osm;

import core.graph.LinkI;
import core.graph.annotations.GraphElementAnnotation.Neo4JLinkElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

@Neo4JLinkElement(label="RoadLink")
public class RoadLink implements LinkI{
	
	@Neo4JPropertyElement(key="avg_travel_time",type=Neo4JType.TOFLOAT)
	private Double avgTravelTime;
	
	public RoadLink() {
		
	}
	
	public Double getAvgTravelTime(){
		return this.avgTravelTime; 
	}

}
