package projects.CTAP.graphElements;

import com.opencsv.bean.CsvBindByName;

import core.graph.LinkI;
import core.graph.annotations.GraphElementAnnotation.Neo4JLinkElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

@Neo4JLinkElement(label="DestinationProbLink")
public class DestinationProbLink implements LinkI {
	
	@CsvBindByName(column = "from_id")
	@Neo4JPropertyElement(key="from_id",type=Neo4JType.TOINTEGER)
	private Long fromId;
	
	@CsvBindByName(column = "to_id")
	@Neo4JPropertyElement(key="to_id",type=Neo4JType.TOINTEGER)
	private Long toId;
	
	@CsvBindByName(column = "probability")
	@Neo4JPropertyElement(key="probability",type=Neo4JType.TOFLOAT)
	private Double probability;
	
	public DestinationProbLink(){
		
	}
	
    public DestinationProbLink(Long fromId,Long toId,Double probability){
		this.fromId = fromId;
		this.toId = toId;
		this.probability = probability;
	}

}
