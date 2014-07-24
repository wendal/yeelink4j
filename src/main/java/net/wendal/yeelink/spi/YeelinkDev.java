package net.wendal.yeelink.spi;

import java.util.Map;

@SuppressWarnings("unchecked")
public class YeelinkDev extends YeelinkObject {
	
	private static final long serialVersionUID = -7450853330989936370L;
	
	public YeelinkDev() {
	}
	
	public String getDevice_id() {
		return getAsString("device_id");
	}
	
	public void setDevice_id(String device_id) {
		put("device_id", device_id);
	}
	
	public String getTitle() {
		return getAsString("title");
	}
	public void setTitle(String title) {
		put("title", title);
	}
	public String getAbout() {
		return getAsString("about");
	}
	public void setAbout(String about) {
		put("about", about);
	}
	public String getTags() {
		return getAsString("tags");
	}
	public void setTags(String tags) {
		put("tags", tags);
	}
	
	public YeelinkLocation getLocation() {
		Object obj = get("location");
		if (obj == null)
			return null;
		if (obj instanceof YeelinkLocation)
			return (YeelinkLocation)obj;
		if (obj instanceof Map){
			YeelinkLocation loc = new YeelinkLocation();
			loc.putAll((Map)obj);
			put("location", loc);
			return loc;
		}
		return null;
	}
	
	public void setLocation(YeelinkLocation location) {
		put("location", location);
	}
}
