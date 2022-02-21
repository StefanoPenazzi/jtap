package projects.CTAP.attractiveness;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;


public class AttractivenessActivityCTAP {
	
	private String activity;
	private Map<String,Double> parameters;
	
	public String getActivity() {
		return activity;
	}
	
	@SuppressWarnings("unchecked")
	@JsonProperty("activity")
	public void setActivity(String activity) {
		this.activity = activity;
	}
	
	public Map<String, Double> getParameters() {
		return parameters;
	}
	
	@SuppressWarnings("unchecked")
	@JsonProperty("parameters")
	public void setParameters(Map<String,Double> parameters) {
		this.parameters = parameters;
	}
}
