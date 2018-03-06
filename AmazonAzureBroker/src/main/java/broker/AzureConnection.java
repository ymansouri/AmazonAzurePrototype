package broker;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.StorageClass;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.LoggingOperations;
import com.microsoft.azure.storage.LoggingProperties;
import com.microsoft.azure.storage.MetricsLevel;
import com.microsoft.azure.storage.MetricsProperties;
import com.microsoft.azure.storage.NameValidator;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.ServiceProperties;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.analytics.CloudAnalyticsClient;
import com.microsoft.azure.storage.analytics.LogRecord;
import com.microsoft.azure.storage.analytics.StorageService;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobListingDetails;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlobDirectory;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
public class AzureConnection {
	private String SUFFIX = "/";
	private HashMap<String, AzureKey> azKey = new HashMap<>();

	public AzureConnection() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AzureConnection(HashMap<String, AzureKey> azKey) {
		super();
		this.azKey = azKey;
	}

	
	// This function creates CloudBlobClient for GRS storage classes: In this implementation we have 8 DCs.
	public HashMap<String, CloudBlobClient> GRSCreateCloudBlobClientAccount()
			throws InvalidKeyException, URISyntaxException, StorageException {

		HashMap<String, CloudBlobClient> GRSAzureClientList = new HashMap<>();

		for (String dcName : azKey.keySet()) {
            
			CloudStorageAccount GRSAccount = CloudStorageAccount.parse(azKey.get(dcName).GRSKey);
			CloudBlobClient AzureClient = GRSAccount.createCloudBlobClient();
			servicePro(AzureClient);
			GRSAzureClientList.put(dcName, AzureClient);
			
		}
		return GRSAzureClientList;
	}

	// This function creates CloudBlobClient for LRS storage classes: In this implementation we have 9 DCs.
	public HashMap<String, CloudBlobClient> LRSCreateCloudBlobClientAccount()
			throws InvalidKeyException, URISyntaxException, StorageException {

		HashMap<String, CloudBlobClient> LRSAzureClientList = new HashMap<>();
		for (String dcName : azKey.keySet()) {

			CloudStorageAccount LRSAccount = CloudStorageAccount.parse(azKey.get(dcName).LRSKey);
			CloudBlobClient AzureClient = LRSAccount.createCloudBlobClient();
			servicePro(AzureClient);
			LRSAzureClientList.put(dcName, AzureClient);

		}
		return LRSAzureClientList;
	}

	// 1.1 This function defines logProperties, MetricsProperties for each client
	public void servicePro(CloudBlobClient client) throws StorageException {

		// Create logProperties
		LoggingProperties logProperties = new LoggingProperties();
		EnumSet<LoggingOperations> logOper = EnumSet.of(LoggingOperations.WRITE, LoggingOperations.READ,
				LoggingOperations.DELETE);
		logProperties.setLogOperationTypes(logOper);
		logProperties.setRetentionIntervalInDays(90);
		//System.out.println(logProperties.getLogOperationTypes());
        
		// Create Metrics properties
		MetricsProperties metProerties = new MetricsProperties();
		metProerties.setRetentionIntervalInDays(90);
		metProerties.setMetricsLevel(MetricsLevel.SERVICE_AND_API);
       
		// Create ServiceProperties
		ServiceProperties serviceProperties = new ServiceProperties();

		serviceProperties.setLogging(logProperties);// set log
		serviceProperties.setMinuteMetrics(metProerties);
		serviceProperties.setHourMetrics(metProerties);// set metrics
	
		client.uploadServiceProperties(serviceProperties);
		//System.out.println(metProerties.getMetricsLevel());
	}

