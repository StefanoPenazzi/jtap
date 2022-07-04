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
	
	@CsvBindByName(column = "from_node_type")
	@CsvBindByPosition(position = 1)
	@Neo4JPropertyElement(key="from_node_type",type=Neo4JType.TOSTRING)
	private String from_node_type;
	
	@CsvBindByName(column = "to")
	@CsvBindByPosition(position = 2)
	@Neo4JPropertyElement(key="to",type=Neo4JType.TOSTRING)
	private String to;
	
	@CsvBindByName(column = "to_node_type")
	@CsvBindByPosition(position = 3)
	@Neo4JPropertyElement(key="to_node_type",type=Neo4JType.TOSTRING)
	private String to_node_type;
	
	@CsvBindByName(column = "distance")
	@CsvBindByPosition(position = 4)
	@Neo4JPropertyElement(key="distance",type=Neo4JType.TOINTEGER)
	private Integer distance;
	
	@CsvBindByName(column = "avg_travel_time")
	@CsvBindByPosition(position = 5)
	@Neo4JPropertyElement(key="avg_travel_time",type=Neo4JType.TOINTEGER)
	private Integer avgTravelTime;
	

	public CrossLink() {}
	
	public CrossLink( String from, String from_node_type, String to, String to_node_type, Integer distance,Integer avgTravelTime) {
		this.from = from;
		this.from_node_type = from_node_type;
		this.to = to;
		this.to_node_type = to_node_type;
		this.distance = distance;
		this.avgTravelTime = avgTravelTime;
	}
	
	public String getFrom() {
		return this.from;
	}
	
	public String getFromNodeType() {
		return this.from_node_type;
	}
	
	public String getTo(){
		return this.to; 
	}
	
	public String getToNodeType(){
		return this.to_node_type; 
	}
	
	public Integer getDistance(){
		return this.distance; 
	}
	
	public Integer getAvgTravelTime(){
		return this.avgTravelTime; 
	}
}
