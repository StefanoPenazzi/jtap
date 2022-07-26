package projects.CTAP.transport;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import config.CtapTransportLinkConfig;

public class TransitCTAPTransportLinkFactory1 extends AbstractCTAPTransportLinkFactory {
	
	//NOTE: MvTransferTime is the monetary value of TRAVEL TIME not transfer time! this was misnamed, and we haven't had time to refactor it.
	// to specify the value of waiting time, you need to use getMvWaitingTime()
	
	// STUFF FOR CROSS AIR-RAIL AND RAIL-AIR CROSS LINKS
	//TODO Get this to be imported from a file
	//TODO get this to be airport-dependant. Currently we are applying the same transfer time information for ALL airports! The current values are for Madrid!! 
	
	//Switch between BEFORE and AFTER optimization
	final private boolean beforeSchOpt = true;
	
	//Air-Rail Coordination parameters
	
	final private double tooShortTransferVOTFactor = 1.5; //this is a multiplier to the waiting time VOT, to represent the assumption that 
	//passengers are far more stressed by the risk of missing their connection than they are annoyed at waiting for a long time
	//This tooShortTransferVoTFactor will likely need to be calibrated.
	
	//NOTE FOR FUTURE USERS:  
	//The best advice for future users of this model will be to try and get a study that attempts to 
	//quantify this effect through behavioral research, like a stated or revealed preference survey!! 
	
	final private double idealConnectionTimeRoadToAir = 90 * 60;
	final private double idealConnectionTimeAirToRoad = 60 * 60; 
	
	//Air-Rail Coordination variables
	
	// VALUES BEFORE SCHEDULE OPTIMIZATION
	
	final private double idealConnectionTimeRailToAir = 90 * 60; // ideal transfer time for rail to air connections. The assumption is that these are Schengen flights.
	final private double idealConnectionTimeAirToRail = 60 * 60; // ideal transfer time for rail to air connections. 
	
	// Numbers of flights in each direction (Rail to Air or Air to Rail) that have a transfer time less than or greater than the ideal BEFORE schedule optimization
	final private double numConnGreaterThan_OUT_beforeSchOpt = 2088; // number of rail to air connections with transfer times longer than
	//the idea transfer time before schedule optimization
	final private double numConnLessThan_OUT_beforeSchOpt = 748; // number of rail to air connections with transfer times shorter than
	//the idea transfer time before schedule optimization
	final private double numConnGreaterThan_IN_beforeSchOpt = 1599;
	final private double numConnLessThan_IN_beforeSchOpt = 560;
	
	final private double totalFlights_OUT_beforeSchOpt = numConnGreaterThan_OUT_beforeSchOpt + numConnLessThan_OUT_beforeSchOpt;
	final private double totalFlights_IN_beforeSchOpt = numConnGreaterThan_IN_beforeSchOpt + numConnLessThan_IN_beforeSchOpt;
	
	final private double percentGreater_OUT_beforeSchOpt = numConnGreaterThan_OUT_beforeSchOpt/totalFlights_OUT_beforeSchOpt;
	final private double percentLess_OUT_beforeSchOpt = numConnLessThan_OUT_beforeSchOpt/totalFlights_OUT_beforeSchOpt;
	final private double percentGreater_IN_beforeSchOpt = numConnGreaterThan_IN_beforeSchOpt/totalFlights_IN_beforeSchOpt;
	final private double percentLess_IN_beforeSchOpt = numConnLessThan_IN_beforeSchOpt/totalFlights_IN_beforeSchOpt;
	
	// Average extra wait time of "greater" transfer times, average "missing' transfer time of "lesser" transfer times BEFORE schedule optimization
	final private double averageGreaterWait_OUT_beforeSchOpt = 84 * 60; //assuming the times provided were in minutes
	final private double averageGreaterWait_IN_beforeSchOpt = 56 * 60;
	final private double averageMissingTime_OUT_beforeSchOpt = 27 * 60; //note that I am rounding to the nearest who minute, the input from the optimization is finer
	final private double averageMissingTime_IN_beforeSchOpt = 20 * 60;
	
	
	//VALUES AFTER SCHEDULE OPTIMIZATION
	
	// Numbers of flights in each direction (Rail to Air or Air to Rail) that have a transfer time less than or greater than the ideal AFTER schedule optimization
	final private double numConnGreaterThan_OUT_afterSchOpt = 2265;
	final private double numConnLessThan_OUT_afterSchOpt = 571; 
	final private double numConnGreaterThan_IN_afterSchOpt = 1493;
	final private double numConnLessThan_IN_afterSchOpt = 666;	
	
	final private double totalFlights_OUT_afterSchOpt = numConnGreaterThan_OUT_afterSchOpt + numConnLessThan_OUT_afterSchOpt;
	final private double totalFlights_IN_afterSchOpt = numConnGreaterThan_IN_afterSchOpt + numConnLessThan_IN_afterSchOpt;
	
