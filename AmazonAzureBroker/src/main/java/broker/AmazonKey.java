package broker;

public class AmazonKey {
	
	String region;
	String accessKey;
	String secretKey;
    String ipAddress;
	
	public AmazonKey() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public AmazonKey(String region, String accessKey, String secretKey, String ipAddress) {
		super();
		this.region = region;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.ipAddress = ipAddress;
	}

	public String getRegion() {
		return region;
	}



	public void setRegion(String region) {
		this.region = region;
	}



	public String getIpAddress() {
		return ipAddress;
	}



	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}



	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	
}
