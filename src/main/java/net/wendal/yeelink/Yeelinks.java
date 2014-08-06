package net.wendal.yeelink;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Yeelink Simple Client for Java<p/>
 * 无依赖的客户端,供上传数据用 
 * @author wendal(wendal1985@gmail.com)
 *
 */
public class Yeelinks {

	protected static String url_prefix = "http://api.yeelink.net/v1.1";
	
	/**全局的U-ApiKey*/
	public static String UAPIKEY;
	/**默认的设备id, 设置后, 调用upload方法时device_id可以填0*/
	public static int DFT_DEV_ID;
	/**默认的传感器id, 设置后, 调用upload方法时sensor_id可以填0*/
	public static int DFT_SEN_ID;
	/**访问超时设置,默认5秒, 以毫秒数计算*/
	public static int TIMEOUT = 5*1000;
	
	/**上传一个数值型传感器数据*/
	public static void upload(int device_id, int sensor_id, double num) {
		byte[] body = String.format("{\"value\":%s}", num).getBytes();
		_upload(device_id, sensor_id, body);
	}
	
	/**上传一个GPS传感器数据*/
	public static void upload(int device_id, int sensor_id, Map<String, Object> gps) {
		StringBuilder sb = new StringBuilder("{\"value\":{");
		for (Entry<String, Object> en : gps.entrySet()) {
			sb.append(String.format("\"%s\":%s,", en.getKey(), en.getValue().toString()));
		}
		sb.setCharAt(sb.length() - 1, '}');
		sb.append("}");
		_upload(device_id, sensor_id, sb.toString().getBytes());
	}
	
	/**上传一个图像数据或泛型数据(json字符串)*/
	public static void upload(int device_id, int sensor_id, byte[] body) {
		_upload(device_id, sensor_id, body);
	}
	
	protected static Map<String, String> yeelinkHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("U-ApiKey", UAPIKEY);
		return headers;
	}
	
	protected static String _U(int device_id, int sensor_id) {
		if (device_id == 0)
			device_id = DFT_DEV_ID;
		if (sensor_id == 0)
			sensor_id = DFT_SEN_ID;
		return url_prefix + "/device/" + device_id + "/sensor/" + sensor_id + "/datapoints";
	}
	
	protected static void _upload(int device_id, int sensor_id, byte[] body) {
		try {
			http(_U(device_id, sensor_id), "POST", yeelinkHeaders(), body, TIMEOUT);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected static byte[] http(String url, String method, Map<String, String> headers, byte[] body, int timeout) throws IOException {
		if (body == null)
			body = new byte[0];
		System.out.printf("url=%s, method=%s, headers=%s, body=%s, timeout=%s\n", url, method, headers, new String(body).trim(), timeout);
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod(method.toUpperCase());
		conn.setDoInput(true);
		if (headers != null && headers.size() > 0) {
			for (Entry<String, String> en : headers.entrySet()) {
				conn.setRequestProperty(en.getKey(), en.getValue());
			}
		}
		if (body.length != 0) {
			conn.setDoOutput(true);
			conn.setFixedLengthStreamingMode(body.length);
			OutputStream out = conn.getOutputStream();
			out.write(body);
			out.flush();
			out.close();
		}
		conn.connect();
		
		if (conn.getResponseCode() != 200) {
			throw new IllegalArgumentException("resp code=" + conn.getResponseCode());
		}
		InputStream in = conn.getInputStream();
		ByteArrayOutputStream tmp = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		while (true) {
			int len = in.read(buf);
			if (len == -1)
				break;
			if (len > 0)
				tmp.write(buf, 0, len);
		}
		return tmp.toByteArray();
	}

	public static void main(String[] args) throws Exception {
		UAPIKEY = "Your U-ApiKey";

		// 明确指定设备id和传感器id,上传一个数值型数据(这里演示的是一个温度传感器的数据)
		upload(12825, 20872, 30.12);
		
		// 这里指定设备id和传感器id是可选的,是为了在调用upload时可以忽略这两个参数.
		DFT_DEV_ID = 12825; // 你的设备id
		DFT_SEN_ID = 21746; // 传感器id
		Map<String, Object> gps = new HashMap<String, Object>();
		gps.put("lat", 40.0);
		gps.put("lng", 115.0);
		gps.put("speed", 0);
		gps.put("offset", false);
		upload(0, 0, gps); // 传0代表走默认的设备和传感器
		
		
		// 这里演示上传一个图像数据, 实际数据的来源是多种多样的,不局限于文件
//		File f = new File("abc.jpg");
//		byte[] buf = new byte[(int)f.length()];
//		new FileInputStream(f).read(buf);
//		upload(12825, 20956, buf);
		
		// 上传一个泛型数据, 参考api可知需要上传一个json格式数据
		// 其中key是一个唯一键值, value是数据, 可以是复杂的,也可以简单一个字符串
		// 复杂的数据应借助Json序列化类库,而不是下面的拼字符串哦
		String str = String.format("{\"key\":\"%s\", \"value\":{\"mytext\":\"%s\"}}", System.currentTimeMillis(), "http://wendal.net");
		upload(12825, 21749, str.getBytes());
	}
}
