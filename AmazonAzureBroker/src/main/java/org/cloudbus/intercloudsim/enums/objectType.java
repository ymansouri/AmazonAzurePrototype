package org.cloudbus.intercloudsim.enums;

public enum objectType {
      HOTSPOT,
      MEDIUMSPOT,
      LOWSPOT;
      public static objectType getObjectTypeByName(String name) {
  		
  		for (objectType p:objectType.values()) {
  			if (p.toString().equalsIgnoreCase(name)) {
  				return p;
  			}
  		}
  		return null;
  	}
    
}
