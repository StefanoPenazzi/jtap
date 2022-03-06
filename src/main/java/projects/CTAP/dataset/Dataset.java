package projects.CTAP.dataset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.google.inject.Provider;

import core.dataset.DatasetI;
import core.dataset.ModelElementI;
import core.dataset.ParameterI;

public final class Dataset implements DatasetI {
	
	private Map<String,ModelElementI> modelElementsMap = new HashMap<>();
	
	public Dataset(List<? extends ModelElementI> params) {
		this.modelElementsMap = params.stream()
				.collect(Collectors.toMap(ModelElementI::getId, Function.identity()));
	}
	
}
