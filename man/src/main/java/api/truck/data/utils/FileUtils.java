package api.truck.data.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class FileUtils {
	
	static Logger logger = Logger.getLogger(FileUtils.class.getName());
	
	public static void createToBeProcessedFolder () {
		File file = new File(System.getProperty("user.home") + "\\to_be_processed\\");
	    if (!file.exists()) {
	        if (file.mkdir()) {
	        	logger.info("Folder to process Truck data created!" + file.getPath());
	        }
	    } else {
	    	logger.info("Folder to process Truck data already created!");
	    }

	}
	
	public static void createProcessedFolder () {
		File file = new File(System.getProperty("user.home") + "\\processed\\");
	    if (!file.exists()) {
	        if (file.mkdir()) {
	        	logger.info("Folder to have processed files created!" + file.getPath());
	        }
	    } else {
	    	logger.info("Folder for processed files already created!");
	    }
	}
	
	//This function will rename all soft_* and hard_* files to software_file and hardware_file
	public static Resource[] renameFile (String component, ApplicationContext applicationContext) throws IOException {
		FileSystemResource file = new FileSystemResource(new File(System.getProperty("user.home") + "\\to_be_processed\\"));
		Resource[] resources;
		String originalFileName=component != null && component.equals("hardware") ? "/hard_*.csv" : "/soft_*.csv";
		String finalFileName=component != null && component.equals("hardware") ? "hardware_file.csv" : "software_file.csv";
		String finalFileNamePath=component != null && component.equals("hardware") ? "/hardware_file.csv" : "/software_file.csv";
		
		resources = applicationContext.getResources("file:" + file.getPath() + originalFileName);
		
		if (resources != null && resources.length > 0) {
			Path source = Paths.get(file.getPath()+"/"+resources[0].getFilename());
			try {
				Files.move(source, source.resolveSibling(finalFileName));
			} catch (IOException e) {
				logger.severe("Something went wrong renaming the file! " + e.getLocalizedMessage());
			}
		} else {
			File f = new File(file.getPath() + finalFileNamePath);
			try {
				f.createNewFile();
			} catch (IOException e) {
				logger.severe("Something went wrong creating blank file! " + e.getLocalizedMessage());
			}
		}
		resources = applicationContext.getResources("file:"+file.getPath()+finalFileNamePath);
			
		return resources;
	}
	
	//This function will move the processed file to the processed folder, marked with the timestamp
	public static void moveFile (String component, ApplicationContext applicationContext) throws IOException {
		String fileprefix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		FileSystemResource file = new FileSystemResource(new File(System.getProperty("user.home") + "\\to_be_processed\\"));
		FileSystemResource file2 = new FileSystemResource(new File(System.getProperty("user.home") + "\\processed\\"));
		String finalFileNamePath=component != null && component.equals("hardware") ? "/hardware_file.csv" : "/software_file.csv";
		Resource[] resources;	
		
		resources = applicationContext.getResources("file:" + file.getPath() + finalFileNamePath);
		
		if (resources != null && resources.length > 0) {
			Path source = Paths.get(file.getPath()+"/"+resources[0].getFilename());
			Path dest = Paths.get(file2.getPath()+"/"+fileprefix+"_"+resources[0].getFilename());
			Files.move(source, dest);
		}
	}
}