	// 3. This function creates a container in Azure storage
	public void createContainer(CloudBlobClient client, String containerName)
			throws URISyntaxException, StorageException {

		try {
			  // 1. create container
			  NameValidator.validateContainerName(containerName);
			  CloudBlobContainer container = client.getContainerReference(containerName);


			  if (container.createIfNotExists() == true) {

				// 2 . Giving permission to a container: It has three permissions: container, blob, and OFF
				BlobContainerPermissions permissions = new BlobContainerPermissions();
				permissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
				
				container.uploadPermissions(permissions);
				
				System.out.println("container" + "  " + containerName + "  " + "created.");
			  } else {
				System.out.println("container" + "  " + containerName + "  " + "is already created.");
			  }
	
			
	        }catch (IllegalArgumentException ex){
		          System.out.println("The container "+ containerName+" is not valid");
	        }
	         catch(StorageException storageException)
	        {
		         System.out.print("StorageException encountered: ");
		         System.out.println(storageException.getMessage());
		         System.exit(-1);
	        }catch(Exception e)
	        {
		         System.out.print("Exception encountered: ");
		         System.out.println(e.getMessage());
		         System.exit(-1);
	        }

	}

	
	// This function creates a folder in a bucket. For hot-spot, the storageClassCode is 1, otherwise is 0 (for cold-spot)
		  public void AzureCreateFolder(CloudBlobClient client, String containerName, String folderName  
		  			                   ) throws URISyntaxException, StorageException, FileNotFoundException, IOException {
		  		
			  
			  if(AzureExistenceFolder(client, containerName, folderName)){
				  System.out.println("folder"+" "+containerName+SUFFIX+folderName+" "+"already exists");
			  }
			  else{
			       CloudBlobContainer blobContainer =client.getContainerReference(containerName);
		  		   ObjectMetadata metadata = new ObjectMetadata();
	  	           metadata.setContentLength(0);
	  	           InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
	  	        
	  	           //File sourceFile = new File("/FlashM/survey.pdf");
	  	           String f=folderName+SUFFIX;
	  	           CloudBlockBlob blob=blobContainer.getBlockBlobReference(f);
	  	        
		  		   blob.upload(emptyContent, 0);
	  	          //blob.upload(new FileInputStream(sourceFile), sourceFile.length());
		  		  System.out.println("folder "+containerName+SUFFIX+folderName +" created");
			  }
		  		
		 }
	
	
	
	// This function lists all containers in a cloudBlobClient as well as all folders in a container
	public void listContainers(CloudBlobClient client, String containerName)
			throws URISyntaxException, StorageException {

		for (CloudBlobContainer c : client.listContainers()) {

			System.out.println(" - " + c.getName() + "---" + c.getUri() + "----" + c.getProperties().getEtag());
			for (ListBlobItem item : c.listBlobs(null, true)) {
				System.out.println(item.getUri().getPath());
				CloudBlockBlob blob = (CloudBlockBlob) item;
				System.out.println(blob.getName() + "   " + blob.getProperties().getLength() / 1024 + " KB");

			}

		}

	}

	// This function downloads the object from a DC in Azure
	public void AzureDownloadObject(CloudBlobClient client, String containerName, String folderName, String desAddress)
			throws IOException {
		
		try {

			for (CloudBlobContainer c : client.listContainers()) {

				if (c.getName().equalsIgnoreCase(containerName)) {

					for (ListBlobItem blobItem : c.listBlobs(null, true)) {
						if (!(blobItem instanceof CloudBlobDirectory)  && blobItem.getParent().getPrefix().equalsIgnoreCase(folderName+SUFFIX)) {
							CloudBlob blob = (CloudBlob) blobItem;
							

							if ((blob.getProperties().getLength() / 1024) != 0) {
								
								new File(desAddress+SUFFIX+folderName).mkdirs();// create folder
							    blob.downloadToFile(desAddress+SUFFIX+folderName+SUFFIX+GetSubString(blob.getName(), 1));
								System.out.println(blob.getName() + "   " + blob.getProperties().getLength() / 1024 + " KB downladed");
							}
						}
					}
				}
			} // for
		} // try
		catch (Exception e) {
			System.out.println("The object " + " was not found.");
		}
	}

