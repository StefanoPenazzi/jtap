package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "externalIntermodalCost")
public class ExternalIntermodalCost {
	
	private Double car2train;
	private Double car2bus;
	private Double car2plane;
	private Double car2walk;
	private Double train2car;
	private Double train2bus;
	private Double train2plane;
	private Double train2walk;
	private Double plane2car;
	private Double plane2bus;
	private Double plane2train;
	private Double plane2walk;
	private Double bus2car;
	private Double bus2plane;
	private Double bus2train;
	private Double bus2walk;
	private Double walk2car;
	private Double walk2plane;
	private Double walk2train;
	private Double walk2bus;
	
	@XmlElement(name = "bus2car",required = true)
	public Double getBus2car() {
		return bus2car;
	}
	
	@XmlElement(name = "bus2plane",required = true)
	public Double getBus2plane() {
		return bus2plane;
	}
	
	@XmlElement(name = "bus2train",required = true)
	public Double getBus2train() {
		return bus2train;
	}
	
	@XmlElement(name = "car2bus",required = true)
	public Double getCar2bus() {
		return car2bus;
	}
	
	@XmlElement(name = "car2plane",required = true)
	public Double getCar2plane() {
		return car2plane;
	}
	
	@XmlElement(name = "car2train",required = true)
	public Double getCar2train() {
		return car2train;
	}
	
	@XmlElement(name = "plane2bus",required = true)
	public Double getPlane2bus() {
		return plane2bus;
	}
	
	@XmlElement(name = "plane2car",required = true)
	public Double getPlane2car() {
		return plane2car;
	}
	
	@XmlElement(name = "plane2train",required = true)
	public Double getPlane2train() {
		return plane2train;
	}
	
	@XmlElement(name = "train2bus",required = true)
	public Double getTrain2bus() {
		return train2bus;
	}
	
	@XmlElement(name = "train2plane",required = true)
	public Double getTrain2plane() {
		return train2plane;
	}
	
	@XmlElement(name = "train2car",required = true)
	public Double getTrain2car() {
		return train2car;
	}
	
	@XmlElement(name = "bus2walk",required = true)
	public Double getBus2walk() {
		return bus2walk;
	}
	
	@XmlElement(name = "car2walk",required = true)
	public Double getCar2walk() {
		return car2walk;
	}
	
	@XmlElement(name = "plane2walk",required = true)
	public Double getPlane2walk() {
		return plane2walk;
	}
	
	@XmlElement(name = "train2walk",required = true)
	public Double getTrain2walk() {
		return train2walk;
	}
	
	@XmlElement(name = "walk2bus",required = true)
	public Double getWalk2bus() {
		return walk2bus;
	}
	
	@XmlElement(name = "walk2car",required = true)
	public Double getWalk2car() {
		return walk2car;
	}
	
	@XmlElement(name = "walk2plane",required = true)
	public Double getWalk2plane() {
		return walk2plane;
	}
	
	@XmlElement(name = "walk2train",required = true)
	public Double getWalk2train() {
		return walk2train;
	}
	
	public void setBus2car(Double bus2car) {
		this.bus2car = bus2car;
	}
	
	public void setBus2plane(Double bus2plane) {
		this.bus2plane = bus2plane;
	}
	
	public void setBus2train(Double bus2train) {
		this.bus2train = bus2train;
	}
	
	public void setCar2bus(Double car2bus) {
		this.car2bus = car2bus;
	}
	
	public void setCar2plane(Double car2plane) {
		this.car2plane = car2plane;
	}
	
	public void setCar2train(Double car2train) {
		this.car2train = car2train;
	}
	
	public void setPlane2bus(Double plane2bus) {
		this.plane2bus = plane2bus;
	}
	
	public void setPlane2car(Double plane2car) {
		this.plane2car = plane2car;
	}
	
	public void setPlane2train(Double plane2train) {
		this.plane2train = plane2train;
	}
	
	public void setTrain2bus(Double train2bus) {
		this.train2bus = train2bus;
	}
	
	public void setTrain2car(Double train2car) {
		this.train2car = train2car;
	}
	
	public void setTrain2plane(Double train2plane) {
		this.train2plane = train2plane;
	}
	
	public void setBus2walk(Double bus2walk) {
		this.bus2walk = bus2walk;
	}
	
	public void setCar2walk(Double car2walk) {
		this.car2walk = car2walk;
	}
	
	public void setPlane2walk(Double plane2walk) {
		this.plane2walk = plane2walk;
	}
	
	public void setTrain2walk(Double train2walk) {
		this.train2walk = train2walk;
	}
	
	public void setWalk2bus(Double walk2bus) {
		this.walk2bus = walk2bus;
	}
	
	public void setWalk2car(Double walk2car) {
		this.walk2car = walk2car;
	}
	
	public void setWalk2plane(Double walk2plane) {
		this.walk2plane = walk2plane;
	}
	
	public void setWalk2train(Double walk2train) {
		this.walk2train = walk2train;
	}

}
