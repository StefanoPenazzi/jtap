package controller;

import java.io.File;
import java.util.Collections;
import java.util.List;
import config.Config;
import controller.modules.DefaultModule;
import data.external.neo4j.Neo4jConnection;


public class Controller implements ControllerI {
	
	private static com.google.inject.Injector injector;
	
	private static List<AbstractModule> modules = Collections.singletonList(new DefaultModule());
	
	private  static AbstractModule overrides = AbstractModule.emptyModule();
	
	private final Config config;

	public Controller(Config config) {
		this.config = config;
	}
	
	public final void run() {
		injector = Injector.createInjector(this.config,AbstractModule.override(Collections.singleton(new AbstractModule() {
			@Override
			public void install() {
				for (AbstractModule module : modules) {
					install(module);
				}
			}
		}), overrides));
	}
	
	public static com.google.inject.Injector getInjector(){
		return injector;
	}
	
	public static Neo4jConnection getNeo4JConnection() {
		return getInjector().getInstance(Neo4jConnection.class);
	}
	
	public static Config getConfig() {
		return getInjector().getInstance(Config.class);
	}
	
	public static void emptyTempDirectory() {
		File directory = new File(getInjector().getInstance(Config.class).getGeneralConfig().getTempDirectory());
		File[] files = directory.listFiles(); 
		if (files == null) {
			
		}else {
			for(File file : files) {
				file.delete();
			}
		}
	}
	public static void emptyOutputDirectory() {
		File directory = new File(getInjector().getInstance(Config.class).getGeneralConfig().getOutputDirectory());
		File[] files = directory.listFiles();    
		for(File file : files) {
		  file.delete();
		}
	}
	
	public final Controller addOverridingModule( AbstractModule abstractModule ) {
		this.overrides = AbstractModule.override(Collections.singletonList(this.overrides), abstractModule);
		return this ;
	}
}
