package api.truck.data.batch.step;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import api.truck.data.dto.TruckDTO;
import api.truck.data.repository.TruckConfigurationRepository;

public class HardwareProcessor implements ItemProcessor<TruckDTO, TruckDTO> {
	
	Logger logger = Logger.getLogger(HardwareProcessor.class.getName());
	
	@Autowired
	TruckConfigurationRepository repo;
	
	private String hardwareCodesPresent;
	private String hardwareCodesNotPresent;

	public HardwareProcessor(String hardwareCodesPresent,String hardwareCodesNotPresent,
			TruckConfigurationRepository truckConfigurationRepository) {
		this.hardwareCodesPresent = hardwareCodesPresent;
		this.hardwareCodesNotPresent = hardwareCodesNotPresent;
		if (this.repo == null) {
			this.repo = truckConfigurationRepository;
		}
	}

	@Override
	public TruckDTO process(TruckDTO item) throws Exception {
		
		//Let's check if there is an item available and if it isn't already available in DB
		if (item != null && this.repo.checkTruckHardwareConfiguration(item.getVin(), item.getHardware()) == 0) {
		
			List<String> present = Arrays.asList(hardwareCodesPresent.split(","));
			List<String> notPresent = Arrays.asList(hardwareCodesNotPresent.split(","));
			
			if (present.contains(item.getHardware())) {
				//Available hardware configuration for VIN
				item.setPresent(true); 
			} else if (notPresent.contains(item.getHardware())) {
				// Not available hardware configuration for VIN
				item.setPresent(false); 
			} else {
				// Not present in any of the lists, it will be null. Can have further configuration in the future
				item.setPresent(null); 
			}
			logger.info("Processing: VIN=" + item.getVin() + " HARDWARE=" + item.getHardware() + " Present: " + item.isPresent());
			return item;
		} else {
			return null;
		}
	}

}
