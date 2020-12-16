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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import api.truck.data.dto.TruckDTO;
import api.truck.data.utils.FileUtils;

public class SoftwareReader implements ItemReader<TruckDTO> {
	
	Logger logger = Logger.getLogger(SoftwareReader.class.getName());
	
	@Bean
    public ItemReader<TruckDTO> itemReader(ApplicationContext applicationContext) {
		FlatFileItemReader<TruckDTO> csvFileReader = new FlatFileItemReader<>();
		Resource[] resources;
		Resource softwareResource;
		try {
			resources = FileUtils.renameFile("software", applicationContext);
			softwareResource = resources != null && resources.length > 0 ? resources[0] : null;

			if (softwareResource != null) {
		        csvFileReader.setResource(resources != null && resources.length > 0 ? resources[0] : null);
		        
		        LineMapper<TruckDTO> truckSoftwareLineMapper = createSoftwareTruckLineMapper();
		        csvFileReader.setLineMapper(truckSoftwareLineMapper);	
			}
			
		} catch (IOException e) {
			logger.warning("No files were found...move along...");
		}
		csvFileReader.setStrict(false);
        return csvFileReader;
    }
	
	private LineMapper<TruckDTO> createSoftwareTruckLineMapper() {
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
