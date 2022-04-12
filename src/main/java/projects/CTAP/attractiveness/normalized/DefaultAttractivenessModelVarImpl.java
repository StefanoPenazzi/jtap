package projects.CTAP.attractiveness.normalized;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import projects.CTAP.attractiveness.AttractivenessModelVariablesI;

public class DefaultAttractivenessModelVarImpl implements AttractivenessModelVariablesI {

	@Override
	public List<Double> getVariables(Map<String, Object> map) {
		
		List<Double> res =new ArrayList<>();
		res.add(((map.get("restaurant") == null? new Long(0): (Long)map.get("restaurant"))).doubleValue());
		res.add(((map.get("restaurant") == null? new Long(0): (Long)map.get("restaurant"))).doubleValue());
		return res;
	}

}
