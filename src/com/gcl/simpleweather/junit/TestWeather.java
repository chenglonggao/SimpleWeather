package com.gcl.simpleweather.junit;

import android.test.AndroidTestCase;

import com.gcl.simpleweather.bean.Weather;
import com.gcl.simpleweather.utils.GetWeather;

public class TestWeather extends AndroidTestCase {
	
	public void testWeather(){
		GetWeather get = new GetWeather();
		Weather weather = get.parseWeather();
		
		String date = weather.getDate();
		String[] nums = date.split("-");
		String newDate = "20" + nums[0] + nums[1] + nums[2];
		System.out.println(newDate);
		System.out.println(getContext().getPackageName());
	}
	

}
