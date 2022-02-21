package projects.CTAP.attractiveness;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import config.Config;
import core.graph.Activity.ActivityNode;
import core.graph.geo.CityNode;
import core.graph.population.AgentNodeI;
import core.graph.population.StdAgentNodeImpl;
import projects.CTAP.graphElements.CTAPCityStatNode;

public class AttractivenessCTAP extends AttractivenessAbstract {
	
	private List<String> paramsVector_ = new ArrayList<>(Arrays.asList("restaurant", "theater", "parking_space","parking"));
	private final List<String> paramsVector;
//	private List<String> variablesVector_ = new ArrayList<>(Arrays.asList("restaurant", "theater", "time"));
//	private final List<String> variablesVector;
	
	private Config config;
	private Map<Integer,Map<String,Double[]>> parametersMap = new HashMap<>();
	
	@Inject
	public AttractivenessCTAP (Config config) throws IOException {
		//TODO
		super(0d,8760d);
		this.config = config;
		paramsVector = List.of(paramsVector_.toArray(new String[]{}));
		//variablesVector = List.of(variablesVector_.toArray(new String[]{}));
		initialize();
	}
	
	private void initialize() throws IOException {
		//import model params as JSON
		ObjectMapper mapper = new ObjectMapper();
    	FileInputStream inputStream = new FileInputStream(config.getCtapModelConfig().getAttractivenessFile());
    	List<AttractivenessAgentCTAP> parameters;
    	try {
    	    String json = IOUtils.toString(inputStream);
    	    parameters = new ObjectMapper()
    	      .readerFor(new TypeReference<List<AttractivenessAgentCTAP>>(){})
    	      .readValue(json); 
    	} finally {
    	    inputStream.close();
    	}
    	parameters.forEach(parameter->{
    		Integer agId = parameter.getAgentId();
    		parametersMap.put(agId,new HashMap<String,Double[]>());
    		parameter.getActivities().forEach(activity->{
    			Double[] pa = new Double[paramsVector.size()];
    			int i = 0;
    			for(String value: paramsVector){
    				pa[i] = (Double) activity.getParameters().get(value);
    				i++;
    			}
    			parametersMap.get(agId).put(activity.getActivity(),pa);
    		});
    	});
	}
	
	private Double getAttractiveness(Double[] params, Double[] variables) {
		return seasonalitySummerSinglePeakSineFunction(params[0]*variables[0],variables[2]);
	}
	
	public Double getAttractiveness(Double[] variables,Integer agentId, String activity) {
		return getAttractiveness(parametersMap.get(agentId).get(activity),variables);
	}

}
