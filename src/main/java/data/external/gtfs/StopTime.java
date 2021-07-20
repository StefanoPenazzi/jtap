package data.external.gtfs;

import java.util.Date;

import com.opencsv.bean.CsvBindByPosition;

public final class StopTime {
	
	@CsvBindByPosition(position = 0)
	private String tripId;
	@CsvBindByPosition(position = 1)
	private Date arrivalTime;
	@CsvBindByPosition(position = 2)
	private Date departureTime;
	@CsvBindByPosition(position = 3)
	private String stopId;
	@CsvBindByPosition(position = 4)
	private Boolean stopSequence;
	@CsvBindByPosition(position = 5)
	private Boolean stopHeadsign;
	@CsvBindByPosition(position = 6)
	private Boolean pickupType;
	@CsvBindByPosition(position = 7)
	private Boolean dropOffType;
	@CsvBindByPosition(position = 8)
	private Integer shapeDistTraveled;
	
	
	public String getTripId() {
		return this.tripId;
	}
	public Date getArrivalTime() {
		return this.arrivalTime;
	}
	public Date getDepartureTime() {
		return this.departureTime;
	}
	public String getStopId() {
		return this.stopId;
	}
	public Boolean getStopSequence() {
		return this.stopSequence;
	}
	public Boolean getStopHeadsign() {
		return this.stopHeadsign;
	}
	public Boolean getPickupType() {
		return this.pickupType;
	}
	public Boolean getDropOffType() {
		return this.dropOffType;
	}
	public Integer getShapeDistTraveled() {
		return this.shapeDistTraveled;
	}

}
