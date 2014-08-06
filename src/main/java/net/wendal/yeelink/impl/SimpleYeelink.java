package net.wendal.yeelink.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.wendal.yeelink.Yeelinks;
import net.wendal.yeelink.spi.Yeelink;
import net.wendal.yeelink.spi.YeelinkDev;
import net.wendal.yeelink.spi.YeelinkObject;
import net.wendal.yeelink.spi.YeelinkSensor;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SimpleYeelink implements Yeelink {
	
	protected String url_prefix = "http://api.yeelink.net/v1.1";

	protected String apikey;

	public SimpleYeelink(String apikey) {
		this.apikey = apikey;
	}

	public String createDev(YeelinkDev dev) {
		return getMap("/devices", "POST", dev).getAsString("device_id");
	}

	public void updateDev(YeelinkDev dev) {
		_send("/devices/"+dev.getDevice_id(), "POST", dev);
	}
	
	public List<YeelinkDev> listDev() {
		List<YeelinkDev> devs = new ArrayList<YeelinkDev>();
		List<YeelinkObject> resp = getList("/devices", "GET", null);
		for (YeelinkObject obj : resp) {
			devs.add(new YeelinkDev(obj));
		}
		return devs;
	}

	public YeelinkDev getDev(String device_id) {
		return new YeelinkDev(getMap("/devices/" + device_id, "GET", null));
	}

	public void delDev(String device_id) {
		_send("/devices/"+device_id, "DELETE", null);
	}

	public String createSensor(String device_id, YeelinkSensor sensor) {
		return getMap("/devices/"+device_id+"/sensors", "POST", sensor).getAsString("sensor_id");
	}

	public void updateSensor(String device_id, YeelinkSensor sensor) {
		_send("/devices/"+device_id+"/sensors", "POST", sensor);
	}

	public List<YeelinkSensor> getSensors(String device_id) {
		List<YeelinkSensor> sensors = new ArrayList<YeelinkSensor>();
		List<YeelinkObject> resp = getList("/devices/"+device_id+"/sensors", "GET", null);
		for (YeelinkObject obj : resp) {
			sensors.add(new YeelinkSensor(obj));
		}
		return sensors;
	}

	public void delSensor(String device_id, String sensor_id) {
		_send("/devices/"+device_id+"/sensors", "DELETE", null);
	}
	
	protected static SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
	protected static SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

	public void upload(String device_id, String sensor_id, Object val) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("value", val);
		_send(String.format("/device/%s/sensor/%s/datapoints", device_id, sensor_id), "POST", map);
	}

	public void upload(String device_id, String sensor_id, String key, YeelinkObject val) {
		
	}
	
	protected Object _send(String uri, String method, Map obj) {
		Map<String, String> headers = new LinkedHashMap<String, String>();
		headers.put("U-ApiKey", apikey);
			
		try {
			byte[] resp = Yeelinks.http(url_prefix + uri, method, headers, JSONObject.toJSONString(obj).getBytes(), 2000);
			if (resp == null || resp.length == 0)
				return null;
			return JSONValue.parse(new String(resp));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected YeelinkObject getMap(String uri, String method, Map obj) {
		Object resp = _send(uri, method, obj);
		if (resp == null)
			return null;
		if (resp instanceof Map)
			return new YeelinkObject(obj);
		throw new IllegalArgumentException("Not map resp " + resp);
	}
	
	protected List<YeelinkObject> getList(String uri, String method, Map obj) {
		Object resp = _send(uri, method, obj);
		if (resp == null)
			return null;
		if (resp instanceof List) {
			List<YeelinkObject> list = new ArrayList<YeelinkObject>();
			for (Object tmp : (List)resp) {
				list.add(new YeelinkObject((Map<String, ? extends Object>) tmp));
			}
			return list;
		}
		throw new IllegalArgumentException("Not list resp " + resp);
	}
}
