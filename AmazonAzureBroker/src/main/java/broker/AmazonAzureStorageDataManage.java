package broker;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.text.ParseException;

import org.cloudbus.intercloudsim.config.InterCloudSimConfiguration;

import com.microsoft.azure.storage.StorageException;

public class AmazonAzureStorageDataManage {
	static String SUFFIX="/";
	static File fMigrationTime;
	static PrintWriter outfMigrationTime;
	   
	
	
	public static void main(String[] args) throws IOException, InvalidKeyException, URISyntaxException, StorageException, ParseException {
		     
		 if(args.length > 0)
				InterCloudSimConfiguration.DEFAULT_PATH = args[0];// Set configure your path 
			    
			else{
				System.out.println("There is no config file.");
				System.exit(0);
			}
		    
		    
		     // a simple example
		     RepMigData rmd=new RepMigData();
		     rmd.createS3Clients();
		     rmd.createAmazonBuckets();
		     rmd.createCloudBlobClients();
		     rmd.createAzureContainers();
		    
		    
		     outputFileCreate(0);
		     for (int i = 0; i < 1; i++) {
		    	 System.out.println("UID===>"+i);
		    	 
			     rmd.bucketTransferTime(outfMigrationTime);
			     filesClose();
			}  
	  }
	
	public static String GetSubString(String str, int location) {

		String name = null;
		String[] parts = str.split(SUFFIX);
		name = parts[location];
		return name;
	}
    
	private static void  outputFileCreate(int i){
		
		try {
			   
			 File directoryC = new File("."+"/"+"Time"+"/");
	  	       if (!directoryC.exists()) {
	  	    	 directoryC.mkdirs();
	  	      }
			
			   fMigrationTime=new File(directoryC+"/"+"bucketMigrationTime_"+i+".csv");
			   outfMigrationTime=new PrintWriter(fMigrationTime);
			   outfMigrationTime.println("sourceDC"+","+"destinationDC"+","+"objectName"+","+"objectSize"+","+"migrationTime");
			
		  } catch (Exception e) {
			// TODO: handle exception
	     }
	}
	
	private static void filesClose(){
	    outfMigrationTime.close();
		  
		
	}
  }
