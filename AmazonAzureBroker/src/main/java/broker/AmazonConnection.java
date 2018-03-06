package broker;



import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.BucketLoggingConfiguration;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.SetBucketLoggingConfigurationRequest;
import com.amazonaws.services.s3.model.StorageClass;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

public class AmazonConnection {
	
	private String  SUFFIX="/";
	private AzureConnection azureConnec=new AzureConnection();        
	private HashMap<String, AmazonKey> amKey=new HashMap<>();
	
	public AmazonConnection() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AmazonConnection(HashMap<String, AmazonKey> amKey) {
		super();
		this.amKey = amKey;
	}

    //1. This function creates S3Client for each region we require
	public HashMap<String, AmazonS3Client> createS3ClientAccount() {
		
      HashMap<String, AmazonS3Client> S3ClientHashMap=new HashMap<>();
      for (String dcName : amKey.keySet()) {
    	  
    	   for (int i = 0; i < Regions.values().length; i++) {
			
    		   if(Regions.values()[i].toString().equals(amKey.get(dcName).region)){
    			   AWSCredentials credentials=new BasicAWSCredentials(amKey.get(dcName).accessKey,amKey.get(dcName).secretKey);
    			   AmazonS3Client s3client=new AmazonS3Client(credentials);
    		   	   s3client.setRegion(Region.getRegion(Regions.values()[i]));
    		   	   S3ClientHashMap.put(dcName, s3client);
    		   }
    		}   
    	  } 
        return S3ClientHashMap;
	 }	

	
	//2. This function creates a bucket in S3
	public void createBucket( AmazonS3 client, String bucketName) {
		 try {
              if(!(client.doesBucketExist(bucketName)))
             {    
         	 	 CreateBucketRequest request = new CreateBucketRequest(bucketName);
         	 	 //
           	     request.setCannedAcl(CannedAccessControlList.PublicReadWrite);
           	     request.setCannedAcl(CannedAccessControlList.LogDeliveryWrite);
           	     client.createBucket(request);
           	     System.out.println("bucket"+"  "+bucketName+"  "+ "created."); 
             }
             else{
            	  System.out.println("The requested bucket "+bucketName+" was already created.");
              }
         
         
          } catch (AmazonServiceException ase) {
                   System.out.println("Caught an AmazonServiceException, which " +
         		    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
                    System.out.println("Error Message:    " + ase.getMessage());
                    System.out.println("HTTP Status Code: " + ase.getStatusCode());
                    System.out.println("AWS Error Code:   " + ase.getErrorCode());
                    System.out.println("Error Type:       " + ase.getErrorType());
                    System.out.println("Request ID:       " + ase.getRequestId());
         
          } catch (AmazonClientException ace) {
                    System.out.println("Caught an AmazonClientException, which " +
         		    "means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
                    System.out.println("Error Message: " + ace.getMessage());
           }
		
	}
	
      
   // This function creates a folder in a bucket. For hot-spot, the storageClassCode is 1, otherwise is 0 (for cold-spot)
  	public void AmazonCreateFolder(AmazonS3 client, String bucketName, String folderName,  String storageClass) {
  	   
  		
  		if(client.doesBucketExist(bucketName+SUFFIX+folderName)){
  			System.out.println("folder"+" "+bucketName+SUFFIX+folderName+" "+"already exists");
  			
  		}
  		else{
  		      // create meta-data for your folder and set content-length to 0
  	          ObjectMetadata metadata = new ObjectMetadata();
  	          metadata.setContentLength(0);

  	          // create empty content
  	          InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

  	         // create a PutObjectRequest passing the folder name suffixed by /
             for (int sc = 0; sc < StorageClass.values().length; sc++) {
				if(StorageClass.values()[sc].toString().equals(storageClass)){
					PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
		  	        folderName + SUFFIX, emptyContent, metadata).withStorageClass(StorageClass.values()[sc]);
		  	        // send request to S3 to create folder     
		  	        client.putObject(putObjectRequest);
					break;
					
				}
			 } 	         
  	           System.out.println("folder"+" "+bucketName+SUFFIX+folderName+" "+"was created");
  		 }
  	    
  	}
   
  	   
	// This function creates a log for operations. 
	 public void recordLog( AmazonS3 client, String bucketName) {
		      
		   // It takes the name of folder (key) and then delete it from the specified bucketname 
		   String desBucketNameLog=null;
		   for (Bucket listbucket : client.listBuckets()) {
			  if(listbucket.getName().equalsIgnoreCase("yaser2")){
				  desBucketNameLog="yaser2";
				  client.setBucketAcl("yaser2", CannedAccessControlList.LogDeliveryWrite);
			  }
			   
		 }
		  // client.setBucketAcl("yaser4", CannedAccessControlList.LogDeliveryWrite); This code enables LogDeliveryWrite for bucket "yaser4".
		   
		   //BucketLoggingConfiguration temp=new BucketLoggingConfiguration();
		   BucketLoggingConfiguration bucketLog=client.getBucketLoggingConfiguration(bucketName);
		   bucketLog.isLoggingEnabled();
		   
		   bucketLog.setDestinationBucketName(desBucketNameLog);
		   bucketLog.setLogFilePrefix("logs");
		   System.out.println("desLogNmae===>"+bucketLog.getDestinationBucketName());
		   System.out.println("Log for"+" "+bucketName+ " "+"enabled and recorded");
		   
        }  
	   
	  
       // This functions uploads an object file into data center. "sourceAdress" shows the address of the file should be uploaded. 
       public void AmazonUploadObjectFile(AmazonS3 client, String bucketName,String folderName,String objectName,
    		                     String srcAddress,  String storageClass )  throws IOException {
    	   File objectFile=new File(srcAddress);
    	   try {
	    	      String keyObject = folderName + SUFFIX + objectName;
	    	      for (int sc = 0; sc < StorageClass.values().length; sc++) {
	  				if(StorageClass.values()[sc].toString().equals(storageClass)){
	  					client.putObject(new PutObjectRequest(bucketName, keyObject,objectFile)
						.withCannedAcl(CannedAccessControlList.PublicReadWrite).withStorageClass(StorageClass.values()[sc]));
	  		  	        break;
	  					
	  				}
	  			} 	
	    	    
	    	       System.out.println("object"+" "+objectName +" "+"uploaded in"+" "+bucketName+SUFFIX+folderName);			
		      } catch (Exception e) {
		    	  System.out.println("the object"+" "+objectName + " was not found.");
		      }
		    
	    } 
  
       // This functions uploads an object stream into data center. 
       public void AmazonUploadObjectStream(AmazonS3 client, String bucketName,String folderName, InputStream objectStream, String objectStreamName,
    		                     String storageClass )  throws IOException {
    	   
    	   try {
    		      
    		      ObjectMetadata omd = new ObjectMetadata();
    		      omd.setContentLength(objectStream.available());
    		      
    		      String keyObject = folderName + SUFFIX + objectStreamName;
    		     
	    	      for (int sc = 0; sc < StorageClass.values().length; sc++) {
	  				if(StorageClass.values()[sc].toString().equals(storageClass)){
	  					
	  					client.putObject(new PutObjectRequest(bucketName, keyObject, objectStream,omd)
						.withCannedAcl(CannedAccessControlList.PublicReadWrite).withStorageClass(StorageClass.values()[sc]));
						break;
	  					
	  				}
	  			} 	
	    	     
	    	    System.out.println("object"+" "+objectStreamName +" "+"uploaded in"+" "+bucketName+SUFFIX+folderName);			
		      } catch (Exception e) {
		    	  
		    	  System.out.println("the object"+" "+objectStreamName + " was not found.");
		      }
		    
	    } 
       
       public  void AmazonDownloadObject(AmazonS3 client, String bucketName,String folderName,String desAddress) throws IOException{
              
    	   try{  
    		   
    		          ObjectListing listing = client.listObjects(new ListObjectsRequest().withBucketName(bucketName));
 				      long startTime = System.currentTimeMillis();
 				      for (S3ObjectSummary objectSummary : listing.getObjectSummaries()) {
 					          if(objectSummary.getKey().startsWith(folderName+SUFFIX) && !objectSummary.getKey().equalsIgnoreCase(folderName+SUFFIX)){
 					        	 client.getObject(
 	 		                             new GetObjectRequest(bucketName, objectSummary.getKey()),
 	 		                             new File(desAddress+SUFFIX+folderName+SUFFIX+getObjectName(objectSummary.getKey())));
 	     	                             System.out.println("object"+" "+getObjectName(objectSummary.getKey())+" "+ "downloaded");  
 					          } 
 					     }
 				         long estimatedTime = System.currentTimeMillis() - startTime;
 						 System.out.println("estimatedTime for upload object is  " + estimatedTime);
 				         
		    	  } catch (Exception e) {
		    	      System.out.println("The object " + " was not found.");
			           
		    	  }  
		}
       
       
       // This function deletes a folder in a bucket	
       public void AmazonDeleteFolder( AmazonS3 client, String bucketName, String folderName) throws IOException {
	      
		         // It takes the name of folder (key) and then delete it from the specified bucketName 
		       try{    
    	              String key=folderName;//bucketName+SUFFIX+folderName;
	                  ObjectListing objects = client.listObjects(bucketName, key);
	                  for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) 
	                  {
	        	         client.deleteObject(bucketName, objectSummary.getKey());
	        	         System.out.println(objectSummary.getKey());
	                  } 
		        }catch (Exception e) {
	    	            System.out.println("The folder "+folderName+ " "+ "does not exist");
                }
       }
       
    
       
