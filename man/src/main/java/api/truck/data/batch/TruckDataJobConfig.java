package api.truck.data.batch;

import java.util.logging.Logger;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import api.truck.data.batch.listener.JobCompletionListener;
import api.truck.data.batch.step.HardwareProcessor;
import api.truck.data.batch.step.HardwareReader;
import api.truck.data.batch.step.HardwareWriter;
import api.truck.data.batch.step.SoftwareProcessor;
import api.truck.data.batch.step.SoftwareReader;
import api.truck.data.batch.step.SoftwareWriter;
import api.truck.data.dto.TruckDTO;
import api.truck.data.repository.TruckConfigurationRepository;
import api.truck.data.utils.FileUtils;

@Configuration
@EnableBatchProcessing
@PropertySource("classpath:truck.properties")
public class TruckDataJobConfig {
	
	Logger logger = Logger.getLogger(TruckDataJobConfig.class.getName());
	
	private static final String PROCESS_SOFTWARE_HARDWARE_JOB = "processSoftwareHardwareJob";
	private static final String PROCESS_HARDWARE_STEP = "processHardwareData";	
	private static final String PROCESS_SOFTWARE_STEP = "processSoftwareData";	
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
    private TruckConfigurationRepository truckConfigurationRepository;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Value("${hardware.codes.present}")
    private String hardwareCodesPresent;
	
	@Value("${hardware.codes.notPresent}")
    private String hardwareCodesNotPresent;
	
	@Value("${software.codes.present}")
    private String softwareCodesPresent;
	
	@Value("${software.codes.notPresent}")
    private String softwareCodesNotPresent;
	
	@Bean
	public Job processSoftwareHardwareJob() {
		logger.info("Creating folders for csv files...");
		FileUtils.createToBeProcessedFolder(); // Creates de folder to recieve the files to process
		FileUtils.createProcessedFolder(); // Creates a folder to recieve all processed files
		logger.info("Folders for csv files created...");
		
		return jobBuilderFactory.get(PROCESS_SOFTWARE_HARDWARE_JOB)
				.incrementer(new RunIdIncrementer()).listener(listener())
				.flow(processHardwareData(applicationContext))
				.next(processSoftwareData(applicationContext))
				.end()
				.build();
	}

	@Bean
	public Step processHardwareData(ApplicationContext applicationContext) {
		return stepBuilderFactory.get(PROCESS_HARDWARE_STEP).<TruckDTO, TruckDTO> chunk(150)
				.reader(new HardwareReader().itemReader(applicationContext))
				.processor(new HardwareProcessor(hardwareCodesPresent,hardwareCodesNotPresent,truckConfigurationRepository))
				.writer(new HardwareWriter(truckConfigurationRepository))
				.build();
	}
	
	@Bean
	public Step processSoftwareData(ApplicationContext applicationContext) {
		return stepBuilderFactory.get(PROCESS_SOFTWARE_STEP).<TruckDTO, TruckDTO> chunk(150)
				.reader(new SoftwareReader().itemReader(applicationContext))
				.processor(new SoftwareProcessor(softwareCodesPresent,softwareCodesNotPresent,truckConfigurationRepository))
				.writer(new SoftwareWriter(truckConfigurationRepository))
				.build();
	}

	@Bean
	public JobExecutionListener listener() {
		return new JobCompletionListener();
	}

}
