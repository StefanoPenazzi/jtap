package controller.modules;

import controller.AbstractModule;
import projects.CTAP.modules.CTAPDatasetModule;

public class DefaultModule extends AbstractModule {

	@Override
	public void install() {
		
		install(new CTAPDatasetModule());
	}
}
