package api.truck.data.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table (name="truckconfiguration")
public class TruckConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(nullable = false)
	private String vin;
	@Column(nullable = false)
	private Date created_at;
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="truckconfiguration")
	@JsonManagedReference
    private Set<TruckHardwareConf> hardwareconfigs;
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="truckconfiguration")
	@JsonManagedReference
    private Set<TruckSoftwareConf> softwareconfigs;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Set<TruckHardwareConf> getHardwareconfigs() {
		return hardwareconfigs;
	}

	public void setHardwareconfigs(Set<TruckHardwareConf> hardwareconfigs) {
		this.hardwareconfigs = hardwareconfigs;
	}

	public Set<TruckSoftwareConf> getSoftwareconfigs() {
		return softwareconfigs;
	}

	public void setSoftwareconfigs(Set<TruckSoftwareConf> softwareconfigs) {
		this.softwareconfigs = softwareconfigs;
	}
	
	@Override
	public boolean equals(Object object) {
	    return object instanceof TruckConfiguration && ((TruckConfiguration) object).vin.equals(vin);
	}
	
	@Override
	public int hashCode() {
	    return vin.hashCode(); 
	}

}
