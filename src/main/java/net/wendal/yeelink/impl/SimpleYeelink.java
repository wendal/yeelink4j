package net.wendal.yeelink.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import net.wendal.yeelink.Yeelinks;
import net.wendal.yeelink.spi.Yeelink;
import net.wendal.yeelink.spi.YeelinkDev;
import net.wendal.yeelink.spi.YeelinkObject;
import net.wendal.yeelink.spi.YeelinkSensor;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SimpleYeelink implements Yeelink {
	
	protected String url_prefix = "http://api.yeelink.net/v1.1";

	protected String apikey;

	public SimpleYeelink(String apikey) {
		super();
		this.apikey = apikey;
	}

	public String createDev(YeelinkDev dev) {
		return null;
	}

	public String updateDev(YeelinkDev dev) {
		return null;
	}

	public YeelinkDev getDev(String device_id) {
		return null;
	}

	public void delDev(String device_id) {
	}

	public String createSensor(String device_id, YeelinkSensor sensor) {
		return null;
	}

	public String updateSensor(String device_id, YeelinkSensor sensor) {
		return null;
	}

	public List<YeelinkSensor> getSensors(String device_id) {
		return null;
	}

	public void delSensor(String device_id, String sensor_id) {
	}
	
	protected static SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
	protected static SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

	public String upload(String device_id, String sensor_id, Object val) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Date date = new Date();
		map.put("timestamp", ((DateFormat)day.clone()).format(date) + "T" + ((DateFormat)time.clone()).format(date));
		map.put("value", val);
		send(String.format("/device/%s/sensor/%s/datapoints", device_id, sensor_id), "POST", map);
		return null;
	}

	public String upload(String device_id, String sensor_id, String key, YeelinkObject val) {
		return null;
	}

	protected YeelinkObject send(String uri, String method, Map obj) {
		Map<String, String> headers = new LinkedHashMap<String, String>();
		headers.put("U-ApiKey", "f7bd63b34b30303a11a36f6fd7628ef4");
			
		try {
			byte[] resp = Yeelinks.http(url_prefix + uri, method, headers, JSONObject.toJSONString(obj).getBytes(), 2000);
			if (resp == null || resp.length == 0)
				return null;
			Map map = (Map)JSONValue.parse(new String(resp));
			YeelinkObject _obj = new YeelinkObject();
			_obj.putAll(map);
			return _obj;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}