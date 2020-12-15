package api.truck.data.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import api.truck.data.entity.TruckConfiguration;

@Repository
public interface TruckConfigurationRepository extends JpaRepository<TruckConfiguration, Long>{
	
	@Query("SELECT tc FROM TruckConfiguration tc WHERE tc.vin = ?1")
	TruckConfiguration findByVIN(String vin);
	
	@Query("SELECT tc FROM TruckConfiguration tc WHERE tc.vin in :vins")
	List<TruckConfiguration> findByVINs(Set<String> vins);
	
	@Query("SELECT COUNT(*) FROM TruckHardwareConf thc "
			+ "INNER JOIN TruckConfiguration tc ON tc.id = thc.truckconfiguration  "
			+ "WHERE tc.vin = ?1 and thc.hardwarecode = ?2")
	int checkTruckHardwareConfiguration (String vin, String hardwarecode);
	
	@Query("SELECT COUNT(*) FROM TruckSoftwareConf tsc "
			+ "INNER JOIN TruckConfiguration tc ON tc.id = tsc.truckconfiguration  "
			+ "WHERE tc.vin = ?1 and tsc.softwarecode = ?2")
	int checkTruckSofwareConfiguration (String vin, String softwarecode);
	
	@Query("SELECT tc FROM TruckConfiguration tc "
			+" JOIN FETCH tc.hardwareconfigs as thc "
			+" JOIN FETCH tc.softwareconfigs as tsc " 
			+ " WHERE tc.vin = ?1 and (thc.present = 1 and tsc.present = 1) " )
	TruckConfiguration findInstallableFeatures(String vin);
	
	@Query("SELECT tc FROM TruckConfiguration tc "
			+" JOIN FETCH tc.hardwareconfigs as thc "
			+" JOIN FETCH tc.softwareconfigs as tsc " 
			+ " WHERE tc.vin = ?1 and (thc.present = 0 and tsc.present = 0) " )
	TruckConfiguration findNotInstallableFeatures(String vin);

}

