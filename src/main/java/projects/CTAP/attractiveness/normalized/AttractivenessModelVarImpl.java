package projects.CTAP.attractiveness.normalized;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import projects.CTAP.attractiveness.AttractivenessModelVariablesI;

public class AttractivenessModelVarImpl  implements AttractivenessModelVariablesI {

	@Override
	public List<Double> getVariables(Map<String, Object> map) {
		
		List<Double> res =new ArrayList<>();
		res.add(((map.get("restaurant") == null? new Long(0): (Long)map.get("restaurant"))).doubleValue());
		res.add(((map.get("hotel") == null? new Long(0): (Long)map.get("hotel"))).doubleValue());
		return res;
	}

}