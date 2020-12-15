package api.truck.data.batch.listener;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class SkipStepListener extends StepExecutionListenerSupport {
	
	Logger logger = Logger.getLogger(SkipStepListener.class.getName());
    
	public ExitStatus afterStep(StepExecution stepExecution) {
    	ClassLoader cl = this.getClass().getClassLoader(); 
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
    	Resource[] resources = null;
		try {
			resources = resolver.getResources("classpath*:/to_be_processed/hard_*.csv");
			
		} catch (IOException e) {
			logger.warning("Something went wrong with the file, check log for further details...");
			return ExitStatus.FAILED;
		}
		
		logger.info(resources != null && resources.length > 0 ? "Hardware files available to read..." : "No Hardware files available to read...");
               
        return resources != null && resources.length > 0 ? ExitStatus.COMPLETED : ExitStatus.NOOP ;
    }
}
