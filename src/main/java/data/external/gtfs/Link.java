package data.external.gtfs;

import java.time.LocalTime;

public class Link {
	
	private final String from;
	private final String to;
	private final Double avgTravelTime;
	private final Integer connectionsPerDay;
	private final LocalTime firstDepartureTime;
	private final LocalTime lastDepartureTime;
	
	
	public Link( String from,String to,Double avgTravelTime,
			Integer connectionsPerDay,LocalTime firstDepartureTime,
			LocalTime lastDepartureTime) {
		this.from = from;
		this.to = to;
		this.avgTravelTime = avgTravelTime;
		this.connectionsPerDay = connectionsPerDay;
		this.firstDepartureTime = firstDepartureTime;
		this.lastDepartureTime = lastDepartureTime;
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

}
