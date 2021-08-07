package core.graph;

public enum Link implements GraphElement {
	
	ROUTABLE("RoutableLink");

	private final String linkType;
	
	Link(String linkType){
		this.linkType = linkType;
	}
	
	@Override
	public String getKey() {
		return "LinkType";
	}

	@Override
	public String getValue() {
		return this.linkType;
	}

}
