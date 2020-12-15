package api.truck.data.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Configuration
@EnableBatchProcessing
@PropertySource("classpath:truck.properties")
public class TruckDataJobConfig {
	
	private static final String PROCESS_SOFTWARE_HARDWARE_JOB = "processSoftwareHardwareJob";
	private static final String PROCESS_HARDWARE_STEP = "processHardwareData";	
	private static final String PROCESS_SOFTWARE_STEP = "processSoftwareData";	
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
    private TruckConfigurationRepository truckConfigurationRepository;
	
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
		return jobBuilderFactory.get(PROCESS_SOFTWARE_HARDWARE_JOB)
				.incrementer(new RunIdIncrementer()).listener(listener())
				.flow(processHardwareData()).next(processSoftwareData()).end().build();
	}

	@Bean
	public Step processHardwareData() {
		return stepBuilderFactory.get(PROCESS_HARDWARE_STEP).<TruckDTO, TruckDTO> chunk(150)
				.reader(new HardwareReader().itemReader())
				.processor(new HardwareProcessor(hardwareCodesPresent,hardwareCodesNotPresent,truckConfigurationRepository))
				.writer(new HardwareWriter(truckConfigurationRepository))
				.build();
	}
	
	@Bean
	public Step processSoftwareData() {
		return stepBuilderFactory.get(PROCESS_SOFTWARE_STEP).<TruckDTO, TruckDTO> chunk(150)
				.reader(new SoftwareReader().itemReader())
				.processor(new SoftwareProcessor(softwareCodesPresent,softwareCodesNotPresent,truckConfigurationRepository))
				.writer(new SoftwareWriter(truckConfigurationRepository))
				.build();
	}

	@Bean
	public JobExecutionListener listener() {
		return new JobCompletionListener();
	}

}
