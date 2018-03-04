package com.aarestu.controller;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.StorageClass;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlobDirectory;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;



@Controller
@RequestMapping("/storage")// storage is the name of controller
public class StorageControler {

	private String  SUFFIX="/";
	private String status_failure = "{\"status\":\"fail\"}";
	private String status_ok = "{\"status\":\"ok\"}";
	
	
	@RequestMapping(value = "/putamazon", method = RequestMethod.POST)
	public @ResponseBody
	String AmazonPutObjectStream(@RequestParam(value = "acname") String acName, 
			                        @RequestParam(value = "ackey") String acKey,
			                        @RequestParam(value = "regName") String regName,
			                        @RequestParam(value = "bucketName") String bucketName, 
			                        @RequestParam(value = "folderName") String folderName,
			                        @RequestParam(value = "storageClass") String storageClass,
			                        @RequestParam(value = "objectSize") Integer objectSize) {
		try {
			    String response="{\"delays\":[";
			    AmazonS3Client client=createS3ClientAccount(acName, acKey, regName);
			    RandomInputStream ris=new RandomInputStream(objectSize);
			    String objectStreamName=ris.streamNameReviser(ris, folderName);
			    RandomInputStream objectStream=new RandomInputStream(objectSize);
			    ObjectMetadata omd = new ObjectMetadata();
			    omd.setContentLength(objectStream.available());
			   
			    String keyObject = folderName + SUFFIX + objectStreamName;
			    for (int sc = 0; sc < StorageClass.values().length; sc++) {
			    	  
			    	   if(StorageClass.values()[sc].toString().equals(storageClass)){
	  					  long startTime = System.currentTimeMillis();
	  					
	  					   client.putObject(new PutObjectRequest(bucketName, keyObject, objectStream,omd)
	  					   .withCannedAcl(CannedAccessControlList.PublicReadWrite).withStorageClass(StorageClass.values()[sc]));
	  					   
	  					   long putLatency=System.currentTimeMillis() - startTime;
	  					   
	  					   response+="{\""+bucketName+"\":"+"\""+objectStreamName+"\":"+"\""+objectSize+"\":"+"\""+Long.toString(putLatency)+"\"}";
	 			           response+="]}";
	 					  break;
						
	  				}
	  			} 	
			        
			    return response;
		 		    
			   //System.out.println("object"+" "+objectStreamName +" "+"uploaded in"+" "+bucketName+SUFFIX+folderName); 
			   //System.out.println(putLatency);
			   //return Long.toString(putLatency);
			     
		 } catch (Exception e) {
			
			e.printStackTrace();
			return status_failure;
		}
	}
	
	
	@RequestMapping(value = "/getamazon", method = RequestMethod.GET)
	public @ResponseBody
	String AmazonGetObjectStream(@RequestParam(value = "acname") String acName, 
			                        @RequestParam(value = "ackey") String acKey,
			                        @RequestParam(value = "regName") String regName,
			                        @RequestParam(value = "bucketName") String bucketName,
			                        @RequestParam(value = "folderName") String folderName,
			                        @RequestParam(value = "desAddress") String desAddress)
			                         {
		
		try {
               
			    String response="{\"delays\":[";  
			    AmazonS3Client client=createS3ClientAccount(acName, acKey, regName);
			    ObjectListing listing = client.listObjects(new ListObjectsRequest().withBucketName(bucketName));
			   
			    for (S3ObjectSummary objectSummary : listing.getObjectSummaries()) {
				        
				      if(objectSummary.getKey().startsWith(folderName+SUFFIX) && !objectSummary.getKey().equalsIgnoreCase(folderName+SUFFIX)){
				    	    long startTime = System.currentTimeMillis(); 
				    	    client.getObject(
		                             new GetObjectRequest(bucketName, objectSummary.getKey()),
		                             new File(desAddress+SUFFIX+folderName+SUFFIX+getObjectName(objectSummary.getKey())));
   	                         long getLatency = System.currentTimeMillis() - startTime;
				        	 
				        	 response+="{\""+objectSummary.getBucketName()+"\":"+"\""+objectSummary.getKey()+"\":"+"\""+objectSummary.getSize()+"\":"+"\""+Long.toString(getLatency)+"\"},";
				        } 
				  }
			      if (response.charAt(response.length()-1)==',') {
					response = response.substring(0, response.length()-1);
				   }
			       response+="]}";
			  return response;
			      //return obj.toString();
		   } catch (Exception e) {
			
			e.printStackTrace();
			return status_failure;
		}
	}
	
	
	@RequestMapping(value = "/createfolderamazon", method = RequestMethod.POST)
	public @ResponseBody
	String AmazonFolderCreate(@RequestParam(value = "acname") String acName, 
			                        @RequestParam(value = "ackey") String acKey,
			                        @RequestParam(value = "regName") String regName,
			                        @RequestParam(value = "bucketName") String bucketName, 
			                        @RequestParam(value = "folderName") String folderName,
			                        @RequestParam(value = "storageClass") String storageClass) {
		try {
			   
			  AmazonS3Client client=createS3ClientAccount(acName, acKey, regName);
			  if(!client.doesBucketExist(bucketName+SUFFIX+folderName)){
	  		      
	  			  ObjectMetadata metadata = new ObjectMetadata();
	  	          metadata.setContentLength(0);
	  	          InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
	              for (int sc = 0; sc < StorageClass.values().length; sc++) {
					if(StorageClass.values()[sc].toString().equals(storageClass)){
						PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
			  	        folderName + SUFFIX, emptyContent, metadata).withStorageClass(StorageClass.values()[sc]);
			  	        client.putObject(putObjectRequest);
						break;
					}
				 } 
	              
	  	      }
			  return status_ok;
			      //return acKey;
		 } catch (Exception e) {
			
			e.printStackTrace();
			return status_failure;
		}
	}
	
	
	
	@RequestMapping(value = "/deletefolderamazon", method = RequestMethod.DELETE)
	public @ResponseBody
	String AmazonFolderDelete(@RequestParam(value = "acname") String acName, 
			                        @RequestParam(value = "ackey") String acKey,
			                        @RequestParam(value = "regName") String regName,
			                        @RequestParam(value = "bucketName") String bucketName, 
			                        @RequestParam(value = "folderName") String folderName) {
		try {
			   
			  AmazonS3Client client=createS3ClientAccount(acName, acKey, regName);
			  String key=folderName;
              ObjectListing objects = client.listObjects(bucketName, key);
              for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) 
              {
    	         client.deleteObject(bucketName, objectSummary.getKey());
    	         System.out.println(objectSummary.getKey());
              } 
			  return status_ok;
			      //return acKey;
		 } catch (Exception e) {
			
			e.printStackTrace();
			return status_failure;
		}
	}
	
	
	
	
	@RequestMapping(value = "/putazure", method = RequestMethod.POST)
	public @ResponseBody
	String AzurePutObjectStream(@RequestParam(value = "ackey") String acKey,
			                    @RequestParam(value = "containerName") String containerName, 
			                    @RequestParam(value = "folderName") String folderName,
			                    @RequestParam(value = "objectSize") Integer objectSize) {
		try {
      
			  String response="{\"delays\":["; 
			  CloudStorageAccount azureAccount = CloudStorageAccount.parse(acKey);
			  CloudBlobClient azureClient = azureAccount.createCloudBlobClient();
			
			  RandomInputStream ris=new RandomInputStream(objectSize);
			  String objectStreamName=ris.streamNameReviser(ris, folderName);
			  RandomInputStream objectStream=new RandomInputStream(objectSize);
			   
			  ObjectMetadata omd = new ObjectMetadata();
			  omd.setContentLength(objectStream.available());
			  
			  CloudBlockBlob blob = azureClient.getContainerReference(containerName + SUFFIX + folderName)
						.getBlockBlobReference(objectStreamName);
				  
			        long startTime = System.currentTimeMillis();
			        
			        blob.upload(objectStream, objectStream.available());
			        
			        long putLatency = System.currentTimeMillis() - startTime;
			       
			        System.out.println("object" + " " + objectStreamName + " " + "uploaded in" + " " + containerName + SUFFIX+ folderName);
			        
			        //System.out.println("objectSize===>"+objectSize);
			        response+="{\""+containerName+"\":"+"\""+objectStreamName+"\":"+"\""+objectSize+"\":"+"\""+Long.toString(putLatency)+"\"}";
			        response+="]}";
		
					return response;
		 } catch (Exception e) {
			
			e.printStackTrace();
			return status_failure;
		}
	}
	
	
	@RequestMapping(value = "/getazure", method = RequestMethod.GET)
	public @ResponseBody
	String AzureGetObjectStream( @RequestParam(value = "ackey") String acKey,
			                        @RequestParam(value = "containerName") String containerName,
			                        @RequestParam(value = "folderName") String folderName,
			                        @RequestParam(value = "desAddress") String desAddress)
			                         {
		
		try {
              String response="{\"delays\":["; 
			  CloudStorageAccount azureAccount = CloudStorageAccount.parse(acKey);
			  CloudBlobClient azureClient = azureAccount.createCloudBlobClient();
			
			
			 for (CloudBlobContainer c : azureClient.listContainers()) {
				  
				if (c.getName().equalsIgnoreCase(containerName)) {
					
					for (ListBlobItem blobItem : c.listBlobs(null, true)) {
						
						if (!(blobItem instanceof CloudBlobDirectory)  && blobItem.getParent().getPrefix().equalsIgnoreCase(folderName+SUFFIX)) {
							CloudBlob blob = (CloudBlob) blobItem;
						

							if ((blob.getProperties().getLength()) != 0) {
								
								new File(desAddress+SUFFIX+folderName).mkdirs();// create folder
								
								long startTime = System.currentTimeMillis(); 
								
								blob.downloadToFile(desAddress+SUFFIX+folderName+SUFFIX+GetSubString(blob.getName(), 1));
								
								long estimateTime = System.currentTimeMillis() - startTime;
								
								//response+="{\""+GetSubString(blob.getName(),1)+"\":"+"\""+Long.toString(estimateTime)+"\"},";
								response+="{\""+containerName+"\":"+"\""+GetSubString(blob.getName(),1)+"\":"+"\""+blob.getProperties().getLength()+"\":"+"\""+Long.toString(estimateTime)+"\"},";
								
								
								System.out.println(blob.getName() + "   " + blob.getProperties().getLength() + " B downladed");
							}
						}
					}
				}
			} // for
			if (response.charAt(response.length()-1)==',') {
				response = response.substring(0, response.length()-1);
			}
			response+="]}";
			//System.out.println(response); 
			return response;
		     
		 } catch (Exception e) {
			
			e.printStackTrace();
			return status_failure;
		}
	}
	
	
	@RequestMapping(value = "/createfolderazure", method = RequestMethod.POST)
	public @ResponseBody
	String AzureFolderCreate(@RequestParam(value = "ackey") String acKey,
			                    @RequestParam(value = "containerName") String containerName, 
			                    @RequestParam(value = "folderName") String folderName) {
		try {
      
			   CloudStorageAccount azureAccount = CloudStorageAccount.parse(acKey);
			   CloudBlobClient azureClient = azureAccount.createCloudBlobClient();
			
			
			   CloudBlobContainer blobContainer =azureClient.getContainerReference(containerName);
	  		   ObjectMetadata metadata = new ObjectMetadata();
	           metadata.setContentLength(0);
	           
	           InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
	           String f=folderName+SUFFIX;
	           CloudBlockBlob blob=blobContainer.getBlockBlobReference(f);
	           blob.upload(emptyContent, 0);
	           
	           return status_ok;
		   } catch (Exception e) {
			
			e.printStackTrace();
			return status_failure;
		}
	}
	
	@RequestMapping(value = "/deletefolderazure", method = RequestMethod.DELETE)
	public @ResponseBody
	String AzureFolderDelete(@RequestParam(value = "ackey") String acKey,
			                    @RequestParam(value = "containerName") String containerName, 
			                    @RequestParam(value = "folderName") String folderName) {
		try {
      
			   CloudStorageAccount azureAccount = CloudStorageAccount.parse(acKey);
			   CloudBlobClient azureClient = azureAccount.createCloudBlobClient();
			  
			
			   for (ListBlobItem item : azureClient.getContainerReference(containerName).listBlobs(folderName+SUFFIX, true)) {
					if (item.getParent().getBlockBlobReference(folderName + SUFFIX).deleteIfExists()) {
						System.out.println(containerName+ SUFFIX + folderName + " deleted");
						break;
					}
				} 
			   return status_ok;
		   } catch (Exception e) {
			
			e.printStackTrace();
			return status_failure;
		}
	}
	
	public AmazonS3Client createS3ClientAccount(String acname, String ackey, String regname) {
		
		AmazonS3Client s3client=null;
   	    for (int i = 0; i < Regions.values().length; i++) {
   	    	if(Regions.values()[i].toString().equals(regname)){
   	    		AWSCredentials credentials=new BasicAWSCredentials(acname,ackey);
   	    		s3client=new AmazonS3Client(credentials);
   	    		s3client.setRegion(Region.getRegion(Regions.values()[i]));
   	    		break;
   	    	}
   	     }
   	    return s3client;
   	 }
	
	
	public String getObjectName(String keyObj) {
		   
 	   String objectName="";
 	   String[] parts = keyObj.split(SUFFIX);
        objectName=parts[1];
        
        return objectName;
	   }

	public String GetSubString(String str, int location) {

		String name = null;
		String[] parts = str.split(SUFFIX);
		name = parts[location];
		return name;
	}
	
	
}