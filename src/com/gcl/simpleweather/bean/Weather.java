package com.gcl.simpleweather.bean;

/**
 * ����������װweather����Ϣ����
 * 
 * @author Administrator
 * 
 */
public class Weather {
	// ��������
	String city;
	// �������
	String weatherState;
	// ��ǰ�¶�
	String temp;
	// ����¶�
	String l_temp;
	// ����¶�
	String h_temp;
	// ����
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
