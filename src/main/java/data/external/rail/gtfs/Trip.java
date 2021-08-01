package data.external.rail.gtfs;

import java.util.Date;

import com.opencsv.bean.CsvBindByPosition;

public final class Trip {
	
	@CsvBindByPosition(position = 0)
	private String routeId;
	@CsvBindByPosition(position = 1)
	private String serviceId;
	@CsvBindByPosition(position = 2)
	private String tripId;
	@CsvBindByPosition(position = 3)
	private Integer tripHeadsign;
	@CsvBindByPosition(position = 4)
	private Boolean directionId;
	@CsvBindByPosition(position = 5)
	private Integer blockId;
	@CsvBindByPosition(position = 6)
	private Integer shapeId;
	
	
	public String getRouteId() {
		return this.routeId;
	}
	public String getServiceId() {
		return this.serviceId;
	}
	public String getTripId() {
		return this.tripId;
	}
	public Integer getTripHeadsign() {
		return this.tripHeadsign;
	}
	public Boolean getDirectionId() {
		return this.directionId;
	}
	public Integer getBlockId() {
		return this.blockId;
	}
	public Integer getShapeId() {
		return this.shapeId;
	}
}