	// This functions uploads an object file into data center. "sourceAdress" is the  address of the object that should be uploaded.
	public void AzureUploadObjectFile(CloudBlobClient client, String containerName, String folderName,
			                      String objectName, String srcAddress) throws IOException {

		long startTime = System.currentTimeMillis();
		File objectFile = new File(srcAddress);
		try {
			   CloudBlockBlob blob = client.getContainerReference(containerName + SUFFIX + folderName)
					.getBlockBlobReference(objectName);
			   blob.upload(new FileInputStream(objectFile), objectFile.length());
              
			   System.out.println("object" + " " + objectName + " " + "uploaded in" + " " + containerName + SUFFIX+ folderName);

		      } catch (Exception e) {
			    System.out.println("the object" + " " + objectName + " was not found.");
		    }
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("estimatedTime for upload object is  " + estimatedTime);
	
	}
    
	// This functions uploads an object Stream into data center. 
    public void AzureUploadObjectStream(CloudBlobClient client, String containerName, String folderName,
				                      InputStream objectStream, String objectStreamName) throws IOException {

			long startTime = System.currentTimeMillis();
			//File objectFile = new File(srcAddress);
			try {
				   CloudBlockBlob blob = client.getContainerReference(containerName + SUFFIX + folderName)
						.getBlockBlobReference(objectStreamName);
				   //blob.upload(new FileInputStream(objectFile), objectFile.length());
	               blob.upload(objectStream, objectStream.available());
				   System.out.println("object" + " " + objectStreamName + " " + "uploaded in" + " " + containerName + SUFFIX+ folderName);

			      } catch (Exception e) {
			    	  
				    System.out.println("the object" + " " + objectStreamName + " was not found.");
			    }
			long estimatedTime = System.currentTimeMillis() - startTime;
			System.out.println("estimatedTime for upload object is  " + estimatedTime);
		
		}
	
	
	// This function deletes a folder in a container if it exists.
	public void AzureDeleteFolder(CloudBlobClient client, CloudBlobContainer containerName, String folderName)
			throws StorageException, URISyntaxException {

		boolean exeistanceFlag = false;
        
		
		for (ListBlobItem item : containerName.listBlobs(folderName+SUFFIX, true)) {
			if (item.getParent().getBlockBlobReference(folderName + SUFFIX).deleteIfExists()) {
				System.out.println(containerName.getName() + SUFFIX + folderName + " deleted");
				exeistanceFlag = true;
				break;
			}
		}

		if (exeistanceFlag == false) {
			System.out.println("There is no such folder: " + containerName.getName() + SUFFIX + folderName);
		}
	}

	/*
	 * This function updates the class storage of a folder. This happens when
	 * objects changes its hot-status to cold-status. For this purpose, the
	 * object should be copied from GRS storage to LRS storage as the object
	 * transmitted from hot to cold and vice versa. This function also copy data
	 * from one Azure DC to another one.
	 */
	public void AzureTransferStorageFolder(CloudBlobClient srcClient, CloudBlobClient desClient,
			String srcContainerName, String desContainerName, String srcFoldername, String desFolderName) {

		try {
			if (!AzureExistenceFolder(desClient, desContainerName, desFolderName)) {
				AzureCreateFolder(desClient, desContainerName, desFolderName);
			}

			for (CloudBlobContainer c : srcClient.listContainers()) {

				if (c.getName().equalsIgnoreCase(srcContainerName)) {

					for (ListBlobItem blobItem : c.listBlobs(srcFoldername+SUFFIX, true)) {
						if (!(blobItem instanceof CloudBlobDirectory)) {
							CloudBlob srcBlob = (CloudBlob) blobItem;

							URI srcUri = new URI(srcBlob.getUri().toString());
							if ((srcBlob.getProperties().getLength() / 1024) != 0) {
								System.out.println("blob name " + srcBlob.getName());

								CloudBlockBlob desBlob = desClient
										.getContainerReference(desContainerName + SUFFIX + desFolderName)
										.getBlockBlobReference(GetSubString(srcBlob.getName(), 1));
								desBlob.startCopy(srcUri);
							}
						}
					}
				}
			} // for
		} // try
		catch (Exception e) {
			System.out.println("The object " + " was not found.");
		}
	}

