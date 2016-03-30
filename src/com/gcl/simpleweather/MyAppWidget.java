package com.gcl.simpleweather;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;

/**
 * 显示与更新桌面小控件的类
 * 
 * @author Administrator
 * 
 */
public class MyAppWidget extends AppWidgetProvider {

	int count = 1;
	int num;

	/*
	 * 第一次安装小控件 AppWidgetService会调用该方法，来开启一个appwidget的service
	 * 一般地当某个AppWidget第一次被手机用户创建时
	 * ，会发送一条Enabled和Update广播，之后如果用户再创建这个AppWidget时，则只会发送一条Update广播，
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		num = count++;
		System.out.println("onUpdate()方法被调用" + num
				+ "次-----------------------------");
		Intent intent = new Intent(context, UpdateWeatherService.class);
		context.startService(intent);
	}

	// /**
	// * 每删除一个appwidget Instance就会调用一次该方法
	// */
	// @Override
	// public void onDeleted(Context context, int[] appWidgetIds) {
	// int num = count++;
	// System.out.println("onDeleted()方法被调用" + num
	// + "次-----------------------------");
	//
	// }

	/**
	 * 当最后一个appwidget被删除时调用的方法 ondelete()是每一个appwidget被删除时调用的方法
	 */
	@Override
	public void onDisabled(Context context) {
		int num = count++;
		System.out.println("onDisabled()方法被调用" + num
				+ "次-----------------------------");

		Intent intent = new Intent(context, UpdateWeatherService.class);
		context.stopService(intent);

	}

}
