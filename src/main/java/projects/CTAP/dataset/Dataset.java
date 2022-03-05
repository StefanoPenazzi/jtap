package projects.CTAP.dataset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.google.inject.Provider;

import core.dataset.DatasetI;
import core.dataset.ParameterI;

public final class Dataset implements DatasetI {
	
	private Map<String,ParameterI> paramsMap = new HashMap<>();
	
	public Dataset(List<? extends ParameterI> params) {
		this.paramsMap = params.stream()
				.collect(Collectors.toMap(ParameterI::getId, Function.identity()));
	}
	
}
