package data.external.rail.gtfs;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public final class Transfer {
	
	@CsvBindByName(column = "from_stop_id")
	@CsvBindByPosition(position = 0)
	private String fromStopId;
	@CsvBindByName(column = "to_stop_id")
	@CsvBindByPosition(position = 1)
	private String toStopId;
	@CsvBindByName(column = "transfer_type")
	@CsvBindByPosition(position = 2)
	private String transferType;
	@CsvBindByName(column = "min_transfer_time")
	@CsvBindByPosition(position = 3)
	private Integer minTransferTime;
	@CsvBindByName(column = "from_route_id")
	@CsvBindByPosition(position = 4)
	private String fromRouteId;
	@CsvBindByName(column = "to_route_id")
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
