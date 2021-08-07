package core.graph.rail;

import java.time.LocalTime;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class RailLink {
	
	@CsvBindByName(column = "stop_from")
	@CsvBindByPosition(position = 0)
	private String from;
	@CsvBindByName(column = "stop_to")
	@CsvBindByPosition(position = 1)
	private String to;
	@CsvBindByName(column = "avg_travel_time")
	@CsvBindByPosition(position = 2)
	private Double avgTravelTime;
	@CsvBindByName(column = "connections_per_day")
	@CsvBindByPosition(position = 3)
	private Integer connectionsPerDay;
	@CsvBindByName(column = "first_dep_time")
	@CsvBindByPosition(position = 4)
	private LocalTime firstDepartureTime;
	@CsvBindByName(column = "last_dep_time")
	@CsvBindByPosition(position = 5)
	private LocalTime lastDepartureTime;
	@CsvBindByName(column = "type")
	@CsvBindByPosition(position = 6)
	private String type;
	
	public RailLink() {}
	
	public RailLink( String from,String to,Double avgTravelTime,
			Integer connectionsPerDay,LocalTime firstDepartureTime,
			LocalTime lastDepartureTime,String type) {
		this.from = from;
		this.to = to;
		this.avgTravelTime = avgTravelTime;
		this.connectionsPerDay = connectionsPerDay;
		this.firstDepartureTime = firstDepartureTime;
		this.lastDepartureTime = lastDepartureTime;
		this.type = type;
	}
	
	public String getFrom() {
		return this.from;
	}
	
	public String getTo(){
		return this.to; 
	}
	
	public Double getAvgTravelTime(){
		return this.avgTravelTime; 
	}
	
	public Integer getConnectionsPerDay(){
		return this.connectionsPerDay; 
	}
	
	public LocalTime getFirstDepartureTime(){
		return this.firstDepartureTime; 
	}
	
	public LocalTime getLastDepartureTime(){
		return this.lastDepartureTime; 
	}
	
	public String getType(){
		return this.type; 
	}

}