	// This function migrates data from an Azure DC to an Amazon DC.
	public ArrayList<MigratedObject> AzureToAmzonStorageFolder(CloudBlobClient srcClient, String srcContainerName, String srcFoldername,
			AmazonS3Client desClient, String bucketName, String desFolderName, String storageClass) {
		AmazonConnection amazonConnec = new AmazonConnection();
		ArrayList<MigratedObject> objectsMigrationTime=new ArrayList<>();
		try {
			
			// 1.determine whether the source bucket and folder exist in the source DC or not
			if (AzureExistenceFolder(srcClient, srcContainerName, srcFoldername)) {
				
				if(!amazonConnec.AmazonExistenceBucket(desClient, bucketName)){
					        
					        amazonConnec.createBucket(desClient, bucketName);
				            amazonConnec.AmazonCreateFolder(desClient, bucketName, desFolderName, storageClass);
				            
				 }
				else if(!amazonConnec.AmazonExistenceFolder(desClient, bucketName, desFolderName)){
					  amazonConnec.AmazonCreateFolder(desClient, bucketName, desFolderName, storageClass);	  
				}
				// 3.
				for (CloudBlobContainer c : srcClient.listContainers()) {
					
					if (c.getName().equalsIgnoreCase(srcContainerName)) {
				     
						//long bucketTimeElasped=0;
						for (ListBlobItem blobItem : c.listBlobs(srcFoldername+SUFFIX, true)) {

							if (!(blobItem instanceof CloudBlobDirectory)
									&& (blobItem.getParent().getPrefix().equalsIgnoreCase(srcFoldername + SUFFIX))) {
                                
								CloudBlob srcBlob = (CloudBlob) blobItem;
								File file=new File(srcBlob.getName());	
				                FileUtils.copyURLToFile(srcBlob.getUri().toURL() , file);
								
								for (int sc = 0; sc < StorageClass.values().length; sc++) {
									if(StorageClass.values()[sc].toString().equals(storageClass)){
										
										long startTime = System.currentTimeMillis();
										desClient.putObject(new PutObjectRequest(bucketName, srcBlob.getName(), file)
												.withCannedAcl(CannedAccessControlList.PublicReadWrite)
												.withStorageClass(StorageClass.values()[sc]));
									    
										
										long estimatedTime = System.currentTimeMillis() - startTime;
	   	                			    //
										MigratedObject objecttime=new MigratedObject(srcBlob.getName(),srcBlob.getProperties().getLength(),estimatedTime);
	   	                			     objectsMigrationTime.add(objecttime);
	   	                		     	break;
										
									}
								 } 	       
							}
						}
					}
				} // for
			} // if source
			else {
				System.out.println("there is no folder " + srcContainerName + SUFFIX + srcFoldername + " in "
						+ srcClient.getEndpoint() + " in source");
			}

		} // try
		catch (Exception e) {
			System.out.println("The object " + " was not found.");
		}
		return objectsMigrationTime; 
		
	}

