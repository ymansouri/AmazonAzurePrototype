package broker;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;

public class RandomInputStream extends InputStream{
	
	private int available;
    private Random random = new Random();
    
    
    
    
    public RandomInputStream() {
		super();
	}


	public RandomInputStream(final int totalSize)
    {
        this.available = totalSize;
    }
    
    
    @Override
    public int read() throws IOException
    {
        if(available == 0)
            return -1;
        --available;
        return random.nextInt(256);
    }


    @Override
    public int available() throws IOException
    {
        return available;
    }
	
    
    // This function prepares the name of RandomInputstream--USID# +@+TimeDate
    public String streamNameReviser(InputStream ins, String userid) {
		
    	String sname="";
   
	    String stemp=ins.getClass().getName();
    	sname=stemp.replace(stemp, userid);
    	sname=sname+"@"+timeDateCreator();
	    
    	return sname;
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
    
	
		
}
