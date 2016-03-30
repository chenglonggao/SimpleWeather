package com.gcl.simpleweather.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.junit.Test;

import android.test.AndroidTestCase;
import android.util.Log;

import com.gcl.simpleweather.bean.Weather;

/**
 * �����������ϻ�ȡ������Ϣ
 * 
 * @author Administrator
 * 
 */
public class GetWeather {

	Weather weather;

	/**
	 * ������ǰ������Ϣ
	 */

	public Weather parseWeather() {
		
		
		weather = new Weather();

		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();

		try {
			URL url = new URL(
					"http://apis.baidu.com/apistore/weatherservice/weather?citypinyin=xinyang");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			// ����apikey��HTTP header
			conn.setRequestProperty("apikey",
					"5ceeb5e20ef91d89c21e4f325ad5a537");
			// conn.connect();
			conn.setReadTimeout(5000);
			int code = conn.getResponseCode();

			// ����json��������ҳ�ϵ�weather��Ϣ

			if (code == 200) {
				InputStream is = conn.getInputStream();
				reader = new BufferedReader(new InputStreamReader(is));
				String strRead = null;
				while ((strRead = reader.readLine()) != null) {
					sb.append(strRead);
					sb.append("\r\n");
				}
				reader.close();
				String jsonStr = sb.toString();
				JSONObject jsonObj = new JSONObject(jsonStr);
				JSONObject weatherObj = jsonObj.getJSONObject("retData");
				// ����ȡ��������Ϣ��װ��weather������
				weather.setCity(weatherObj.getString("city"));
				weather.setDate(weatherObj.getString("date"));
				weather.setH_temp(weatherObj.getString("h_tmp"));
				weather.setL_temp(weatherObj.getString("l_tmp"));
				weather.setTemp(weatherObj.getString("temp"));
				weather.setWeatherState(weatherObj.getString("weather"));
				return weather;
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			new Throwable(e);
			return null;
		}

		
		
		
		
		
	}

}
