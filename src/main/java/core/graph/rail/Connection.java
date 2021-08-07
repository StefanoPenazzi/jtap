package core.graph.rail;

import java.time.LocalTime;


public final class Connection {
	
	private final String from;
	private final String to;
	private final LocalTime departureTime;
	private final Integer duration;
	
	public Connection(String from,String to,LocalTime departureTime,Integer duration) {
		this.from = from;
		this.to = to;
		this.departureTime = departureTime;
		this.duration = duration;
	}
	
	public String getFrom() {
		return this.from;
	}
	
	public String getTo(){
		return this.to; 
	}
	
	public LocalTime getDepartureTime(){
		return this.departureTime; 
	}
	
	public Integer getDuration(){
		return this.duration; 
	}

}