	final private double percentGreater_OUT_afterSchOpt = numConnGreaterThan_OUT_afterSchOpt/totalFlights_OUT_afterSchOpt;
	final private double percentLess_OUT_afterSchOpt = numConnLessThan_OUT_afterSchOpt/totalFlights_OUT_afterSchOpt;
	final private double percentGreater_IN_afterSchOpt = numConnGreaterThan_IN_afterSchOpt/totalFlights_IN_afterSchOpt;
	final private double percentLess_IN_afterSchOpt = numConnLessThan_IN_afterSchOpt/totalFlights_IN_afterSchOpt;
	
	// Average extra wait time of "greater" transfer times, average "missing' transfer time of "lesser" transfer times AFTER schedule optimization
	final private double averageGreaterWait_OUT_afterSchOpt = 79 * 60; //assuming the times provided were in minutes
	final private double averageGreaterWait_IN_afterSchOpt = 48 * 60;
	final private double averageMissingTime_OUT_afterSchOpt = 20 * 60; //note that I am rounding to the nearest who minute, the input from the optimization is finer
	final private double averageMissingTime_IN_afterSchOpt = 16 * 60;

	

	@Override
	public double CTAPTransportCrossLinkValue(Object tlc_, Map<String, Object> inputData) {
		CtapTransportLinkConfig tlc = (CtapTransportLinkConfig)tlc_;
		//NOTE: MvTransferTime is the monetary value of TRAVEL TIME not transfer time! this was misnamed, and we haven't had time to refactor it. 
		Long CrossLinkValue = null;
		
		//Check if before or after schedule optimization
		if (beforeSchOpt) {
			//Outer loop (ground to air VS air to ground)
			if(inputData.get("to_node_type").equals("air")){
				
				//Inner Loop 1 (which ground mode?)
				//From Rail
				if(inputData.get("from_node_type").equals("rail")) {
					CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk() +
							idealConnectionTimeRailToAir * tlc.getMvWaitingTime() + 
							percentGreater_OUT_beforeSchOpt * averageGreaterWait_OUT_beforeSchOpt * tlc.getMvWaitingTime() +
							percentLess_OUT_beforeSchOpt * averageMissingTime_OUT_beforeSchOpt * tlc.getMvWaitingTime() * tooShortTransferVOTFactor ); 
				//From Road	
				} else if(inputData.get("from_node_type").equals("road")){
					CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk() +
							(idealConnectionTimeRoadToAir * tlc.getMvWaitingTime()));
				
				//Directly from City
				//TODO make the cross links from cities to airports dependent on size of city? 
				} else if(inputData.get("from_node_type").equals("city")){
					CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getBus() +
							(idealConnectionTimeRoadToAir * tlc.getMvWaitingTime()));
				
					//From some other mode or node (should not happen!) but will treat like from Road	
				} else {
					System.out.println("WARNING: unknown from node for an airport crosslink, assuming road to air");
					CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk() +
							(idealConnectionTimeRoadToAir * tlc.getMvWaitingTime()));
				}
			
			} else if (inputData.get("from_node_type").equals("air")) {
				
				//Inner loop 2 (which ground mode?)
				//TO Rail
				if(inputData.get("to_node_type").equals("rail")) {
					CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk() +
							idealConnectionTimeAirToRail * tlc.getMvWaitingTime() + 
							percentGreater_IN_beforeSchOpt * averageGreaterWait_IN_beforeSchOpt * tlc.getMvWaitingTime() +
							percentLess_IN_beforeSchOpt * averageMissingTime_IN_beforeSchOpt * tlc.getMvWaitingTime() * tooShortTransferVOTFactor ); 
				//TO Road	
				} else if(inputData.get("to_node_type").equals("road")){
					CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk() +
							(idealConnectionTimeAirToRoad * tlc.getMvWaitingTime()));
				
				//Directly TO City
				//TODO make the cross links from cities to airports dependent on size of city? 
				} else if(inputData.get("to_node_type").equals("city")){
					CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getBus() +
							(idealConnectionTimeAirToRoad * tlc.getMvWaitingTime()));
				
					//To some other mode or node (should not happen!) but will treat like from Road	
				} else {
					System.out.println("WARNING: unknown from node for an airport crosslink, assuming air to road connection");
					CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk() +
							(idealConnectionTimeAirToRoad * tlc.getMvWaitingTime()));
				}
			
			} else { //else we are not concerned about this type of intermodal transfer at the moment, so default to walk time
				
				CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk());
			}
		} else { // NOW BEGINS THE STUFF FOR AFTER SCHEDULE OPTIMIZATION
			
			//Outer loop (ground to air VS air to ground)
			if(inputData.get("to_node_type").equals("air")){
				
				//Inner loop 1 (which ground mode?)
				//From Rail
				if(inputData.get("from_node_type").equals("rail")) {
					CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk() +
							idealConnectionTimeRailToAir * tlc.getMvWaitingTime() + 
							percentGreater_OUT_afterSchOpt * averageGreaterWait_OUT_afterSchOpt * tlc.getMvWaitingTime() +
							percentLess_OUT_afterSchOpt * averageMissingTime_OUT_afterSchOpt * tlc.getMvWaitingTime() * tooShortTransferVOTFactor ); 
				//From Road	
				} else if(inputData.get("from_node_type").equals("road")){
					CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk() +
							(idealConnectionTimeRoadToAir * tlc.getMvWaitingTime()));
				
				//Directly from City
				//TODO make the cross links from cities to airports dependent on size of city? 
				} else if(inputData.get("from_node_type").equals("city")){
					CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getBus() +
							(idealConnectionTimeRoadToAir * tlc.getMvWaitingTime()));
				
					//From some other mode or node (should not happen!) but will treat like from Road	
				} else {
					System.out.println("WARNING: unknown from node for an airport crosslink, assuming road to air");
					CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk() +
							(idealConnectionTimeRoadToAir * tlc.getMvWaitingTime()));
				}
			
			} else if (inputData.get("from_node_type").equals("air")) {
				
				//Inner loop 2 (which ground mode?)
				//TO Rail
				if(inputData.get("to_node_type").equals("rail")) {
					CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk() +
							idealConnectionTimeAirToRail * tlc.getMvWaitingTime() + 
							percentGreater_IN_afterSchOpt * averageGreaterWait_IN_afterSchOpt * tlc.getMvWaitingTime() +
							percentLess_IN_afterSchOpt * averageMissingTime_IN_afterSchOpt * tlc.getMvWaitingTime() * tooShortTransferVOTFactor ); 
				//TO Road	
				} else if(inputData.get("to_node_type").equals("road")){
					CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk() +
							(idealConnectionTimeAirToRoad * tlc.getMvWaitingTime()));
				
				//Directly TO City
				//TODO make the cross links from cities to airports dependent on size of city? 
				} else if(inputData.get("to_node_type").equals("city")){
					CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getBus() +
							(idealConnectionTimeAirToRoad * tlc.getMvWaitingTime()));
				
					//To some other mode or node (should not happen!) but will treat like from Road	
				} else {
					System.out.println("WARNING: unknown from node for an airport crosslink, assuming air to road connection");
					CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk() +
							(idealConnectionTimeAirToRoad * tlc.getMvWaitingTime()));
				}
			
			} else { //else we are not concerned about this type of intermodal transfer at the moment, so default to walk time
				
				CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk());
			}
			
			
		}
		return CrossLinkValue;
	}

	@Override
	public double CTAPTransportRoadLinkValue(Object tlc_, Map<String, Object> inputData) {
		CtapTransportLinkConfig tlc = (CtapTransportLinkConfig)tlc_;
		return Double.parseDouble(inputData.get("avg_travel_time").toString()) *tlc.getMvTransferTime().getCar();
	}

	@Override
	public double CTAPTransportRailLinkValue(Object tlc_, Map<String, Object> inputData) {
		CtapTransportLinkConfig tlc = (CtapTransportLinkConfig)tlc_;
		String ldt = (String)inputData.get("last_dep_time");
		String fdt = (String)inputData.get("first_dep_time");
		
		if(ldt == null) {
			ldt = "23:59";
		}
		if(fdt == null) {
			fdt = "00:00";
		}
		
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Date lastDepTime = null;
		Date firstDepTime = null;
		try {
			lastDepTime = dateFormat.parse(ldt);
			firstDepTime = dateFormat.parse(fdt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Long diff = (lastDepTime.getTime() - firstDepTime.getTime());
		Long connPerDay = (Long)inputData.get("connections_per_day");
		
		if(connPerDay == null) {
			connPerDay = 1L;
		}
		
		double reliability = ((diff.doubleValue()/connPerDay.doubleValue())/2)*tlc.getMvServicePerceivedReliability();
		
		return (double)inputData.get("avg_travel_time")*tlc.getMvTransferTime().getTrain() 
				+ (double)inputData.get("avg_travel_time")*30.55*tlc.getPriceXKm().getTrain()
				+ reliability
				+ 300 * tlc.getMvWaitingTime();
	}

	@Override
	public double CTAPTransportAirLinkValue(Object tlc_, Map<String, Object> inputData) {
		CtapTransportLinkConfig tlc = (CtapTransportLinkConfig)tlc_;
		return Double.parseDouble(inputData.get("avg_travel_time").toString()) *tlc.getMvTransferTime().getPlane();
	}

}
