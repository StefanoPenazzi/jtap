package core.graph.routing;

import java.util.List;

public class PathRes {
	private final double totalCost;
	private final List<Long> path;
	public PathRes(double totalCost,List<Long> path) {
		this.totalCost = totalCost;
		this.path = path;
	}
	
	public double getTotalCost() {
		return totalCost;
	}
	public List<Long> getPath() {
		return path;
	}

}
