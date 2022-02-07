package core.graph.road.osm;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import core.graph.NodeGeoI;
import core.graph.annotations.GraphElementAnnotation.Neo4JNodeElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

@Neo4JNodeElement(labels={"Intersection"})
public class Intersection implements NodeGeoI {
	
	@CsvBindByName(column = "node_osm_id")
	@CsvBindByPosition(position = 0)
	@Neo4JPropertyElement(key="node_osm_id",type=Neo4JType.TOINTEGER)
	private String stopId;
	@CsvBindByName(column = "lat")
	@CsvBindByPosition(position = 1)
	@Neo4JPropertyElement(key="lat",type=Neo4JType.TOFLOAT)
	private Double lat;
	@CsvBindByName(column = "lon")
	@CsvBindByPosition(position = 2)
	@Neo4JPropertyElement(key="lon",type=Neo4JType.TOFLOAT)
	private Double lon;
	@Override
	public Double getLat() {
		return this.lat;
	}
	@Override
	public Double getLon() {
		return this.lon;
	}

}
