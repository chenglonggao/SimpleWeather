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
 * 用来从网络上获取天气信息
 * 
 * @author Administrator
 * 
 */
public class GetWeather {

	Weather weather;

	/**
	 * 解析当前天气信息
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
			// 填入apikey到HTTP header
			conn.setRequestProperty("apikey",
					"5ceeb5e20ef91d89c21e4f325ad5a537");
			// conn.connect();
			conn.setReadTimeout(5000);
			int code = conn.getResponseCode();

			// 采用json来解析网页上的weather信息

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
				// 将获取的天气信息封装到weather对象中
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
