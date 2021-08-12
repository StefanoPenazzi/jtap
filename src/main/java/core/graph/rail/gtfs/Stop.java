package core.graph.rail.gtfs;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import core.graph.NodeGeoI;
import core.graph.annotations.GraphElementAnnotation.Neo4JNodeElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

@Neo4JNodeElement(labels={"RailNode","RoutableNode"})
public final class Stop implements NodeGeoI {
	
	@CsvBindByName(column = "stop_id")
	@Neo4JPropertyElement(key="id",type=Neo4JType.TOSTRING)
	private String stopId;
	@CsvBindByName(column = "stop_name")
	@Neo4JPropertyElement(key="name",type=Neo4JType.TOSTRING)
	private String stopName;
	@CsvBindByName(column = "stop_desc")
	@Neo4JPropertyElement(key="stop_desc",type=Neo4JType.TOSTRING)
	private String stopDesc;
	@CsvBindByName(column = "stop_lat")
	@Neo4JPropertyElement(key="lat",type=Neo4JType.TOFLOAT)
	private Double stopLat;
	@CsvBindByName(column = "stop_lon")
	@Neo4JPropertyElement(key="lon",type=Neo4JType.TOFLOAT)
	private Double stopLon;
	@CsvBindByName(column = "zone_id")
	@Neo4JPropertyElement(key="zone_id",type=Neo4JType.TOSTRING)
	private String zoneId;
	@CsvBindByName(column = "stop_url")
	@Neo4JPropertyElement(key="stop_url",type=Neo4JType.TOSTRING)
	private String stopUrl;
	@CsvBindByName(column = "location_type")
	@Neo4JPropertyElement(key="location_type",type=Neo4JType.TOBOOLEAN)
	private Boolean locationType;
	@CsvBindByName(column = "parent_station")
	@Neo4JPropertyElement(key="parent_station",type=Neo4JType.TOSTRING)
	private String parentStation;
	
	
	
	public String getId() {
		return this.stopId;
	}
	public String getStopName() {
		return this.stopName;
	}
	public String getStopDesc() {
		return this.stopDesc;
	}
	public Double getLat() {
		return this.stopLat;
	}
	public Double getLon() {
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
