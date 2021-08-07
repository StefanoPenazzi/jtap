package core.graph.rail;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class RailTransferLink {
	
	@CsvBindByName(column = "stop_from")
	@CsvBindByPosition(position = 0)
	private String from;
	@CsvBindByName(column = "stop_to")
	@CsvBindByPosition(position = 1)
	private String to;
	@CsvBindByName(column = "avg_travel_time")
	@CsvBindByPosition(position = 2)
	private Double avgTravelTime;
	
	
	public RailTransferLink() {}
	
	public RailTransferLink( String from,String to,Double avgTravelTime) {
		this.from = from;
		this.to = to;
		this.avgTravelTime = avgTravelTime;
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
}
