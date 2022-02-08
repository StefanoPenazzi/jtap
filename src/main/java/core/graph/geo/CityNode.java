package core.graph.geo;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import core.graph.NodeGeoI;
import core.graph.annotations.GraphElementAnnotation.Neo4JNodeElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

@Neo4JNodeElement(labels={"CityNode"})
public class CityNode implements NodeGeoI{
	
	@CsvBindByName(column = "id")
	@Neo4JPropertyElement(key="id",type=Neo4JType.TOINTEGER)
	private Long id;
	
	@CsvBindByName(column = "city")
	@Neo4JPropertyElement(key="city",type=Neo4JType.TOSTRING)
	private String city;
	
	@CsvBindByName(column = "lat")
	@Neo4JPropertyElement(key="lat",type=Neo4JType.TOFLOAT)
	private Double lat;
	
	@CsvBindByName(column = "lng")
	@Neo4JPropertyElement(key="lon",type=Neo4JType.TOFLOAT)
	private Double lon;
	
	@CsvBindByName(column = "country")
	@Neo4JPropertyElement(key="country",type=Neo4JType.TOSTRING)
	private String country;
	
	@CsvBindByName(column = "population")
	@Neo4JPropertyElement(key="population",type=Neo4JType.TOINTEGER)
	private Integer population;
	
	public Long getId() {
		return this.id;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public Double getLat() {
		return this.lat;
	}
	
	public Double getLon() {
		return this.lon;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public Integer getPopulation() {
		return this.population;
	}
	

}
