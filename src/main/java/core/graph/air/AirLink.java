package core.graph.air;

import java.time.LocalTime;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import core.graph.LinkI;
import core.graph.annotations.GraphElementAnnotation.Neo4JLinkElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

@Neo4JLinkElement(label="AirLink")
public class AirLink implements LinkI{
	
	@CsvBindByName(column = "id_from")
	@CsvBindByPosition(position = 0)
	@Neo4JPropertyElement(key="id_from",type=Neo4JType.TOINTEGER)
	private Long idFrom;
	@CsvBindByName(column = "id_to")
	@CsvBindByPosition(position = 1)
	@Neo4JPropertyElement(key="id_to",type=Neo4JType.TOINTEGER)
	private Long idTo;
	@CsvBindByName(column = "avg_travel_time")
	@CsvBindByPosition(position = 2)
	@Neo4JPropertyElement(key="avg_travel_time",type=Neo4JType.TOFLOAT)
	private Double avgTravelTime;
	@CsvBindByName(column = "connections_per_day")
	@CsvBindByPosition(position = 3)
	@Neo4JPropertyElement(key="connections_per_day",type=Neo4JType.TOINTEGER)
	private Integer connectionsPerDay;
	@CsvBindByName(column = "first_dep_time")
	@CsvBindByPosition(position = 4)
	@Neo4JPropertyElement(key="first_dep_time",type=Neo4JType.TOSTRING)
	private LocalTime firstDepartureTime;
	@CsvBindByName(column = "last_dep_time")
	@CsvBindByPosition(position = 5)
	@Neo4JPropertyElement(key="last_dep_time",type=Neo4JType.TOSTRING)
	private LocalTime lastDepartureTime;
	
	
	public AirLink() {}
	
	public AirLink( Long from,Long to,Double avgTravelTime,
			Integer connectionsPerDay,LocalTime firstDepartureTime,
			LocalTime lastDepartureTime) {
		this.idFrom = from;
		this.idTo = to;
		this.avgTravelTime = avgTravelTime;
		this.connectionsPerDay = connectionsPerDay;
		this.firstDepartureTime = firstDepartureTime;
		this.lastDepartureTime = lastDepartureTime;
	}
	
	public Long getIdFrom() {
		return this.idFrom;
	}
	
	public Long getIdTo(){
		return this.idTo;
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
}
