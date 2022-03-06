package projects.CTAP.dataset;

import java.util.List;

import core.dataset.IndexI;

public class CitiesDsIndex implements IndexI<Long> {

	private List<Long> index;
	private String id = "CitiesDsIndex";
	private String description = "";
	
	public CitiesDsIndex(List<Long> index) {
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

