package api.truck.data.dto;

import java.io.Serializable;

public class TruckDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String vin;
	private String hardware;
	private String software;
	private Boolean present;
	
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getHardware() {
		return hardware;
	}
	public void setHardware(String hardware) {
		this.hardware = hardware;
	}
	public String getSoftware() {
		return software;
	}
	public void setSoftware(String software) {
		this.software = software;
	}
	public Boolean isPresent() {
		return present;
	}
	public void setPresent(Boolean present) {
		this.present = present;
	}

}
