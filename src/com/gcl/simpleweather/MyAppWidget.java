package com.gcl.simpleweather;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;

/**
 * ��ʾ���������С�ؼ�����
 * 
 * @author Administrator
 * 
 */
public class MyAppWidget extends AppWidgetProvider {

	int count = 1;
	int num;

	/*
	 * ��һ�ΰ�װС�ؼ� AppWidgetService����ø÷�����������һ��appwidget��service
	 * һ��ص�ĳ��AppWidget��һ�α��ֻ��û�����ʱ
	 * ���ᷢ��һ��Enabled��Update�㲥��֮������û��ٴ������AppWidgetʱ����ֻ�ᷢ��һ��Update�㲥��
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		num = count++;
		System.out.println("onUpdate()����������" + num
				+ "��-----------------------------");
		Intent intent = new Intent(context, UpdateWeatherService.class);
		context.startService(intent);
	}

	// /**
	// * ÿɾ��һ��appwidget Instance�ͻ����һ�θ÷���
	// */
	// @Override
	// public void onDeleted(Context context, int[] appWidgetIds) {
	// int num = count++;
	// System.out.println("onDeleted()����������" + num
	// + "��-----------------------------");
	//
	// }

	/**
	 * �����һ��appwidget��ɾ��ʱ���õķ��� ondelete()��ÿһ��appwidget��ɾ��ʱ���õķ���
	 */
	@Override
	public void onDisabled(Context context) {
		int num = count++;
		System.out.println("onDisabled()����������" + num
				+ "��-----------------------------");

		Intent intent = new Intent(context, UpdateWeatherService.class);
		context.stopService(intent);

	}

}
