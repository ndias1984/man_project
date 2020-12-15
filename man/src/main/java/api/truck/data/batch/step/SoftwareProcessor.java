package api.truck.data.batch.step;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import api.truck.data.dto.TruckDTO;
import api.truck.data.repository.TruckConfigurationRepository;

public class SoftwareProcessor implements ItemProcessor<TruckDTO, TruckDTO> {
	
	Logger logger = Logger.getLogger(SoftwareProcessor.class.getName());
	
	@Autowired
	TruckConfigurationRepository repo;
	
	private String softwareCodesPresent;
	private String softwareCodesNotPresent;

	public SoftwareProcessor(String softwareCodesPresent,String softwareCodesNotPresent,TruckConfigurationRepository truckConfigurationRepository) {
		this.softwareCodesPresent = softwareCodesPresent;
		this.softwareCodesNotPresent = softwareCodesNotPresent;
		if (this.repo == null) {
			this.repo = truckConfigurationRepository;
		}
	}

	@Override
	public TruckDTO process(TruckDTO item) throws Exception {
		
		//Let's check if there is an item available and if it isn't already available in DB
		if (item != null && this.repo.checkTruckSofwareConfiguration(item.getVin(), item.getSoftware()) == 0) {
		
			List<String> present = Arrays.asList(softwareCodesPresent.split(","));
			List<String> notPresent = Arrays.asList(softwareCodesNotPresent.split(","));
			
			if (present.contains(item.getSoftware())) {
				//Available hardware configuration for VIN
				item.setPresent(true); 
			} else if (notPresent.contains(item.getSoftware())) {
				// Not available hardware configuration for VIN
				item.setPresent(false); 
			} else {
				// Not present in any of the lists, it will be null. Can have further configuration in the future
				item.setPresent(null); 
			}
			logger.info("Processing: VIN=" + item.getVin() + " SOFTWARE=" + item.getSoftware() + " Present: " + item.isPresent());
			return item;
		}else {
			return null;
		}
	}

}
