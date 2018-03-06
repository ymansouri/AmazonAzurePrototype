package broker;

public class AzureKey {
	
	
	String GRSKey;
	String LRSKey;
	String ipAddress;
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public AzureKey() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public AzureKey(String gRSKey, String lRSKey, String ipAddress) {
		super();
		this.GRSKey = gRSKey;
		this.LRSKey = lRSKey;
		this.ipAddress = ipAddress;
		
	}
	
	public String getGRSKey() {
		return GRSKey;
	}
	public void setGRSKey(String gRSKey) {
		GRSKey = gRSKey;
	}
	public String getLRSKey() {
		return LRSKey;
	}
	public void setLRSKey(String lRSKey) {
		LRSKey = lRSKey;
	}
	
}
