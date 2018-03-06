package org.cloudbus.intercloudsim.enums;

public enum objectDistribution {
	NORMAL,
	UNIFORM,
	CONSTANT,
	ZIPF,
	SKEWEDNORMAL;
	
	
	public static objectDistribution getAuctionQtyDistributionByName(String name) {
		
		for (objectDistribution p:objectDistribution.values()) {
			if (p.toString().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}
	
	
	
	
	
	
	
}
