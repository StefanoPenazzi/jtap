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
	private Long fromId;
	
	@CsvBindByName(column = "to_id")
	@Neo4JPropertyElement(key="to_id",type=Neo4JType.TOINTEGER)
	private Long toId;
	
	@CsvBindByName(column = "link_type")
	@Neo4JPropertyElement(key="link_type",type=Neo4JType.TOSTRING)
	private String linkType;
	
	@CsvBindByName(column = "weight")
	@Neo4JPropertyElement(key="weight",type=Neo4JType.TOFLOAT)
	private Double weight;
	
	public CTAPTransportLink(){
		
	}
	
    public CTAPTransportLink(Long fromId,Long toId,String linkType,Double weight){
		this.fromId = fromId;
		this.toId = toId;
		this.linkType = linkType;
		this.weight = weight;
	}

}
