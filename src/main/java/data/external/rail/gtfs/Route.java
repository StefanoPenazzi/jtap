package data.external.rail.gtfs;

import com.opencsv.bean.CsvBindByPosition;

public final class Route {
	
	@CsvBindByPosition(position = 0)
	private String routeId;
	@CsvBindByPosition(position = 1)
	private String agencyId;
	@CsvBindByPosition(position = 2)
	private String routeShortName;
	@CsvBindByPosition(position = 3)
	private String routeLongName;
	@CsvBindByPosition(position = 4)
	private Integer routeDesc;
	@CsvBindByPosition(position = 5)
	private String routeType;
	@CsvBindByPosition(position = 6)
	private String routeUrl;
	@CsvBindByPosition(position = 7)
	private String routeColor;
	@CsvBindByPosition(position = 8)
	private String routeTextColor;
	
	
	
	public String getRouteId() {
		return this.routeId;
	}
	public String getAgencyId() {
		return this.agencyId;
	}
	public String getRouteShortName() {
		return this.routeShortName;
	}
	public String getRouteLongName() {
		return this.routeLongName;
	}
	public Integer getRouteDesc() {
		return this.routeDesc;
	}
	public String getRouteType() {
		return this.routeType;
	}
	public String getRouteUrl() {
		return this.routeUrl;
	}
	public String getRouteColor() {
		return this.routeColor;
	}
	public String getRouteTextColor() {
		return this.routeTextColor;
	}
	
	

}
