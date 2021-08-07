package core.graph.rail.gtfs;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public final class Stop {
	
	@CsvBindByName(column = "stop_id")
	@CsvBindByPosition(position = 0)
	private String stopId;
	@CsvBindByName(column = "stop_name")
	@CsvBindByPosition(position = 1)
	private String stopName;
	@CsvBindByName(column = "stop_desc")
	@CsvBindByPosition(position = 2)
	private String stopDesc;
	@CsvBindByName(column = "stop_lat")
	@CsvBindByPosition(position = 3)
	private Double stopLat;
	@CsvBindByName(column = "stop_lon")
	@CsvBindByPosition(position = 4)
	private Double stopLon;
	@CsvBindByName(column = "zone_id")
	@CsvBindByPosition(position = 5)
	private String zoneId;
	@CsvBindByName(column = "stop_url")
	@CsvBindByPosition(position = 6)
	private String stopUrl;
	@CsvBindByName(column = "location_type")
	@CsvBindByPosition(position = 7)
	private Boolean locationType;
	@CsvBindByName(column = "parent_station")
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
