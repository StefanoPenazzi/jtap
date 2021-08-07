package core.graph.rail.gtfs;

import java.time.LocalTime;
import java.util.Date;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.CsvDate;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public final class StopTime {
	
	@CsvBindByPosition(position = 0)
	private String tripId;
	
	@CsvCustomBindByPosition(position = 1, converter = LocalTimeConverterGTFS.class)
	private LocalTime arrivalTime;
	
	@CsvCustomBindByPosition(position = 2, converter = LocalTimeConverterGTFS.class)
	private LocalTime departureTime;
	@CsvBindByPosition(position = 3)
	private String stopId;
	@CsvBindByPosition(position = 4)
	private Byte stopSequence;
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
	public LocalTime getArrivalTime() {
		return this.arrivalTime;
	}
	public LocalTime getDepartureTime() {
		return this.departureTime;
	}
	public String getStopId() {
		return this.stopId;
	}
	public Byte getStopSequence() {
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
