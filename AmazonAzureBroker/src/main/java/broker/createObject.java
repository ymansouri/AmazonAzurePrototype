package broker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

public class createObject {
	
	
	private String nameObject;// This name is allocated by user
	private String uniqueNameObject;
	private int size;
	private RandomAccessFile randomAccessFile;
    private String addressObject;
    final String dir = System.getProperty("user.dir");
	public createObject() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public RandomAccessFile createFile(String name, int size) throws FileNotFoundException, IOException {
		  
		  uniqueNameObject=name+timeDateCreator();
		  randomAccessFile  = new java.io.RandomAccessFile(uniqueNameObject, "rw");
		  randomAccessFile.setLength(size);
		  
		  addressObject=dir+"/"+uniqueNameObject;
		  
		  System.out.println(randomAccessFile+" address "+addressObject);
		  
		  return randomAccessFile;  
	}
	
   private String timeDateCreator() {
		
		String timeDate="";
		
		Date d = new Date();
		timeDate=timeDateReviser(d.toString());
		
		return timeDate;
	}

	// This function removes the name of week, AETA, and year. 
	private String timeDateReviser(String dt){
		
		String results="";
		String[] tempDT=null;
		tempDT=dt.split(" ");
		for (int i = 1; i < tempDT.length-2; i++) {
			results=results+" "+tempDT[i];
	     }
		return results;
		
	}
	public String getAddressObject() {
		return addressObject;
	}

	public void setAddressObject(String addressObject) {
		this.addressObject = addressObject;
	}

	public String getUniqueNameObject() {
		return uniqueNameObject;
	}


	public void setUniqueNameObject(String uniqueNameObject) {
		this.uniqueNameObject = uniqueNameObject;
	}
	
	public String getNameObject() {
		return nameObject;
	}
	public void setNameObject(String nameObject) {
		this.nameObject = nameObject;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
}
