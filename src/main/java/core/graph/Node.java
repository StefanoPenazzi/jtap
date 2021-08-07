package core.graph;

public enum Node implements GraphElement {
	
	ROUTABLE("RoutableNode");

	private final String nodeType;
	
	Node(String nodeType){
		this.nodeType = nodeType;
	}
	
	@Override
	public String getKey() {
		return "NodeType";
	}

	@Override
	public String getValue() {
		return this.nodeType;
	}

}
