package broker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.StorageClass;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;

import optimalCost.objectDatacenterSpecification;

public class RepMigData {
	
	private String SUFFIX="/";
	private HashMap<String, AmazonS3Client> s3ClientHashMap=new HashMap<>();
	private AmazonConnection amazonConnec;
	private HashMap<String, CloudBlobClient> GRSCloudBlobClient=new HashMap<>();
    private HashMap<String, CloudBlobClient> LRSCloudBlobClient=new HashMap<>();
    private AzureConnection azureConnec;
	
	
	//1. create Amazon S3Clients
	public void createS3Clients() {
	    objectDatacenterSpecification.readSpecificationObjectWorkload();
	    objectDatacenterSpecification.readAmazonKey();
	    amazonConnec=new AmazonConnection(objectDatacenterSpecification.amazonKey);  
	    s3ClientHashMap.putAll(amazonConnec.createS3ClientAccount());  
    }
	
	//2. create Amazon buckets, one bucket in each DC, which has the same name of DC
	public void createAmazonBuckets(){
		for(String dc: s3ClientHashMap.keySet() ){
	    	   System.out.println(dc);
	           if(amazonConnec.isDNSName(dc)){
	        	   amazonConnec.createBucket(s3ClientHashMap.get(dc), dc);   
	           }
	           else{
	        	   System.out.println("the bucke name"+ dc +"  is not compitable with DNSNmae"); 
	           }
	    	
      }
	}
	
	//3. create all Containers in GRS and LRS in Azure
	public void createCloudBlobClients() throws InvalidKeyException, URISyntaxException, StorageException{
		
		 objectDatacenterSpecification.readSpecificationObjectWorkload();
		 objectDatacenterSpecification.readAzureKey();
	     azureConnec=new AzureConnection(objectDatacenterSpecification.azureKey);
	     //System.out.println(objectDatacenterSpecification.azureKey);
	     
	     GRSCloudBlobClient.putAll(azureConnec.GRSCreateCloudBlobClientAccount());
	     LRSCloudBlobClient.putAll(azureConnec.LRSCreateCloudBlobClientAccount());
	}
   
	//4. create Azure buckets, one bucket in each DC, which has the same name of DC
	public void createAzureContainers() throws URISyntaxException, StorageException{
		for(String dc: GRSCloudBlobClient.keySet() ){
	    	   
	    	   azureConnec.createContainer(GRSCloudBlobClient.get(dc), dc);
	     }
	     for(String dc: LRSCloudBlobClient.keySet() ){
	    	   
	    	   azureConnec.createContainer(LRSCloudBlobClient.get(dc), dc);
	     }
	}
	

	//This function calculates the time spent on the a bucket migration between 3 DCs.  
	public void uploadDataIntoBucket() throws FileNotFoundException, URISyntaxException, StorageException, IOException{
		
		amazonConnec.AmazonCreateFolder(s3ClientHashMap.get("am-us-east"), "am-us-east", "UID1",StorageClass.Standard.toString());
		amazonConnec.AmazonCreateFolder(s3ClientHashMap.get("am-jap-east"), "am-jap-east", "UID1",StorageClass.Standard.toString());
		//azureConnec.AzureCreateFolder(LRSCloudBlobClient.get("az-eu-north"), LRSCloudBlobClient.get("az-eu-north").getContainerReference("az-eu-north"), "UID3");
		//azureConnec.AzureCreateFolder(GRSCloudBlobClient.get("az-us-south"), GRSCloudBlobClient.get("az-us-south").getContainerReference("az-us-south"), "UID3");
        
		int totalBucketSize=0;// the unit is KByte
		
		while(totalBucketSize<=18432){
			
			Random rand = new Random();
			int sizeObj= rand.nextInt((100 - 1) + 1) + 1;
			totalBucketSize=totalBucketSize+sizeObj;
			RandomInputStream ris=new RandomInputStream(1024*sizeObj);
			String sn=ris.streamNameReviser(ris, "UID1");
            System.out.println(sn);		      
		    
            
            //Upload Stream  
             amazonConnec.AmazonUploadObjectStream(s3ClientHashMap.get("am-us-east"), "am-us-east", "UID1",  new RandomInputStream(1024*sizeObj), sn,
		    		                              StorageClass.ReducedRedundancy.toString());
		    
             amazonConnec.AmazonUploadObjectStream(s3ClientHashMap.get("am-jap-east"), "am-jap-east", "UID1",  new RandomInputStream(1024*sizeObj), sn,
                     StorageClass.ReducedRedundancy.toString());
             
          
		      
		  }
		
	 }
	  
	
	 public void bucketTransferTime( PrintWriter out) throws URISyntaxException, StorageException, FileNotFoundException, IOException {
		
		 
		 ArrayList<MigratedObject> mo=new ArrayList<>(); 
		 mo.clear();
		 mo.addAll(
		 azureConnec.AzureToAmzonStorageFolder(LRSCloudBlobClient.get("az-jap-west"), "az-jap-west", "UID4", 
				                             s3ClientHashMap.get("am-us-west"), "am-us-west", "UID4", StorageClass.ReducedRedundancy.toString()));	 
	     
		 for (int i = 0; i < mo.size(); i++) {
			 out.println("az-jap-west"+","+"am-us-west"+","+mo.get(i).getNameObject()+","+mo.get(i).getObjectSize()+","+mo.get(i).getObjectMigrationTime());
		 }
	   	 
     }
	 
	  
	 public void createFolder() throws FileNotFoundException, URISyntaxException, StorageException, IOException {
		
		 azureConnec.AzureCreateFolder(LRSCloudBlobClient.get("az-eu-north"), "az-eu-north", "UIDTest");
	  }
	 
	 
	// This function gets a string and then one part of this string based on the  "location" parameter is removed.
		public String RemoveSubString(String str, int location) {

			String convertedString = "";
			String[] parts = str.split(SUFFIX);

			parts[location] = "";
			for (int index = 0; index < parts.length; index++) {
				if (parts[index] != "") {
					convertedString = convertedString + parts[index] + SUFFIX;
				}

			}
			return convertedString;
		}

}
