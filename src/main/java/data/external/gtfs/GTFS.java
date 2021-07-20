package data.external.gtfs;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.inject.Inject;

import data.utils.io.CSV;

public final class GTFS {
	
	private List<Route> routes;
	private List<Stop> stops;
	private List<StopTime> stopTimes;
	private List<Transfer> transfers;
	private List<Trip> trips;
	
	private final String ROUTESFILE = "routes.csv"; 
	private final String STOPSFILE = "routes.csv";
	private final String STOPSTIMESFILE = "routes.csv";
	private final String TRANSFERSFILE = "routes.csv";
	private final String TRIPSFILE = "routes.csv";
	
	private final String gtfsDirectory;
	
	@Inject
	public GTFS() throws IOException{
		
		gtfsDirectory = "";
		initialize();
	} 
	
	private void initialize() throws IOException {
		this.routes = CSV.getList(new File(gtfsDirectory+ROUTESFILE),Route.class,1);
		this.stops = CSV.getList(new File(gtfsDirectory+STOPSFILE),Stop.class,1);
		this.stopTimes = CSV.getList(new File(gtfsDirectory+STOPSTIMESFILE),StopTime.class,1);
		this.transfers = CSV.getList(new File(gtfsDirectory+TRANSFERSFILE),Transfer.class,1);
		this.trips = CSV.getList(new File(gtfsDirectory+TRIPSFILE),Trip.class,1);
	}
	
	

}
