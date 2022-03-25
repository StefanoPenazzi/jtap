package projects.CTAP.graphElements;

import com.opencsv.bean.CsvBindByName;

import core.graph.LinkI;
import core.graph.annotations.GraphElementAnnotation.Neo4JLinkElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

@Neo4JLinkElement(label="CTAPTransportLink")
public class CTAPTransportLink implements LinkI {
	
	@CsvBindByName(column = "from_id")
	@Neo4JPropertyElement(key="from_id",type=Neo4JType.TOINTEGER)
	private Long from_id;
	
	@CsvBindByName(column = "to_id")
	@Neo4JPropertyElement(key="to_id",type=Neo4JType.TOINTEGER)
	private Long to_id;
	
	@CsvBindByName(column = "transport_mode")
	@Neo4JPropertyElement(key="transport_mode",type=Neo4JType.TOSTRING)
	private String transportMode;
	
	@CsvBindByName(column = "weight")
	@Neo4JPropertyElement(key="weight",type=Neo4JType.TOFLOAT)
	private Double weight;

}
