package api.truck.data.batch.step;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import api.truck.data.dto.TruckDTO;

public class HardwareReader implements ItemReader<TruckDTO> {
	
	Logger logger = Logger.getLogger(HardwareReader.class.getName());
	
	@Bean
    public ItemReader<TruckDTO> itemReader() {
		FlatFileItemReader<TruckDTO> csvFileReader = new FlatFileItemReader<>();
		ClassLoader cl = this.getClass().getClassLoader(); 
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
		Resource[] resources;
		Resource hardwareResource;
		try {
			resources = resolver.getResources("classpath*:/to_be_processed/hard_*.csv");
			hardwareResource = resources != null && resources.length > 0 ? resources[0] : null;
			
			if (hardwareResource != null) {
		        csvFileReader.setResource(resources != null && resources.length > 0 ? resources[0] : null);
		        
		        LineMapper<TruckDTO> truckHardwareLineMapper = createHardwareTruckLineMapper();
		        csvFileReader.setLineMapper(truckHardwareLineMapper);	
			} else {
				logger.info("No hardware file to read...Going to try software file...");
			}
			
		} catch (IOException e) {
			logger.warning("No files were found...move along...");
		}
 
        return csvFileReader;
    }
	
	private LineMapper<TruckDTO> createHardwareTruckLineMapper() {
        DefaultLineMapper<TruckDTO> truckHardwareLineMapper = new DefaultLineMapper<>();
 
        LineTokenizer truckLineTokenizer = createTruckLineTokenizer();
        truckHardwareLineMapper.setLineTokenizer(truckLineTokenizer);
 
        FieldSetMapper<TruckDTO> truckInformationMapper =
                createTruckInformationMapper();
        truckHardwareLineMapper.setFieldSetMapper(truckInformationMapper);
 
        return truckHardwareLineMapper;
    }
	
	private LineTokenizer createTruckLineTokenizer() {
        DelimitedLineTokenizer truckLineTokenizer = new DelimitedLineTokenizer();
        truckLineTokenizer.setDelimiter(",");
        truckLineTokenizer.setNames(new String[]{
                "vin",
                "hardwarecode"
        });
        return truckLineTokenizer;
    }
	
	private FieldSetMapper<TruckDTO> createTruckInformationMapper() {
        BeanWrapperFieldSetMapper<TruckDTO> truckHardwareInformationMapper =
                new BeanWrapperFieldSetMapper<>();
        truckHardwareInformationMapper.setTargetType(TruckDTO.class);
        return truckHardwareInformationMapper;
    }
	

	@Override
	public TruckDTO read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		return null;
	}

}
