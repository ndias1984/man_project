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

public class SoftwareReader implements ItemReader<TruckDTO> {
	
	Logger logger = Logger.getLogger(SoftwareReader.class.getName());
	
	@Bean
    public ItemReader<TruckDTO> itemReader() {
		FlatFileItemReader<TruckDTO> csvFileReader = new FlatFileItemReader<>();
		ClassLoader cl = this.getClass().getClassLoader(); 
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
		Resource[] resources;
		Resource softwareResource;
		try {
			resources = resolver.getResources("classpath*:/to_be_processed/soft_*.csv");
			softwareResource = resources != null && resources.length > 0 ? resources[0] : null;
			
			if (softwareResource != null) {
		        csvFileReader.setResource(resources != null && resources.length > 0 ? resources[0] : null);
		 
		        LineMapper<TruckDTO> truckSoftwareLineMapper = createsoftwareTruckLineMapper();
		        csvFileReader.setLineMapper(truckSoftwareLineMapper);	
			} else {
				logger.info("No software file to read...");
			}
			
		} catch (IOException e) {
			logger.warning("No files were found...move along...");
		}
 
        return csvFileReader;
    }
	
	private LineMapper<TruckDTO> createsoftwareTruckLineMapper() {
        DefaultLineMapper<TruckDTO> truckSoftwareLineMapper = new DefaultLineMapper<>();
 
        LineTokenizer truckLineTokenizer = createTruckLineTokenizer();
        truckSoftwareLineMapper.setLineTokenizer(truckLineTokenizer);
 
        FieldSetMapper<TruckDTO> truckInformationMapper =
                createTruckInformationMapper();
        truckSoftwareLineMapper.setFieldSetMapper(truckInformationMapper);
 
        return truckSoftwareLineMapper;
    }
	
	private LineTokenizer createTruckLineTokenizer() {
        DelimitedLineTokenizer truckLineTokenizer = new DelimitedLineTokenizer();
        truckLineTokenizer.setDelimiter(",");
        truckLineTokenizer.setNames(new String[]{
                "vin",
                "softwarecode"
        });
        return truckLineTokenizer;
    }
	
	private FieldSetMapper<TruckDTO> createTruckInformationMapper() {
        BeanWrapperFieldSetMapper<TruckDTO> trucksoftwareInformationMapper =
                new BeanWrapperFieldSetMapper<>();
        trucksoftwareInformationMapper.setTargetType(TruckDTO.class);
        return trucksoftwareInformationMapper;
    }
	

	@Override
	public TruckDTO read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		return null;
	}

}