	// This function provides log of operation conducted on Azure DCs.
	public void AzureLog(CloudStorageAccount azureAccount)
			throws StorageException, URISyntaxException, ParseException, FileNotFoundException {

		// 1. The file for log is prepared.
		File fName = new File("AzureLog.csv");
		PrintWriter outfile1 = new PrintWriter(fName);
		outfile1.println();
		String seprator = ",";

		// 2. preparing the log
		String s = "08/08/2016";
		String e = "08/10/2016";
		SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
		Date sDate = sd.parse(s);
		Date eDate = sd.parse(e);
		EnumSet<LoggingOperations> logOper = EnumSet.of(LoggingOperations.WRITE, LoggingOperations.READ,
				LoggingOperations.DELETE);
		BlobListingDetails blobDetails = BlobListingDetails.METADATA;
		BlobRequestOptions blobRequest = new BlobRequestOptions();
		blobRequest.getOperationExpiryTimeInMs();
		OperationContext opeContext = new OperationContext();
		opeContext.setLoggingEnabled(false);

		
		try {
			CloudAnalyticsClient analyticsClient = new CloudAnalyticsClient(azureAccount.getBlobStorageUri(),
					azureAccount.getTableStorageUri(), azureAccount.getCredentials());
			
			Iterable<ListBlobItem> blobitems = analyticsClient.listLogBlobs(StorageService.BLOB, sDate, eDate, logOper,
					blobDetails, blobRequest, null);

			for (ListBlobItem item : blobitems) {
				Iterable<LogRecord> logRecords = CloudAnalyticsClient.parseLogBlob(item);
				System.out.println("in Log function");
				for (LogRecord logRecord : logRecords) {

					// AzureLogFile(logRecord, outfile1);
					outfile1.println(logRecord.getOperationType() + seprator + logRecord.getRequestedObjectKey()
							+ seprator + logRecord.getEndToEndLatencyInMS() + seprator
							+ logRecord.getServerLatencyInMS() + seprator + logRecord.getRequestStartTime() + seprator
							+ logRecord.getRequestUrl());
					System.out.println(logRecord.getOperationType() + " " + logRecord.getRequestStartTime() + "  "
							+ logRecord.getEndToEndLatencyInMS());
				}
			}
			outfile1.close();
		} catch (StorageException se) {
			System.out.print("StorageException encountered: ");
			System.out.println(se.getMessage());
			System.exit(-1);
		} catch (Exception e1) {
			System.out.print("Exception encountered: ");
			System.out.println(e1.getMessage());
			System.exit(-1);
		}
		/*
		 * catch (URISyntaxException urie) { System.err.println(
		 * "Unable to create host URI for: "); urie.printStackTrace();
		 * System.exit(-1); }
		 */
	}

	// This procedure writes Azure Log in the a file.
	public void AzureLogFile(LogRecord logRecord, PrintWriter out) throws FileNotFoundException {
		String seprator = ",";
		File fName = new File("AzureLog.csv");
		out = new PrintWriter(fName);

		out.println(logRecord.getOperationType() + seprator + logRecord.getRequestedObjectKey() + seprator
				+ logRecord.getEndToEndLatencyInMS() + seprator + logRecord.getServerLatencyInMS() + seprator
				+ logRecord.getRequestStartTime() + seprator + logRecord.getRequestUrl());
		out.close();
	}

	// This function uploads an object from Azure DCs into Amazon DC
	public void UploadObject(URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("Put");
		// InputStream inputStream = getContentResolver().openInputStream(uri);
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
		out.write("This object uploaded as object.");
		out.close();
		int responseCode = connection.getResponseCode();
		System.out.println("Service returned response code " + responseCode);

	}
    
