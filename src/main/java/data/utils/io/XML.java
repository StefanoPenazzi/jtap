package data.utils.io;

import java.io.File;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class XML {
	
	public static <T> T read(File file, Class<T> c) {
		
		  JAXBContext jaxbContext = null;
		  T res = null;
		  try {
		      jaxbContext = JAXBContext.newInstance(c);
		      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		      res = (T) jaxbUnmarshaller.unmarshal(file);
		
		  } catch (JAXBException e) {
		      e.printStackTrace();
		  }
	      return res;
	}
	
	public static <T> void write(File file,T c) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(c.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(c, new File("product.xml"));
	}

}
