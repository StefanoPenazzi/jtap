package projects.CTAP.graphElements;

import com.opencsv.bean.CsvBindByName;

import core.graph.annotations.GraphElementAnnotation.Neo4JNodeElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

@Neo4JNodeElement(labels={"CityFacStatNode"})
public class CTAPCityStatNode {
	
	@Neo4JPropertyElement(key="city",type=Neo4JType.TOSTRING)
	private String city;
	
	public String getCity() {
		return this.city;
	}
	
	
}
