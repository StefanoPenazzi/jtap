package config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ctapTransportLinkConfig")
public class CtapTransportLinkConfig {

	private Double mvServicePerceivedReliability;
	private Double mvNoServicePerceivedReliability;
	private Double mvWaitingTime;
	private MvTransferTime mvTransferTime;
	private PriceXKm priceXKm;
	private ExternalIntermodalCost externalIntermodalCost;
	
	@XmlElement(name = "externalIntermodalCost",required = true)
	public ExternalIntermodalCost getExternalIntermodalCost() {
		return externalIntermodalCost;
	}
	
	@XmlElement(name = "mvNoServicePerceivedReliability",required = true)
	public Double getMvNoServicePerceivedReliability() {
		return mvNoServicePerceivedReliability;
	}
	
	@XmlElement(name = "mvServicePerceivedReliability",required = true)
	public Double getMvServicePerceivedReliability() {
		return mvServicePerceivedReliability;
	}
	
	@XmlElement(name = "mvTransferTime",required = true)
	public MvTransferTime getMvTransferTime() {
		return mvTransferTime;
	}
	
	@XmlElement(name = "mvWaitingTime",required = true)
	public Double getMvWaitingTime() {
		return mvWaitingTime;
	}
	
	@XmlElement(name = "priceXKm",required = true)
	public PriceXKm getPriceXKm() {
		return priceXKm;
	}
	
	public void setMvNoServicePerceivedReliability(Double mvNoServicePerceivedReliability) {
		this.mvNoServicePerceivedReliability = mvNoServicePerceivedReliability;
	}
	
	public void setExternalIntermodalCost(ExternalIntermodalCost externalIntermodalCost) {
		this.externalIntermodalCost = externalIntermodalCost;
	}
	
	public void setMvServicePerceivedReliability(Double mvServicePerceivedReliability) {
		this.mvServicePerceivedReliability = mvServicePerceivedReliability;
	}
	
	public void setMvTransferTime(MvTransferTime mvTransferTime) {
		this.mvTransferTime = mvTransferTime;
	}
	
	public void setMvWaitingTime(Double mvWaitingTime) {
		this.mvWaitingTime = mvWaitingTime;
	}
	
	public void setPriceXKm(PriceXKm priceXKm) {
		this.priceXKm = priceXKm;
	}
	
}
