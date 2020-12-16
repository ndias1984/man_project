package api.truck.data.batch.step;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import api.truck.data.dto.TruckDTO;
import api.truck.data.entity.TruckConfiguration;
import api.truck.data.entity.TruckHardwareConf;
import api.truck.data.repository.TruckConfigurationRepository;

public class HardwareWriter implements ItemWriter<TruckDTO> {
	
	Logger logger = Logger.getLogger(HardwareWriter.class.getName());
	
	@Autowired
	TruckConfigurationRepository repo;
	
	public HardwareWriter(TruckConfigurationRepository truckConfigurationRepository) {
		if (this.repo == null) {
			this.repo = truckConfigurationRepository;
		}
	}

	@Override
	public void write(List<? extends TruckDTO> truckData) throws Exception {

		if (truckData != null && truckData.size() > 0) {

			List<TruckConfiguration> truckConfigsDB = this.repo.findByVINs(getUniqueVINs(truckData));
			Set<TruckConfiguration> truckConfigs = new HashSet<TruckConfiguration>();
		
			for (TruckDTO truck : truckData) {
				TruckConfiguration truckConf = null;
				if (truckConfigsDB != null && !truckConfigsDB.isEmpty()) {
					truckConf = this.repo.findByVIN(truck.getVin());
					if (truckConf != null) {
						addExtraConfiguration(truckConf, truckData);
						truckConfigs.add(truckConf);
					} else {
						truckConf = new TruckConfiguration();
						addNewConfiguration(truckConf,truck, truckData);
						truckConfigs.add(truckConf);
					}
				} else {
					truckConf = new TruckConfiguration();
					addNewConfiguration(truckConf,truck, truckData);
					truckConfigs.add(truckConf);
				}
				
			}
	
			this.repo.saveAll(truckConfigs); //It will save the chunks of 500 rows
			this.repo.flush(); // commits those rows in DB and releases memory
		} else {
			logger.info("No new data for Hardware configurations...");
		}
	}
	
	private Set<String> getUniqueVINs (List<? extends TruckDTO> truckData) {
		Set<String> s = new LinkedHashSet<>();
		for (TruckDTO dto : truckData) {
			s.add(dto.getVin());
		}
		
		return s;
	}
	
	private TruckConfiguration addNewConfiguration (TruckConfiguration truckConf, 
			TruckDTO truck,List<? extends TruckDTO> truckData) {
		
		Set<TruckHardwareConf> hardwareConfs = new HashSet<TruckHardwareConf>();
		
		truckConf.setVin(truck.getVin());
		truckConf.setCreated_at(new Date());
		for (TruckDTO truckDTO : truckData) {
			if (truckDTO.getVin().equals(truckConf.getVin())) {
				TruckHardwareConf hardwareConf = new TruckHardwareConf();
				hardwareConf.setHardwarecode(truckDTO.getHardware());
				hardwareConf.setCreated_at(truckConf.getCreated_at());
				hardwareConf.setPresent(truckDTO.isPresent());
				hardwareConf.setTruckconfiguration(truckConf);
				hardwareConfs.add(hardwareConf);
			}
		}
		truckConf.setHardwareconfigs(hardwareConfs);
		return truckConf;
	}
	
	private TruckConfiguration addExtraConfiguration (TruckConfiguration truckConf, List<? extends TruckDTO> truckData) {
		
		Set<TruckHardwareConf> hardwareConfs = truckConf.getHardwareconfigs();
		boolean exists = false;
		
		if (hardwareConfs != null && !hardwareConfs.isEmpty()) {
			for (TruckDTO truckDTO : truckData) {
				if (truckDTO.getVin().equals(truckConf.getVin())) {
					for (TruckHardwareConf hardware : hardwareConfs) {
						if (hardware.getHardwarecode().equals(truckDTO.getHardware())) {
							exists = true;
						}
					}
					
					if (!exists) {
						TruckHardwareConf hardwareConf = new TruckHardwareConf();
						hardwareConf.setHardwarecode(truckDTO.getHardware());
						hardwareConf.setCreated_at(truckConf.getCreated_at());
						hardwareConf.setPresent(truckDTO.isPresent());
						hardwareConf.setTruckconfiguration(truckConf);
						hardwareConfs.add(hardwareConf);
					}
				}
					
				exists = false;
			}
		}
		return truckConf;
	}
}
