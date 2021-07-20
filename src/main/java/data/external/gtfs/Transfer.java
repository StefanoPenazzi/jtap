package data.external.gtfs;

import com.opencsv.bean.CsvBindByPosition;

public final class Transfer {
	
	@CsvBindByPosition(position = 0)
	private String fromStopId;
	@CsvBindByPosition(position = 1)
	private String toStopId;
	@CsvBindByPosition(position = 2)
	private String transferType;
	@CsvBindByPosition(position = 3)
	private Integer minTransferTime;
	@CsvBindByPosition(position = 4)
	private String fromRouteId;
	@CsvBindByPosition(position = 5)
	private String toRouteId;
	
	
	public String getFromStopId () {
		return this.fromStopId;
	}
	
	public String getToStopId () {
		return this.toStopId;
	}
	
	public String getTransferType () {
		return this.transferType;
	}
	
	public Integer getMinTransferTime () {
		return this.minTransferTime;
	}
	
	public String getFromRouteId () {
		return this.fromRouteId;
	}
	
	public String getToRouteId () {
		return this.toRouteId;
	}

}
