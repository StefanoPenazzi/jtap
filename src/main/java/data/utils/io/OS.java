package data.utils.io;

import java.io.File;

public class OS {
	
	public static void delete(File f) {
	    if (!f.exists())
	      throw new IllegalArgumentException("The file or directory does not exist " + f.toString());
	    if (!f.canWrite())
	      throw new IllegalArgumentException("Permission denied: " + f.toString());
	    if (f.isDirectory()) {
	      String[] files = f.list();
	      if (files.length > 0)
	        throw new IllegalArgumentException("The directory id not empty: " + f.toString());
	    }
	    boolean success = f.delete();
	    if (!success)
	      throw new IllegalArgumentException("Failed");
	}
	
	public static void delete(String fileName) {
		File f = new File(fileName);
		delete(f);
	}


}
