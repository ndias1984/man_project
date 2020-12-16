package api.truck.data.batch.listener;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import api.truck.data.utils.FileUtils;

public class JobCompletionListener extends JobExecutionListenerSupport {
	
	Logger logger = Logger.getLogger(JobCompletionListener.class.getName());
	
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.STARTED) {
			try {
				logger.info("Let's see if there is a new Hardware file to be processed and renamed...");
				FileUtils.renameFile("hardware", applicationContext);
				logger.info("Let's see if there is a new Software file to be processed and renamed...");
				FileUtils.renameFile("software", applicationContext);
			} catch (IOException e) {
				logger.severe("Not possible to create file..." + e.getLocalizedMessage());
			}
			logger.info("Starting to import MAN truck data...");
		}
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			try {
				logger.info("Moving Hardware file to processed folder...");
				FileUtils.moveFile("hardware", applicationContext);
				logger.info("Moving Software file to processed folder...");
				FileUtils.moveFile("software", applicationContext);
			} catch (IOException e) {
				logger.severe("Not possible to move file..." + e.getLocalizedMessage());
			}
			logger.info("Truck data fully persisted in DB with no errors.");
		}
	}
}