	// This function lists all blobs in a folder.
	public  void AzureListObjects(CloudBlobClient client, CloudBlobContainer container, String folderName) throws URISyntaxException, StorageException {
   		
		boolean containerFlag=false;
		boolean folderFlag=false;
		for (CloudBlobContainer c : client.listContainers()) {

			if (c.getName().equalsIgnoreCase(container.getName())) {
                containerFlag=true;
				for (ListBlobItem blobItem : c.listBlobs(null, true)) {
					if (!(blobItem instanceof CloudBlobDirectory) && blobItem.getParent().getPrefix().equalsIgnoreCase(folderName+SUFFIX)) {
						folderFlag=true;
						CloudBlob blob = (CloudBlob) blobItem;
						//System.out.println("The blobs in folder "+container.getName()+SUFFIX+folderName+"  are:" );
						System.out.println(blob.getName() + "   " + blob.getProperties().getLength() / 1024 + " KB");
					}
					else if(!folderFlag){
						 System.out.println("There is no the requested folder!");
					}
				}
			}
			else if (!containerFlag) {
				System.out.println("There is no the requested container!");
			}
			
		} 
	
      }

	
	//This function determines whether a container exists or not.
	public boolean AzureExistenceContainer(CloudBlobClient client, String containerName)
				throws URISyntaxException, StorageException, FileNotFoundException, IOException {
			boolean containerFlag = false;

			for (CloudBlobContainer c : client.listContainers()) {
				if (c.getName().equalsIgnoreCase(containerName)) {
							
					  containerFlag = true;
					  break;
				}
			}
			//System.out.println("the value of flag===>" + containerFlag);
			return containerFlag;
		}
	
	
	// This function determines whether a folder exists in a container or not. 
	public  boolean AzureExistenceFolder(CloudBlobClient client, String containerName, String folderName 
	                                                 ) throws URISyntaxException, StorageException, FileNotFoundException, IOException{
				boolean exeistanceFlag=false;
		         
		        for (CloudBlobContainer c : client.listContainers()) {
		        	if(c.getName().equalsIgnoreCase(containerName)){
		        		for (ListBlobItem item : c.listBlobs(folderName+SUFFIX, true)) {
		        			if(item.getParent().getBlockBlobReference(folderName+SUFFIX).exists()){
		        				exeistanceFlag=true;
		        				break;
		        			}
		        		}
		        		
		        	}
		        }
		       //System.out.println("the value of flag===>"+exeistanceFlag);
		       return exeistanceFlag;
			}
	
	
	// This function determines whether a folder exists in a container or not.
	public boolean _AzureExistenceFolder(CloudBlobClient client, String containerName, String folderName)
			throws URISyntaxException, StorageException, FileNotFoundException, IOException {
		boolean folderFlag = false;

		for (CloudBlobContainer c : client.listContainers()) {
			if (c.getName().equalsIgnoreCase(containerName)) {
				for (ListBlobItem item : c.listBlobs(folderName+SUFFIX, true)) {
					if (item.getParent().getBlockBlobReference(folderName+SUFFIX).exists()) {
						folderFlag = true;
						break;
					}
				}

			}
		}
		System.out.println("the value of flag===>" + folderFlag);
		return folderFlag;
	}

	public void _AzureExiestsFolder(CloudBlobClient client, String containerName, String folderName,
			CloudBlobContainer container)
			throws URISyntaxException, StorageException, FileNotFoundException, IOException {

		// System.out.println(client.getContainerReference(containerName).getDirectoryReference("/myimages").toString());
		for (ListBlobItem item : container.listBlobs(folderName+SUFFIX, false)) {
			CloudBlockBlob blob = (CloudBlockBlob) item;
			System.out.println(blob.getUri() + "   " + blob.getProperties().getLength() + " " + blob.getName());
		}
	}

	// This function takes the URI a blob and then return the name of blob.
	public String GetFileNameFromBlobURI(String uri, String containerName) {

		String name = null;
		String[] parts = uri.split(SUFFIX);
		int folderLocation = 0;
		for (int index = 0; index < parts.length; index++) {
			if (parts[index].equalsIgnoreCase(containerName)) {
				folderLocation = index;
				break;
			}
		}
		name = parts[folderLocation + 1];
		return name;
	}

	// This function gets a string and then one part of this string based on the
	// "location" parameter is removed.
	public String RemoveSubString(String str, int location) {

		String convertedString = "";
		String[] parts = str.split(SUFFIX);

		parts[location] = "";
		for (int index = 0; index < parts.length; index++) {
			// System.out.println(parts[index]);
			if (parts[index] != "") {
				convertedString = convertedString + parts[index] + SUFFIX;
			}

		}
		// System.out.println("CONVERTED STRING===> "+ convertedString);
		return convertedString;
	}

	// This function gets a string and then based one part of this string based
	// on the "location" parameter is returned.
	public String GetSubString(String str, int location) {

		String name = null;
		String[] parts = str.split(SUFFIX);
		name = parts[location];
		System.out.println("name===>"+name);
		return name;
	}
}
