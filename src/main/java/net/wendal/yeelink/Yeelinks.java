package net.wendal.yeelink;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import net.wendal.yeelink.impl.SimpleYeelink;
import net.wendal.yeelink.spi.Yeelink;

public class Yeelinks {

	public static Yeelink make(String apikey) {
		return new SimpleYeelink(apikey);
	}
	
	public static byte[] http(String url, String method, Map<String, String> headers, byte[] body, int timeout) throws IOException {
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

}
