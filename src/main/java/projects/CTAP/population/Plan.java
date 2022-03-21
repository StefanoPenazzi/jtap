package projects.CTAP.population;

import java.util.stream.IntStream;

public class Plan {
	
	private final double value;
	private final Double[] ts;
	private final Double[] te;
	private final Long[] locations;
	private final Long[] activities;
 	
	public Plan(Long[] locations,Long[] activities,double[] activitiesTime, double value) {
		this.locations = locations;
		this.activities = activities;
		this.ts = IntStream.range(0,(int)(activitiesTime.length/2))
                .mapToObj(i -> activitiesTime[i])
                .toArray(Double[]::new);
		this.te = IntStream.range((int)(activitiesTime.length/2),activitiesTime.length)
                .mapToObj(i -> activitiesTime[i])
                .toArray(Double[]::new);
		this.value = value;
	}
	
	public Plan(Long[] locations,Long[] activities,Double[] ts,Double[] te, double value) {
		this.locations = locations;
		this.activities = activities;
		this.ts = ts;
		this.te = te;
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
	
	public Double[] getTe() {
		return te;
	}
	
	public Double[] getTs() {
		return ts;
	}

}
