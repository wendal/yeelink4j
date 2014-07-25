package net.wendal.yeelink.spi;

import java.util.List;

public interface Yeelink {

	String createDev(YeelinkDev dev);
	
	String updateDev(YeelinkDev dev);
	
	YeelinkDev getDev(String device_id);
	
	void delDev(String device_id);
	
	//------------------------------------
	
	String createSensor(String device_id, YeelinkSensor sensor);
	
	String updateSensor(String device_id, YeelinkSensor sensor);
	
	List<YeelinkSensor> getSensors(String device_id);
	
	void delSensor(String device_id, String sensor_id);
	
	//-------------------------------------
	
	void upload(String device_id, String sensor_id, Object val);
	
	void upload(String device_id, String sensor_id, String key, YeelinkObject val);
}
