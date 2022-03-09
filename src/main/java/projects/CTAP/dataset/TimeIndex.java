package projects.CTAP.dataset;

import java.util.List;

import core.dataset.IndexI;

public class TimeIndex implements IndexI<Long> {

	private List<Long> index;
	private String id = "TimeIndex";
	private String description = "";
	
	public TimeIndex(List<Long> index) {
		this.index = index;
	}
	
	public TimeIndex() {}
	
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
