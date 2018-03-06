package makeConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import optimalCost.objectDatacenterSpecification;

public class configFileMaker {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	 static int configNumber;
	 public static int configsPerNode=12;
	 //static String path1="/Users/yaser/Documents/workspace/FinalPaper2";// for browsing data center specification and delay between them. This address should be changed if the programm run another computer. 
	 //static String path2="/Users/yaser/Documents/workspace/FinalPaper2";// the path of saving properties of each region or multi regions.
	
	
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("config file is created");
		configMaker("Geo","GeoSetup","GeoDatacenter","GeoDelay");
		System.out.println("sh files are created");
		createSHFile("Geo");
		configMaker("Asia","AsiaSetup","AsiaDatacenter","AsiaDelay");
		createSHFile("Asia");
		
	}
	
	
	
	
	public static void configMaker(String str0,String str1, String str2, String str3) throws FileNotFoundException {
		int experimentId=1;
		
		double [] shape={1,1.5};
		double [] scale={0.5,1};
		int [] replica={1,2};
		int [] delay={100,150,250};
		
	    int [] object_min_size={1,10,10,50};
	    int [] object_max_size={1,10,50,100};
	    configNumber=(shape.length*replica.length*delay.length*object_min_size.length);
	    String folderName=folderCreate(0, str0+"Properties");
		for (int dis = 0; dis < 2; dis++) {
			for (int r = 0; r < 2; r++) {
				for (int d = 0; d < 3; d++) {
					
						for (int itemSize = 0; itemSize < 4; itemSize++) {
							
							File fName = new File(folderName+"/"+experimentId+".properties");
							
							PrintWriter outFile1=new PrintWriter(fName);
							outFile1.flush();
							
							outFile1.println("#SIMULATION SETTING");
							outFile1.println("experiment.id="+experimentId);
							outFile1.println("experiment.run="+30);
							outFile1.println("period.time="+120);
							outFile1.println("window.time="+4);
							outFile1.println();
							
							outFile1.println("#DATACENTER SPECIFICATION");
							outFile1.println("datacenter.specification="+"."+"/"+str1+"/"+str2+".txt");
							outFile1.println("datacenter.delay="+"."+"/"+str1+"/"+str3+".txt");
							outFile1.println();
							
							outFile1.println("#AVAILABILITY");
							outFile1.println("nines.number="+10);
							outFile1.println();
							
							outFile1.println("#READ RATE DISTRIBUTION");
							outFile1.println("weibull.shape="+shape[dis]);
							outFile1.println("weibull.scale="+scale[dis]);
							outFile1.println();
							
							outFile1.println("#REPLICA NUMBER");
							outFile1.println("replica.number="+replica[r]);
							outFile1.println();
							
							outFile1.println("#DELAY CONSTRAINT");
							outFile1.println("delay.constraint="+delay[d]);
							outFile1.println();
							
							outFile1.println("#OBJECT SPECIFICATION");
							outFile1.println("object.total.size="+12);
							outFile1.println("bucket.size="+2);
							outFile1.println("region.number="+8);
							outFile1.println("object.read.to.write.rate="+31);
							outFile1.println("max.object.read.to.write.rate="+31);
							outFile1.println("object.min.size="+object_min_size[itemSize]);
							outFile1.println("object.max.size="+object_max_size[itemSize]);
							outFile1.println();
							
							outFile1.close();
							experimentId++;
							
						}//itemSize
					}//delay
		    	}//r
	    	}//dis
			
    	}
	
	
	
	   public static void createSHFile(String str0) throws FileNotFoundException {
		   int index=0;// This variable determines which config should be assigned to each node 
		   
		   int numberNode_SH=configNumber/configsPerNode; // we allocate 12 configure file to each node of cluster
		   String folderName=folderCreate(0, str0+"SHFile");
		   File directory = new File(folderName);
		   
		   for (int i = 0+(determineNode(str0)*numberNode_SH); i < numberNode_SH+(determineNode(str0)*numberNode_SH); i++) {
			  
			   // 1. create folder for each node that contains the file .sh 
			      directory=new File(folderName+"/"+"node"+i);
			      directory.mkdirs();
			      // 2. create .sh file for each node 
			    File fName = new File(directory+"/"+"node"+i+".sh");
		    	PrintWriter outFile1=new PrintWriter(fName);
		    	
			    for (int config = 1+index; config < (configsPerNode+1)+index; config++) {
			    	outFile1.println("java -jar"+" "+str0+"Run.jar"+" "+str0+"Properties"+"/"+config+".properties");
			    }
			    outFile1.close();
			    index+=configsPerNode;
			    
			 }
	   }
	
	
	public static String folderCreate(int i, String fName) {
		 
		String dir=new String();
		dir="."+"/"+fName;
		File directory=new File(dir);
        directory.mkdir(); 		
        return dir;
		
	}
	
	
	public static String createFolder(String path) throws IOException {
		
		String desFolder=determineNodeForResult(objectDatacenterSpecification.dataCenterSpecification);
		//String desFolder=determineNodeForResult();
	    int nodeNumber=(Integer.parseInt(path)/configFileMaker.configsPerNode)+(determineNode(desFolder)*(configNumber/configsPerNode));
	    
	    File directory = new File("."+"/"+desFolder+"SHFile"+"/"+"Node"+nodeNumber+"/"+path);
	    if (!directory.exists()) {
	    	 directory.mkdirs();
	        
	    } else {
	    	//System.out.println("Folder already exists");
	        
	    }
	     
	   new File("."+"/"+desFolder+"SHFile"+"/"+"Node"+nodeNumber+"/"+path+"/"+"workload").mkdirs();
	   File file=  new File("."+"/"+desFolder+"SHFile"+"/"+"Node"+nodeNumber+"/"+path+"/"+"result");
	   file.mkdirs();
	   new File(file.getAbsolutePath()+"/"+"benchmark").mkdirs();
	   new File(file.getAbsolutePath()+"/"+"Optimal").mkdirs();
	   new File(file.getAbsolutePath()+"/"+"Greedy").mkdirs();
	   new File(file.getAbsolutePath()+"/"+"randomize").mkdirs();
	   
	   return directory.getAbsolutePath(); 
	     
	}
	
   
	
	// This function determines the node destination for result
	public static String determineNodeForResult(String str0) {
	   String result=new String();
	   String [] regionName={"Geo","Asia","APasific","SouthA","NorthA"};// There are the names of regions: we select five region because we chave 20 nodes on cluster and each region needs for nodes and each node we have 12 configuration. Apasific: means Australia and Newzeland. 
	   for (int i = 0; i < regionName.length; i++) {
		   if(str0.contains(regionName[i])){
			   result=regionName[i];
			   break;
		   }
	}
	 return result;  
}
	

	// This function receive the name of each setup and then determine on which node(s) is selected for running.
	public static int determineNode(String str) {
		
    	int result=-1;
    	switch (str) {
		case "Geo":
			result=0; 
			break;
		case "Asia":
			result=1; 
			break;
		case "APasific":
			result=2; 
			break;
		case "SouthA":
			result=3; 
			break;
		
		case "NorthA":
			result=4; 
			break;
		
		default:
			break;
		}
    	return result;
    	
	}
	
}
