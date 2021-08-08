package core.graph.geo;

import com.opencsv.bean.CsvBindByPosition;

public class City {
	
	@CsvBindByPosition(position = 0)
	private String name;
	@CsvBindByPosition(position = 1)
	private Double lat;
	@CsvBindByPosition(position = 2)
	private Double lon;
	@CsvBindByPosition(position = 3)
	private String country;
	@CsvBindByPosition(position = 4)
	private Integer population;
	
	
	public String getName() {
		return this.name;
	}
	
	public Double getLat() {
		return this.lat;
	}
	
	public Double getLon() {
		return this.lon;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public Integer getPopulation() {
		return this.population;
	}
	

}
