package core.dataset;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.fasterxml.jackson.databind.ObjectMapper;

import controller.Controller;

public interface ModelElementI {
	
	public String getId();
	default void save() {
		ObjectMapper mapper = new ObjectMapper();
    	try (Writer writer = new FileWriter(Controller.getConfig().getGeneralConfig().getOutputDirectory()+getId()+".json")) {
    	     writer.append(mapper.writeValueAsString(this));
    	} catch (IOException ex) {
    	  ex.printStackTrace(System.err);
    	}
	}

}
