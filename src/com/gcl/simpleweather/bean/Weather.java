package com.gcl.simpleweather.bean;

/**
 * 该类用来封装weather的信息属性
 * 
 * @author Administrator
 * 
 */
public class Weather {
	// 城市名称
	String city;
	// 天气情况
	String weatherState;
	// 当前温度
	String temp;
	// 最低温度
	String l_temp;
	// 最高温度
	String h_temp;
	// 日期
	String date;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getWeatherState() {
		return weatherState;
	}

	public void setWeatherState(String weatherState) {
		this.weatherState = weatherState;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getL_temp() {
		return l_temp;
	}

	public void setL_temp(String l_temp) {
		this.l_temp = l_temp;
	}

	public String getH_temp() {
		return h_temp;
	}

	public void setH_temp(String h_temp) {
		this.h_temp = h_temp;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Weather [city=" + city + ", weatherState=" + weatherState
				+ ", temp=" + temp + ", l_temp=" + l_temp + ", h_temp="
				+ h_temp + ", date=" + date + "]";
	}
	
	
	
	
	
	
	

}
