package controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.inject.Guice;
import com.google.inject.Module;

import config.Config;
import controller.modules.ConfigModule;

public class Injector {
	
    private static Logger logger = LogManager.getLogger(Injector.class);
	
	
	private Injector(){}
	
	public static com.google.inject.Injector createInjector(final Config config,Module... modules) {
		
		List<com.google.inject.Module> guiceModules = new ArrayList<>();
		for (Module module : modules) {
			guiceModules.add(module);
		}
		guiceModules.add(new ConfigModule(config));
		com.google.inject.Injector injector = Guice.createInjector(guiceModules);
		return injector;
	}

}
