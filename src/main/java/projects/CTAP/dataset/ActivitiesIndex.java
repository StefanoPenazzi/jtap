package projects.CTAP.dataset;

import java.util.List;

import core.dataset.IndexI;

public class ActivitiesIndex implements IndexI<Long> {

	private List<Long> index;
	private String id = "ActivitiesIndex";
	private String description = "";
	
	public ActivitiesIndex(List<Long> index) {
		this.index = index;
	}
	
	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public List<Long> getIndex() {
		return this.index;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

}

