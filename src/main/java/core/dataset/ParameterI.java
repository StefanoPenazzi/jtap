package core.dataset;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import controller.Controller;

public interface ParameterI<T> extends Serializable {
	
	public Object getParameter();
	public String getDescription();
	public String getId();
	public List<List<T>> getParameterDescription();
	default void save() {
		ObjectMapper mapper = new ObjectMapper();
    	try (Writer writer = new FileWriter(Controller.getConfig().getGeneralConfig().getOutputDirectory()+getId()+".json")) {
    	     writer.append(mapper.writeValueAsString(this));
    	} catch (IOException ex) {
    	  ex.printStackTrace(System.err);
    	}
	}

}
