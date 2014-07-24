package net.wendal.yeelink.spi;

@SuppressWarnings("unchecked")
public class YeelinkLocation extends YeelinkObject {

	private static final long serialVersionUID = -1183425111266362885L;

	public String getLocal() {
		return getAsString("local");
	}
	public void setLocal(String local) {
		put("local", local);
	}
	public double getLatitude() {
		return getAsDouble("latitude");
	}
	public void setLatitude(double latitude) {
		put("latitude", latitude);
	}
	public double getLongitude() {
		return getAsDouble("longitude");
	}
	public void setLongitude(double longitude) {
		put("longitude", longitude);
	}
}
