package optimalCost;


import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.intercloudsim.config.ICProperties;


import broker.*;

public class objectDatacenterSpecification {

	
	
	
	
	public static int [][] delayBetweenDatacenter;
	public static HashMap<String, AmazonKey> amazonKey=new HashMap<>(); 
	public static HashMap<String, AzureKey> azureKey=new HashMap<>();
	
	
	public static int periodTime;
	public static int windowTime;
	public static int delayConstraint;
	public static long ninesNumber;
	public static long numberObject;
	public static int replicaNumber;
	
	public static double latencySencetive;
	public static String dataCenterSpecification;
	public static String datacenterDelay; 
	public static String amazonKeyAddress;
	public static String azureKeyAddress;
	public static String objectPlacemnet;// address metadata file
	public static ArrayList<String> replicasPlacement;
	
	
	
   
					
	
	//read accessKey and secretKey of Amazon DCs.
	public static void readAmazonKey() {
		
		ArrayList<String> strArrayList=new ArrayList<>();
		strArrayList.addAll(readInput(amazonKeyAddress));
		String[] results=null;
		for (int i = 0; i < strArrayList.size(); i++) {
			String str=strArrayList.get(i);
		 	results = str.split(",");
		    AmazonKey amkey=new AmazonKey(results[1],results[2],results[3],results[4]);
		    amazonKey.put(results[0], amkey);
		}
	 }
     
	//read accessKey and secretKey of Azure DCs.
    public static void readAzureKey() {
    	//System.out.println(azureKeyAddress);
    	ArrayList<String> strArrayList=new ArrayList<>();
		strArrayList.addAll(readInput(azureKeyAddress));
		String[] results=null;	
    	
		for (int i = 0; i < strArrayList.size(); i++) {
			String str=strArrayList.get(i);
		    results = str.split(",");
		    
		    AzureKey azkey=new AzureKey(results[1],results[2],results[3]);
		    azureKey.put(results[0], azkey);
		}
     }
	
	
	
    public static void readSpecificationObjectWorkload() {
		
	     dataCenterSpecification=ICProperties.IC_SIMULATION_DATACENTER_SPECIFICATION.getValue();
	     datacenterDelay=ICProperties.IC_SIMULATION_DATACENTER_DELAY.getValue();
	     amazonKeyAddress=ICProperties.IC_SIMULATION_AMAZON_KEY.getValue();
	     azureKeyAddress=ICProperties.IC_SIMULATION_AZURE_KEY.getValue();
	     delayConstraint=ICProperties.IC_SIMULATION_DELAY_CONSTRAINT.getValueAsInt();
	     latencySencetive=ICProperties.IC_SIMULATION_LATENCY_SENCETIVE.getValueAsDouble();
	     objectPlacemnet=ICProperties.IC_SIMULATION_OBJECT_PLACEMENT.getValue();
	     periodTime=ICProperties.IC_SIMULATION_PERIOD_TIME.getValueAsInt();
	     
	      		
	}
	
	

	//This function read text from file
	public static ArrayList<String> readInput( String fileRead) {
		//System.out.println(fileRead);
		ArrayList<String> result=new ArrayList<>();
		try {
			Scanner inFile=new Scanner(new FileReader(fileRead));
			String str;
			str=inFile.next();
			result.add(str);
			
			while (str!=null) {
				str=inFile.next();
				result.add(str);
			}
			inFile.close();
		} 
		catch (Exception e) {
			if (e.getMessage()!=null) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}// readInput




	


	
	
	
	
	/*
	public static void setDatacenterList(List<NewDatacenter> datacenterList) {
		objectDatacenterSpecification.datacenterList = datacenterList;
	}
	*/
	
	public static long getNumberObject() {
		return numberObject;
	}
	public static void setNumberObject(long numberObject) {
		objectDatacenterSpecification.numberObject = numberObject;
	}

	public static int getReplicaNumber() {
		return replicaNumber;
	}

	public static void setReplicaNumber(int replicaNumber) {
		objectDatacenterSpecification.replicaNumber = replicaNumber;
	}
	public static int getPeriodTime() {
		return periodTime;
	}
	public static void setPeriodTime(int periodTime) {
		objectDatacenterSpecification.periodTime = periodTime;
	}
	
	
	public static int[][] getDelayBetweenDatacenter() {
		return delayBetweenDatacenter;
	}
	
	
	
	
	public static void setDelayBetweenDatacenter(int[][] delayBetweenDatacenter) {
		objectDatacenterSpecification.delayBetweenDatacenter = delayBetweenDatacenter;
	}
	public static int getDelayConstraint() {
		return delayConstraint;
	}
	public static void setDelayConstraint(int delayConstraint) {
		objectDatacenterSpecification.delayConstraint = delayConstraint;
	}
	public static long getNinesNumber() {
		return ninesNumber;
	}

	public static void setNinesNumber(long ninesNumber) {
		objectDatacenterSpecification.ninesNumber = ninesNumber;
	}
	public static int getWindowTime() {
		return windowTime;
	}
	public static void setWindowTime(int windowTime) {
		objectDatacenterSpecification.windowTime = windowTime;
	}

		
	}
