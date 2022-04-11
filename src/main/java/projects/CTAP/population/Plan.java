package projects.CTAP.population;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import controller.Controller;

public class Plan {
	
	private final double value;
	private final Double[] ts;
	private final Double[] te;
	private final int[] locations;
	private final int[] activities;
 	
	public Plan(int[] locations,int[] activities,double[] activitiesTime, Double value) {
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
	
	public Plan(int[] locations,int[] activities,Double[] ts,Double[] te, double value) {
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
	
	public int[] getActivities() {
		return activities;
	}
	
	public int[] getLocations() {
		return locations;
	}
}
