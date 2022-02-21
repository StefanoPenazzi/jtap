package projects.CTAP.attractiveness;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;


public class AttractivenessAgentCTAP {
	
	private Integer agentId;
	private List<AttractivenessActivityCTAP> activities;
	
	public List<AttractivenessActivityCTAP> getActivities() {
		return activities;
	}

	@SuppressWarnings("unchecked")
	@JsonProperty("activities")
	public void setActivities(List<AttractivenessActivityCTAP> activities) {
		this.activities = activities;
	}
	
	public Integer getAgentId() {
		return agentId;
	}
	
	@SuppressWarnings("unchecked")
	@JsonProperty("agent_id")
	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}
}
