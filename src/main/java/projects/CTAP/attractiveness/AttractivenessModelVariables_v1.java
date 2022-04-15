package projects.CTAP.attractiveness;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttractivenessModelVariables_v1 implements AttractivenessModelVariablesI{
	
	@Override
	public List<Double> getVariables(Map<String, Object> map) {
		
		List<Double> res =new ArrayList<>();
		res.add(((map.get("restaurant") == null? new Long(0): (Long)map.get("restaurant"))).doubleValue());
		res.add(((map.get("restaurant") == null? new Long(0): (Long)map.get("restaurant"))).doubleValue());
		return res;
	}
	
}
