package projects.CTAP.modules;

import controller.AbstractModule;
import core.dataset.DatasetProvider;

public class CTAPDatasetModule extends AbstractModule{

	@Override
	public void install() {
		bindDataset(DatasetProvider.class);
	}
}
