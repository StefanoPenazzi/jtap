package projects.CTAP.transport;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import config.CtapTransportLinkConfig;

public class TransitCTAPTransportLinkFactory1 extends AbstractCTAPTransportLinkFactory {

	@Override
	public double CTAPTransportCrossLinkValue(Object tlc_, Map<String, Object> inputData) {
		CtapTransportLinkConfig tlc = (CtapTransportLinkConfig)tlc_;
		
		Long CrossLinkValue = null;
		
		
		if(inputData.get("to_node_type").equals("air")){
			//TODO make the 100 be a function of the average transfer time between the rail and air node
			if(inputData.get("from_node_type").equals("rail")) {
				CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk() +
						(5400 * tlc.getMvWaitingTime()) + 25); // the 25 at the end is a dummy value for the TooShortPenalty for having a too short connection
			/*if(inputData.get("from_node_type").equals("rail")) {
				CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk() +
						(5400 * tlc.getMvWaitingTime()) + extraWaitDueToSchedule * tlc.getMvWaitingTime + HowMuchShorterTheWaitTimeISDueToScheulde*tlc.gettrain2plane); */ 
			} else if(inputData.get("from_node_type").equals("road")){
				CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk() +
						(5400 * tlc.getMvWaitingTime()));
			//TODO make the cross links from cities to airports dependent on size of city? 
			} else if(inputData.get("from_node_type").equals("city")){
				CrossLinkValue = (long) (((Long) inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getBus() +
						(5400 * tlc.getMvWaitingTime()));
			} else {
				System.out.println("WARNING: unknown from node for an airport crosslink, SETTING VALUE TO NULL");
			}
		} else {
			CrossLinkValue = (long) (((Long)inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk());
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
	public double CTAPTransportAirLinkValue(Object tlc, Map<String, Object> inputData) {
		return Double.MAX_VALUE;
	}

}
