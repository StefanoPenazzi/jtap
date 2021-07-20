package controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Module;

public class Injector {
	
private static Logger logger = LogManager.getLogger(Injector.class);
	
	
	private Injector(){}
	
	public static com.google.inject.Injector createInjector(Module... modules) {
		List<com.google.inject.Module> guiceModules = new ArrayList<>();
		for (Module module : modules) {
			guiceModules.add(module);
		}
		com.google.inject.Injector injector = Guice.createInjector(guiceModules);
		return injector;
	}

}