         // This function transfer a Folder from DC d1 to DC d2. This function takes two folders and then all objects in source folder are transfered to destination folder.
   	     public ArrayList<MigratedObject> AmazonTransferStorageFolder(AmazonS3 srcClient, AmazonS3 desClient, String srcBN,String desBN, String srcFN ) {
   		  
   		          boolean existFlag=false; 
   		          ArrayList<MigratedObject> migratedObjectTime=new ArrayList<>(); 
   		          for (Bucket bucket : srcClient.listBuckets()) {
   				
   				      //System.out.println(" - " + bucket.getName()+"---"+bucket.getCreationDate()+"---"+bucket.getOwner().getDisplayName());
   				      if(bucket.getName().equalsIgnoreCase(srcBN)){
   				            existFlag=true;
   					        ObjectListing listing = srcClient.listObjects(new ListObjectsRequest().withBucketName(bucket.getName()));
   				            
   					       
   				           for (S3ObjectSummary objectSummary : listing.getObjectSummaries()) {
   					          if(objectSummary.getKey().startsWith(srcFN+SUFFIX)){
   					            //System.out.println("srcBucketName===>"+srcBucketName+"  "+srcKey);
   					        	
   					        	CopyObjectRequest request=new CopyObjectRequest(srcBN, objectSummary.getKey(), desBN, objectSummary.getKey());
   					        	long startTime = System.currentTimeMillis();
   					        	desClient.copyObject(request);
   					            long estimatedTime = System.currentTimeMillis() - startTime; 
   					            MigratedObject mot=new MigratedObject(objectSummary.getKey(), objectSummary.getSize(), estimatedTime);
   					            migratedObjectTime.add(mot);
   					          } 
   					       }
   				          
   						  //System.out.println("estimatedTime for  bucket migration is  " + estimatedTime);
   				          if(existFlag==true){
   					          break;
   				           }
   				     }//if
   		        }	
   		       if(existFlag==false){
   		    	   System.out.println("there is no "+srcBN+" in "+srcClient.getRegionName());
   		       }
   		      return migratedObjectTime; 
   	    }
     
