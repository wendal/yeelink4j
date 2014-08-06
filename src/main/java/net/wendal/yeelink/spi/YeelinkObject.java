package net.wendal.yeelink.spi;

import java.util.LinkedHashMap;
import java.util.Map;

public class YeelinkObject extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = -1269301879377423687L;
	
	public YeelinkObject(Map<String, ? extends Object> map) {
		putAll(map);
	}
	
	

	public String getAsString(String key) {
		Object obj = get(key);
		return obj == null ? null : String.valueOf(obj);
	}
	
	public double getAsDouble(String key) {
		Object obj = get(key);
		return obj == null ? 0 : ((Number)obj).doubleValue();
	}
	
}
