package com.gcl.simpleweather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.webkit.WebView.FindListener;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.simpleweather.R;
import com.gcl.simpleweather.bean.Weather;
import com.gcl.simpleweather.utils.GetWeather;

/**
 * ������������С�ؼ���service����
 * 
 * @author Administrator
 * 
 */
public class UpdateWeatherService extends Service {
	protected static final int UPDATE_WEATHER = 1;
	protected static final int UPDATE_RATE = 2;
	
	/* ��������ϵͳtime_tick�㲥������time�Ĺ㲥������ */
	MyReceiver timeTickBroadcastReceiver;

	/* ����RemoteViews����Ϊappwidget���� */
	RemoteViews remoteView;

	/* ��ʾʱ���imageView��ID���� */
	int[] timeViewID = new int[] { R.id.h1, R.id.h2, R.id.min1, R.id.min2 };
	/* ʱ�����ֵ�drawable��Id���� */
	int[] timeDrawableID = new int[] { R.drawable.n0, R.drawable.n1,
			R.drawable.n2, R.drawable.n3, R.drawable.n4, R.drawable.n5,
			R.drawable.n6, R.drawable.n7, R.drawable.n8, R.drawable.n9 };

	/* ������������������String���� */
	String[] weatherCN = new String[] { "��", "����", "��", "����", "������", "��������ű���",
			"���ѩ", "С��", "����", "����", "����", "Сѩ" };
	/* ������Ӧ��drawableͼƬid */
	int[] weatherPH = new int[] { R.drawable.sunny, R.drawable.coudy,
			R.drawable.overcast, R.drawable.shower, R.drawable.thundershower,
			R.drawable.thundershower_with_hail, R.drawable.sleet,
			R.drawable.light_rain, R.drawable.moderate_rain,
			R.drawable.heavy_rain, R.drawable.storm, R.drawable.light_snow };

	/**
	 * 1�������һ�ο�������õķ�����updatetime,��updateweather
	 */
	@Override
	public void onCreate() {
		
		// 1�� ��ʼ��RemoteViews(������homeUi����ʾ��Զ��view)
		remoteView = new RemoteViews(getApplication().getPackageName(),
				R.layout.appwidget_layout);

		// �������С�ؼ�����ת��MainActivity�Ľ�����
		Intent intentActivity = new Intent(getApplicationContext(),
				MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, intentActivity, 0);
		remoteView.setOnClickPendingIntent(R.id.ll_whole, pendingIntent);

		// 0����������������
		if (checkNetwork() == true) {

			// 2�� ��һ�ΰ�װʱ����ʱ�������
			updateTime();
			updateWeather();

			// 3������һ��timer��ʱ������ÿ����Сʱ����updateweather��������������

			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					Message msg = new Message();
					msg.what = UPDATE_RATE;

					handler.sendMessage(msg);

				}
			}, 0, 1000 * 60 * 30);

		} else {

			Intent intent = new Intent();
			intent.setClassName("com.android.settings",
					"com.android.settings.WirelessSettings");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);

			Toast.makeText(getApplicationContext(), "���������쳣�������������磬�����°�װ����С����",
					Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * 2��ÿ����ʽstart�÷��񶼻���ø÷��� �ڸ÷����д���ע��һ��BroadcastReceiver��������time_tick�㲥
	 * updateTime
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		timeTickBroadcastReceiver = new MyReceiver();
		IntentFilter updateTimeIntent = new IntentFilter();
		updateTimeIntent.addAction("android.intent.action.TIME_TICK");
		registerReceiver(timeTickBroadcastReceiver, updateTimeIntent);

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * updateTime
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyReceiver extends BroadcastReceiver {

		/** �����յ�ϵͳ���͵�TIME_TICK�㲥��Ϳ�ʼ����ʱ�� */
		@Override
		public void onReceive(Context context, Intent intent) {
			// ʵʱ��������
			updateTime();
		}

	}

	/**
	 * ����appwidget��ʾʱ�����
	 */
	private void updateTime() {

		// 1���õ���ǰʱ�䣺��ʽ��hhmm
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("hhmm");
		String currentTime = format.format(date);

		// 2����ʱ����ʾ��appwidget��layout��
		for (int i = 0; i < currentTime.length(); i++) {
			String number = currentTime.substring(i, i + 1);
			remoteView.setImageViewResource(timeViewID[i],
					timeDrawableID[Integer.valueOf(number).intValue()]);
		}

		// 3�����ı���¸�appwidget
		ComponentName componentName = new ComponentName(getApplication()
				.getPackageName(), "com.gcl.simpleweather.MyAppWidget");
		AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(
				componentName, remoteView);

	}

	/**
	 * ����appwidget������Ϣ����
	 */
	private void updateWeather() {

		// �����������������ķ�ʱ�����������߳���
		new Thread() {

			@Override
			public void run() {
				// ��ȡ������Ϣ
				GetWeather get = new GetWeather();
				Weather weather = get.parseWeather();

				Message msg = new Message();
				msg.what = UPDATE_WEATHER;
				msg.obj = weather;

				handler.sendMessage(msg);

			}

		}.start();

	}

	/**
	 * �����������߳������߳�ͨ�� 1��updateweather���̸߳��½������ 2���̶�Ƶ��updateweather����
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_WEATHER:// ����������Ϣ�Ĳ���
				Weather weather = (Weather) msg.obj;

				// ��������ͼƬ
				String weatherState = weather.getWeatherState();

				for (int i = 0; i < weatherCN.length; i++) {
					if (weatherState.equals(weatherCN[i])) {
						remoteView.setImageViewResource(R.id.weatherPhoto,
								weatherPH[i]);
					}
				}

				// ��������������������
				remoteView.setTextViewText(R.id.weatherState, weatherState);
				remoteView.setTextViewText(R.id.city, weather.getCity());

				// �����¶�
				String temp = weather.getTemp();
				String h_temp = weather.getH_temp();
				String l_temp = weather.getL_temp();

				remoteView.setTextViewText(R.id.temp, temp + "��");
				remoteView.setTextViewText(R.id.h_tmp, h_temp + "��");
				remoteView.setTextViewText(R.id.l_tmp, l_temp + "��");

				// ����date
				String date = weather.getDate();
				String[] nums = date.split("-");
				String newDate = nums[1] + nums[2] + "-" + "��"
						+ (new Date().getDay() + 1);

				remoteView.setTextViewText(R.id.date, newDate + "  ");

				ComponentName componentName = new ComponentName(
						getApplication().getPackageName(),
						"com.gcl.simpleweather.MyAppWidget");
				AppWidgetManager.getInstance(getApplicationContext())
						.updateAppWidget(componentName, remoteView);

				break;

			case UPDATE_RATE:
				updateWeather();

				break;

			}
		}

	};

	/**
	 * ����һ���������ķ���
	 */
	public boolean checkNetwork() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();

		if (null != info && info.isConnected()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 3��������ֹͣʱ���ø÷������ڸ÷�����ȡ��ע��timeTickBroadcastReceiver
	 */
	@Override
	public void onDestroy() {
		unregisterReceiver(timeTickBroadcastReceiver);
		timeTickBroadcastReceiver = null;// ��ʡ�ڴ���Դ
		super.onDestroy();
		
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
