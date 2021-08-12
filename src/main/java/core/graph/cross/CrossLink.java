package core.graph.cross;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import core.graph.LinkI;
import core.graph.annotations.GraphElementAnnotation.Neo4JLinkElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

@Neo4JLinkElement(label="CrossLink")
public class CrossLink implements LinkI {
	
	@CsvBindByName(column = "from")
	@CsvBindByPosition(position = 0)
	@Neo4JPropertyElement(key="from",type=Neo4JType.TOSTRING)
	private String from;
	@CsvBindByName(column = "to")
	@CsvBindByPosition(position = 1)
	@Neo4JPropertyElement(key="to",type=Neo4JType.TOSTRING)
	private String to;
	@CsvBindByName(column = "distance")
	@CsvBindByPosition(position = 2)
	@Neo4JPropertyElement(key="distance",type=Neo4JType.TOINTEGER)
	private Integer distance;
	@CsvBindByName(column = "avg_travel_time")
	@CsvBindByPosition(position = 3)
	@Neo4JPropertyElement(key="avg_travel_time",type=Neo4JType.TOINTEGER)
	private Integer avgTravelTime;
	
	
	public CrossLink() {}
	
	public CrossLink( String from,String to,Integer distance,Integer avgTravelTime) {
		this.from = from;
		this.to = to;
		this.distance = distance;
		this.avgTravelTime = avgTravelTime;
	}
	
	public String getFrom() {
		return this.from;
	}
	
	public String getTo(){
		return this.to; 
	}
	
	public Integer getDistance(){
		return this.distance; 
	}
	
	public Integer getAvgTravelTime(){
		return this.avgTravelTime; 
	}
}
