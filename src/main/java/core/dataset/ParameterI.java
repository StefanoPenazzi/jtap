package core.dataset;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import controller.Controller;

public interface ParameterI<T> extends ModelElementI {
	
	public Object getParameter();
	public String getDescription();
	public List<List<T>> getParameterDescription();
	

}
