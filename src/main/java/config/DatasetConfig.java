package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "datasetConfig")
public class DatasetConfig {
	
	private String importDirectory;
	private NewDatasetParams newDatasetParams;
	
	@XmlElement(name = "importDirectory",required = false)
	public String getImportDirectory() {
		return importDirectory;
	}
	public void setImportDirectory(String importDirectory) {
		this.importDirectory = importDirectory;
	}
	@XmlElement(name = "newDatasetParams ",required = false)
	public NewDatasetParams getNewDatasetParams() {
		return newDatasetParams;
	}
	public void setNewDatasetParams(NewDatasetParams newDatasetParams) {
		this.newDatasetParams = newDatasetParams;
	}

}
