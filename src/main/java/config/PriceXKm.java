package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "priceXKm")
public class PriceXKm {
	
	private Double car;
	private Double train;
	private Double bus;
	private Double plane;
	private Double walk;
	
	@XmlElement(name = "bus",required = true)
	public Double getBus() {
		return bus;
	}
	
	@XmlElement(name = "car",required = true)
	public Double getCar() {
		return car;
	}
	
	@XmlElement(name = "plane",required = true)
	public Double getPlane() {
		return plane;
	}
	
	@XmlElement(name = "train",required = true)
	public Double getTrain() {
		return train;
	}
	
	@XmlElement(name = "walk",required = true)
	public Double getWalk() {
		return walk;
	}
	
	public void setBus(Double bus) {
		this.bus = bus;
	}
	
	public void setCar(Double car) {
		this.car = car;
	}
	
	public void setPlane(Double plane) {
		this.plane = plane;
	}
	
	public void setTrain(Double train) {
		this.train = train;
	}
	
	public void setWalk(Double walk) {
		this.walk = walk;
	}

}
