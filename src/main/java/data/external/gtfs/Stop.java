package data.external.gtfs;

import com.opencsv.bean.CsvBindByPosition;

public final class Stop {
	
	@CsvBindByPosition(position = 0)
	private String stopId;
	@CsvBindByPosition(position = 1)
	private String stopName;
	@CsvBindByPosition(position = 2)
	private String stopDesc;
	@CsvBindByPosition(position = 3)
	private Double stopLat;
	@CsvBindByPosition(position = 4)
	private Double stopLon;
	@CsvBindByPosition(position = 5)
	private String zoneId;
	@CsvBindByPosition(position = 6)
	private String stopUrl;
	@CsvBindByPosition(position = 7)
	private Boolean locationType;
	@CsvBindByPosition(position = 8)
	private String parentStation;
	
	
	
	public String getStopId() {
		return this.stopId;
	}
	public String getStopName() {
		return this.stopName;
	}
	public String getStopDesc() {
		return this.stopDesc;
	}
	public Double getStopLat() {
		return this.stopLat;
	}
	public Double getStopLon() {
		return this.stopLon;
	}
	public String getZoneId() {
		return this.zoneId;
	}
	public String getStopUrl() {
		return this.stopUrl;
	}
	public Boolean getLocationType() {
		return this.locationType;
	}
	public String getParentStation() {
		return this.parentStation;
	}
	
}
