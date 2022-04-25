package core.graph.air;

import com.opencsv.bean.CsvBindByName;

import core.graph.NodeGeoI;
import core.graph.annotations.GraphElementAnnotation.Neo4JNodeElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

@Neo4JNodeElement(labels={"AirNode"})
public final class AirNode implements NodeGeoI {
	
	@CsvBindByName(column = "id")
	@Neo4JPropertyElement(key="id",type=Neo4JType.TOINTEGER)
	private Long id;
	@CsvBindByName(column = "name")
	@Neo4JPropertyElement(key="name",type=Neo4JType.TOSTRING)
	private String name;
	@CsvBindByName(column = "lat")
	@Neo4JPropertyElement(key="lat",type=Neo4JType.TOFLOAT)
	private Double lat;
	@CsvBindByName(column = "lon")
	@Neo4JPropertyElement(key="lon",type=Neo4JType.TOFLOAT)
	private Double lon;
	
	
	@Override
	public Long getId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public Double getLat() {
		return this.lat;
	}
	public Double getLon() {
		return this.lon;
	}
}
