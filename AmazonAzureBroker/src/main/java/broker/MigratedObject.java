package broker;

public class MigratedObject {
	
	
	private String objectName;// This name is allocated by user
	private long objectSize;
	private long objectMigrationTime;
	
	
	
	
	public MigratedObject() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MigratedObject(String nameObject, long objectSize, long objectDurationTime) {
		super();
		this.objectName = nameObject;
		this.objectSize = objectSize;
		this.objectMigrationTime = objectDurationTime;
	}
	public String getNameObject() {
		return objectName;
	}
	public void setNameObject(String nameObject) {
		this.objectName = nameObject;
	}
	public long getObjectSize() {
		return objectSize;
	}
	public void setObjectSize(long objectSize) {
		this.objectSize = objectSize;
	}
	public long getObjectMigrationTime() {
		return objectMigrationTime;
	}
	
}
