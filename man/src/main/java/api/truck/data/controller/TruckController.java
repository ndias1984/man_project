package api.truck.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import api.truck.data.entity.TruckConfiguration;
import api.truck.data.repository.TruckConfigurationRepository;

@RestController
public class TruckController {
	
	@Autowired
    private TruckConfigurationRepository truckConfigurationRepository;

	
    @RequestMapping(value = "/fota/vehicles/{vin}/installable", method = RequestMethod.GET)
    public ResponseEntity<TruckConfiguration> GetInstallable(@PathVariable(value = "vin") String vin)
    {
    	TruckConfiguration truckConfig = truckConfigurationRepository.findInstallableFeatures(vin);
    	if(truckConfig != null)
            return new ResponseEntity<TruckConfiguration>(truckConfig, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @RequestMapping(value = "/fota/vehicles/{vin}/incompatible", method = RequestMethod.GET)
    public ResponseEntity<TruckConfiguration> GetIncompatible(@PathVariable(value = "vin") String vin)
    {
    	TruckConfiguration truckConfig = truckConfigurationRepository.findNotInstallableFeatures(vin);
    	if(truckConfig != null)
            return new ResponseEntity<TruckConfiguration>(truckConfig, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
