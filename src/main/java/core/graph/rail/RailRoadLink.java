package core.graph.rail;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class RailRoadLink {
	
	@CsvBindByName(column = "stop_from")
	@CsvBindByPosition(position = 0)
	private String from;
	@CsvBindByName(column = "road_to")
	@CsvBindByPosition(position = 1)
	private String to;
	@CsvBindByName(column = "distance")
	@CsvBindByPosition(position = 2)
	private Integer distance;
	
	
	public RailRoadLink() {}
	
	public RailRoadLink( String from,String to,Integer distance) {
		this.from = from;
		this.to = to;
		this.distance = distance;
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

}