   	  // This function moves a folder from Amazon to Azure DC.
   	     public void AmazonToAzureStorageFolder(AmazonS3Client srcClient, String srcBucketName, String srcFolderName, 
   	    		                               CloudBlobClient desClient,    String desContainer, String desFolderName) {
		   
   	    	try{
  	          
		       //1.determine whether the source bucket and folder exist in the source DC or not 
   	    		if(AmazonExistenceFolder(srcClient, srcBucketName, srcFolderName)){
   	    			
   	    			//2. The container and the folder (for user)  are created in destination DC if there are not previously.
   	    			if(!azureConnec.AzureExistenceContainer(desClient, desContainer)){
	    				     // 
	    				     azureConnec.createContainer(desClient, srcBucketName);
	    				     //CloudBlobContainer container=desClient.getContainerReference(bucketName);
	   	    		         azureConnec.AzureCreateFolder(desClient, desContainer, srcFolderName);
	    			 }
   	    			else if (!azureConnec.AzureExistenceFolder(desClient, desContainer, desFolderName)) {
   	    				     azureConnec.AzureCreateFolder(desClient, desContainer, srcFolderName);
					}
   	    			
   	   	    		//3. 
   	    			
   	    			ObjectListing objects = srcClient.listObjects(srcBucketName, srcFolderName);
   	    			long bucketTimeElasped=0;
   	   	    		for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
   	                           if(objectSummary.getSize()!=0){
   	                                  
   	                        	      
   	                                  CloudBlockBlob desBlob=
   	     		                                 desClient.getContainerReference(desContainer+SUFFIX+desFolderName).getBlockBlobReference(azureConnec.GetSubString
   	     		            		                           (objectSummary.getKey(), 1) );
   	                                  long startTime = System.currentTimeMillis();  
   	                                  desBlob.startCopy(new URI(AmazonGetURI(srcClient, objectSummary).toString()));
   	                                  long estimatedTime = System.currentTimeMillis() - startTime;
   	                			      bucketTimeElasped=bucketTimeElasped+estimatedTime;
   	                                  System.out.println("estimatedTime for upload object is  " + estimatedTime);
   	                                  System.out.println("estimatedTime for bucket migration is  " + bucketTimeElasped);
   	                		     
   	                            } 
   	                   }
   	   	    	     }//if source
   	    		   
   	    		    else{
   	    			     System.out.println("there is no folder "+srcBucketName+SUFFIX+srcFolderName +" in "+srcClient.getRegion()+" in source");
   	    		   }
   	    		} 
   	    	    catch (Exception e) {
		    	       System.out.println("The object " + " was not found.");
		        }
		  
	      }
   	     
   	    
   	   
   	
   	 
   	  public URI AmazonGetURI(AmazonS3Client client, S3ObjectSummary objectSumary) throws URISyntaxException {
		        //System.out.println(objectSumary.getKey());
		        URI result=client.generatePresignedUrl(new GeneratePresignedUrlRequest(objectSumary.getBucketName(), objectSumary.getKey())).toURI();
     	
   		        return result;
	   }    
   	
   	
     // This function updates the class storage of a folder. This happens when objects changes its hot-status to cold-status. hot-spot with storageCode (1) and cold-spot with storage code (0).
	@SuppressWarnings("deprecation")
	public void AmazonChangeClassStorageFolder( AmazonS3 client,String bucketName, String folderName, String storageClass) {
	           
		// It takes the name of folder (key) and then change the storage class of folder
		try{	
		     if(AmazonExistenceFolder(client, bucketName, folderName)==true){// folder existence
		    	
		          String key=folderName;
			      ObjectListing objects = client.listObjects(bucketName, key);
		          for (S3ObjectSummary objectSummary : objects.getObjectSummaries()){
		                
		        	  for (int sc = 0; sc < StorageClass.values().length; sc++) {
		  				if(StorageClass.values()[sc].toString().equals(storageClass)){
		  					// we use the below lines instead of API "changeObjectStorageClass" to set public access to the object.
		  					CopyObjectRequest cor=new CopyObjectRequest(bucketName, objectSummary.getKey(), bucketName, objectSummary.getKey());
		  					cor.setCannedAccessControlList(CannedAccessControlList.PublicReadWrite);
		  					cor.setStorageClass(StorageClass.values()[sc]);
		  					client.copyObject(cor);
		  					break;
		  					
		  				}
		  			 } 	     
		        	    
		            } 
		       }
		      else{// no folder existence
		    	    System.out.println("there is no "+bucketName+"/"+folderName+" in the source"); 
		      }
		  }catch (AmazonServiceException ase) {
		         System.out.println("Caught an AmazonServiceException, which " +
		          		"means your request made it " +
		                  "to Amazon S3, but was rejected with an error response" +
		                  " for some reason.");
		          System.out.println("Error Message:    " + ase.getMessage());
		          System.out.println("HTTP Status Code: " + ase.getStatusCode());
		          System.out.println("AWS Error Code:   " + ase.getErrorCode());
		          System.out.println("Error Type:       " + ase.getErrorType());
		          System.out.println("Request ID:       " + ase.getRequestId());
		          
		          
		      } catch (AmazonClientException ace) {
		          System.out.println("Caught an AmazonClientException, which " +
		          		 "means the client encountered " +
		                  "an internal error while trying to " +
		                  "communicate with S3, " +
		                  "such as not being able to access the network.");
		          System.out.println("Error Message: " + ace.getMessage());
		      }
		} 
	
	
	   
	 // This function lists all objects in a folder
	 public  void AmazonListObjects(AmazonS3 client, String bucketName, String folderName) {
	   		
		     for (Bucket bucket : client.listBuckets()) {
			        if(bucket.getName().equalsIgnoreCase(bucketName)){
				           ObjectListing listing = client.listObjects(new ListObjectsRequest().withBucketName(bucket.getName()));
			               String objectKey=folderName+SUFFIX;  
			               for (S3ObjectSummary objectSummary : listing.getObjectSummaries()) {
			 	                 if(objectSummary.getKey().startsWith(objectKey)){
			 	                	System.out.println(" -> " + objectSummary.getKey() + "  " +"(size = " + objectSummary.getSize()/1024 + " KB)"); 
			 	               }  
				            }
			           }//if
			        System.out.println("there is no the serached objects");
			        break;
		        }
	  }
	   
	   
	  // This function determines whether a bucket exists or not.
	  public  boolean AmazonExistenceBucket(AmazonS3 client, String bucketName) {
			
			      boolean bucketFlag=false;
	              for (Bucket bucket : client.listBuckets()) {
				 
				        if(bucket.getName().equalsIgnoreCase(bucketName)){
				                bucketFlag=true; 
				 	    	    break;
				 	      }
			      }
		          return bucketFlag;
		} 
	   
	  // This function determines whether a folder exists in a bucket or not. This function just cheeks the existence  of folder, not its contents.
	  public  boolean AmazonExistenceFolder(AmazonS3 client, String bucketName, String folderName) {
		
		      boolean folderFlag=false;
        
		     for (Bucket bucket : client.listBuckets()) {
			 
			        if(bucket.getName().equalsIgnoreCase(bucketName)){
			    
				           ObjectListing listing = client.listObjects(new ListObjectsRequest().withBucketName(bucket.getName()));
			               String objectKey=folderName+SUFFIX;  
			               for (S3ObjectSummary objectSummary : listing.getObjectSummaries()) {
				                 //System.out.println(objectSummary.getKey()+"   "+objectKey);
			 	                 if(objectSummary.getKey().equalsIgnoreCase(objectKey)){
			 	                	    folderFlag=true; 
			 	    	                 break;
			 	                  }  
				            }
			          }
		      }
	          return folderFlag;
	  }
   	
	  
	// This function provides log of operation conducted on Amazon DCs.
    public void AmazonLog(AmazonS3Client client, String bucketName) {
		   
         if(!AmazonExistenceFolder(client, bucketName, "logs")){
    	   
    	           // Assign the permision of LogDelivery to the bucket
    	             client.setBucketAcl(bucketName, CannedAccessControlList.LogDeliveryWrite);
    	 
                  //set the bucket to maintains the log
    	             BucketLoggingConfiguration bucketLogConfig=new BucketLoggingConfiguration();
		             bucketLogConfig.setDestinationBucketName(bucketName);
		   
		             bucketLogConfig.setLogFilePrefix("logs/");// To save the logs in a separate directory, logs
		             bucketLogConfig.isLoggingEnabled();
		   
		         // set the bucket whose log is maintained.
		            SetBucketLoggingConfigurationRequest bucketLogConfigRequest=new SetBucketLoggingConfigurationRequest(bucketName, bucketLogConfig);
		            client.setBucketLoggingConfiguration(bucketLogConfigRequest);
		   
		         //change the class storage of LogFile
		            String folderKey=bucketLogConfig.getLogFilePrefix()+SUFFIX;
		            client.changeObjectStorageClass(bucketName, folderKey, StorageClass.ReducedRedundancy);
		   
		           
    	 }
        else{
    	      System.out.println("The log for bucket "+bucketName+" already is activated");  	
    	}
    }
		 
      
     // Three below function checks whether a bucket/folder name is DNS-Compliance name or not. 
      public boolean isDNSName(String bucketName) {
          return isValidV2BucketName( bucketName );
      }
      
      public void validateBucketName(String bucketName) throws IllegalArgumentException {
          
          if (bucketName == null)
              throw new IllegalArgumentException("Bucket name cannot be null");

          if (!bucketName.toLowerCase().equals(bucketName))
              throw new IllegalArgumentException("Bucket name should not contain uppercase characters");

          if (bucketName.contains("_"))
              throw new IllegalArgumentException("Bucket name should not contain '_'");

          if (bucketName.contains("!") || bucketName.contains("@") || bucketName.contains("#"))
          	throw new IllegalArgumentException("Bucket name contains illegal characters");

          if (bucketName.length() < 3 || bucketName.length() > 63)
              throw new IllegalArgumentException("Bucket name should be between 3 and 63 characters long");

          if (bucketName.endsWith("-") || bucketName.endsWith("."))
              throw new IllegalArgumentException("Bucket name should not end with '-' or '.'");

          if (bucketName.contains(".."))
              throw new IllegalArgumentException("Bucket name should not contain two adjacent periods");

          if ( bucketName.contains("-.") ||
               bucketName.contains(".-") )
              throw new IllegalArgumentException("Bucket name should not contain dashes next to periods");

          if ( bucketName.contains(":") ||
               bucketName.contains(":;") )
              throw new IllegalArgumentException("Bucket name should not contain colons or semicolons");
      }
      
      public boolean isValidV2BucketName(String bucketName) {
          if (bucketName == null) return false;

          try {
              validateBucketName(bucketName);
              return true;
          } catch (IllegalArgumentException e) {
              return false;
          }
      }
      
     
      public  Date GetExperation() {                     
           return new Date((new Date(0).getTime()) + 60 * 60 * 1000);
     }
      
       
	   // This function takes the object key (foldeNmae+objectName) and then seprates the object name and returns it.
       public String getObjectName(String keyObj) {
		   
    	   String objectName="";
    	   String[] parts = keyObj.split(SUFFIX);
           objectName=parts[1];
           
           return objectName;
	   }
   
}
