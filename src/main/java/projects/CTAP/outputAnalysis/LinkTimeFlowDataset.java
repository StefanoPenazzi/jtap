package projects.CTAP.outputAnalysis;

import core.dataset.DatasetI;
import projects.CTAP.dataset.CitiesDsIndex;
import projects.CTAP.dataset.CitiesOsIndex;
import projects.CTAP.dataset.Ds2DsPathParameter;
import projects.CTAP.dataset.Ds2OsPathParameter;
import projects.CTAP.dataset.Os2DsPathParameter;

public class LinkTimeFlowDataset implements DatasetI  {
	
	private final CitiesDsIndex citiesDsIndex;
	private final CitiesOsIndex citiesOsIndex;
	private final Ds2OsPathParameter ds2OsPathParameter; 
	private final Ds2DsPathParameter ds2DsPathParameter; 
	private final Os2DsPathParameter os2DsPathParameter; 
	
	
	public LinkTimeFlowDataset(CitiesDsIndex citiesDsIndex,
							CitiesOsIndex citiesOsIndex,
							Ds2OsPathParameter ds2OsPathParameter, 
							Ds2DsPathParameter ds2DsPathParameter, 
							Os2DsPathParameter os2DsPathParameter ){
		
		this.citiesDsIndex= citiesDsIndex;
		this.citiesOsIndex= citiesOsIndex;
		this.ds2OsPathParameter= ds2OsPathParameter;
		this.ds2DsPathParameter= ds2DsPathParameter;
		this.os2DsPathParameter= os2DsPathParameter; 
	}
	
	public CitiesDsIndex getCitiesDsIndex() {
		return citiesDsIndex;
	}
	
	public CitiesOsIndex getCitiesOsIndex() {
		return citiesOsIndex;
	}
	
	public Ds2DsPathParameter getDs2DsPathParameter() {
		return ds2DsPathParameter;
	}
	
	public Ds2OsPathParameter getDs2OsPathParameter() {
		return ds2OsPathParameter;
	}
	
	public Os2DsPathParameter getOs2DsPathParameter() {
		return os2DsPathParameter;
	}

}
