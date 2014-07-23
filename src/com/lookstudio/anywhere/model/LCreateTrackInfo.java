package com.lookstudio.anywhere.model;

import java.io.Serializable;

public class LCreateTrackInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String ori; // 出发点地址（文字描述）
	private String dst; //目的地地址（文字描述）
	private String len; //路程全长，单位：米
	private String dur; //耗时，单位：秒
	private String coords;//【数组】坐标集，格式：[lon1,lat1,lon2,lat2,…]
	private String ct; //创建时间，长整形时间戳格式
	
	public String getCt() {
		return ct;
	}
	public void setCt(String ct) {
		this.ct = ct;
	}
	public String getOri() {
		return ori;
	}
	public void setOri(String ori) {
		this.ori = ori;
	}
	public String getDst() {
		return dst;
	}
	public void setDst(String dst) {
		this.dst = dst;
	}
	public String getLen() {
		return len;
	}
	public void setLen(String len) {
		this.len = len;
	}
	public String getDur() {
		return dur;
	}
	public void setDur(String dur) {
		this.dur = dur;
	}
	public String getCoords() {
		return coords;
	}
	public void setCoords(String coords) {
		this.coords = coords;
	}
	public LCreateTrackInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LCreateTrackInfo(String ori, String dst, String len, String dur, String coords, String ct) {
		super();
		this.ori = ori;
		this.dst = dst;
		this.len = len;
		this.dur = dur;
		this.coords = coords;
		this.ct = ct;
	}
	
	
	public LCreateTrackInfo getLCreateTrackInfo()
	{
		return new LCreateTrackInfo(ori,dst, len, dur, coords, ct);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result
//				+ ((password == null) ? 0 : password.hashCode());
//		result = prime * result
//				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LCreateTrackInfo other = (LCreateTrackInfo) obj;
//		if (password == null) {
//			if (other.password != null)
//				return false;
//		} else if (!password.equals(other.password))
//			return false;
//		if (username == null) {
//			if (other.username != null)
//				return false;
//		} else if (!username.equals(other.username))
//			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "LCreateTrackInfo [ori=" + ori + ", dst=" + dst
				+  ", len=" + len+ ", dur=" + dur+ ", coords=" + coords+ ", ct=" + ct+ "]";
	}
	
}
