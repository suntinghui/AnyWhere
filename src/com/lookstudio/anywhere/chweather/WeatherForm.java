package com.lookstudio.anywhere.chweather;

public class WeatherForm {
	/**������*/
	private String name;
	/**���б��*/
	private String id;
	/**��ǰ����*/
	private String ddate;
	/**����*/
	private String week;
	/**�¶�*/
	private String temp;
	/**��������*/
	private String weather;
	/**����*/
	private String wind;
	/**����*/
	private String fx;
	public WeatherForm(){
		
	}
	/**
	 * ���췽��
	 * @param name
	 * @param id
	 * @param ddate
	 * @param week
	 * @param temp
	 * @param weather
	 * @param wind
	 * @param fx
	 */
	public WeatherForm(String name, String id, String ddate, String week,
			String temp, String weather, String wind, String fx) {
		super();
		this.name = name;
		this.id = id;
		this.ddate = ddate;
		this.week = week;
		this.temp = temp;
		this.weather = weather;
		this.wind = wind;
		this.fx = fx;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDdate() {
		return ddate;
	}
	public void setDdate(String ddate) {
		this.ddate = ddate;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public String getFx() {
		return fx;
	}
	public void setFx(String fx) {
		this.fx = fx;
	}
	@Override
	public String toString() {
		return "WeatherForm [name=" + name + ", id=" + id + ", ddate=" + ddate
				+ ", week=" + week + ", temp=" + temp + ", weather=" + weather
				+ ", wind=" + wind + ", fx=" + fx + "]";
	}
}
