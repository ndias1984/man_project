package api.truck.data;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@SpringBootApplication
public class ManApplication  {
	
	private static final String JOB_NAME = "ImportTruckData";
	
	@Autowired
    JobLauncher jobLauncher;
     
    @Autowired
    Job truckDataJobConfig;

	public static void main(String[] args) {
		SpringApplication.run(ManApplication.class, args);
	}
	
	//Read the man truck data in the background with a 15 minute interval
	@Scheduled(cron = "0 */1 * * * ?")
    public void perform() throws Exception 
    {
        JobParameters params = new JobParametersBuilder()
                .addString(JOB_NAME, String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(truckDataJobConfig, params);
    }

}
