package controller;

import com.google.inject.Binder;
import com.google.inject.Module;

import config.Config;

public class ConfigModule implements Module {

	private final Config config;

	public ConfigModule(Config config) {
		this.config = config;
	}

	@Override
	public void configure(Binder binder) {
		binder.bind(Config.class).toInstance(config);
		
	}

}
