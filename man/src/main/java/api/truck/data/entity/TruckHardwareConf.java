package api.truck.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table (name="truckhardwareconf")
public class TruckHardwareConf {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(nullable = false)
	private String hardwarecode;
	@Column(nullable = true)
	private Boolean present;
	@Column(nullable = false)
	private Date created_at;
	
	@ManyToOne
    @JoinColumn(name="vin_id", referencedColumnName="id", nullable=false)
	@JsonBackReference
    private TruckConfiguration truckconfiguration;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getHardwarecode() {
		return hardwarecode;
	}
	public void setHardwarecode(String hardwarecode) {
		this.hardwarecode = hardwarecode;
	}
	public Boolean isPresent() {
		return present;
	}
	public void setPresent(Boolean present) {
		this.present = present;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public TruckConfiguration getTruckconfiguration() {
		return truckconfiguration;
	}
	public void setTruckconfiguration(TruckConfiguration truckconfiguration) {
		this.truckconfiguration = truckconfiguration;
	}

}
