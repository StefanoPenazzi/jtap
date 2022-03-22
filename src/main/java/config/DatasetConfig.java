package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "datasetConfig")
public class DatasetConfig {
	
	private NewDatasetParams newDatasetParams;
	private String importDirectory;
	
	@XmlElement(name = "newDatasetParams",required = true)
	public NewDatasetParams getNewDatasetParams() {
		return newDatasetParams;
	}
	public void setNewDatasetParams(NewDatasetParams newDatasetParams) {
		this.newDatasetParams = newDatasetParams;
	}
	
	@XmlElement(name = "importDirectory",required = true)
	public String getImportDirectory() {
		return importDirectory;
	}
	public void setImportDirectory(String importDirectory) {
		this.importDirectory = importDirectory;
	}

}
