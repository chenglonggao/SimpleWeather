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
 * 用来更新桌面小控件的service服务
 * 
 * @author Administrator
 * 
 */
public class UpdateWeatherService extends Service {
	protected static final int UPDATE_WEATHER = 1;
	protected static final int UPDATE_RATE = 2;
	
	/* 用来接收系统time_tick广播来更新time的广播接收者 */
	MyReceiver timeTickBroadcastReceiver;

	/* 声明RemoteViews用来为appwidget布局 */
	RemoteViews remoteView;

	/* 显示时间的imageView的ID数组 */
	int[] timeViewID = new int[] { R.id.h1, R.id.h2, R.id.min1, R.id.min2 };
	/* 时间数字的drawable的Id数组 */
	int[] timeDrawableID = new int[] { R.drawable.n0, R.drawable.n1,
			R.drawable.n2, R.drawable.n3, R.drawable.n4, R.drawable.n5,
			R.drawable.n6, R.drawable.n7, R.drawable.n8, R.drawable.n9 };

	/* 天气的中文名称数组String数组 */
	String[] weatherCN = new String[] { "晴", "多云", "阴", "阵雨", "雷阵雨", "雷阵雨伴着冰雹",
			"雨夹雪", "小雨", "中雨", "大雨", "暴雨", "小雪" };
	/* 天气对应的drawable图片id */
	int[] weatherPH = new int[] { R.drawable.sunny, R.drawable.coudy,
			R.drawable.overcast, R.drawable.shower, R.drawable.thundershower,
			R.drawable.thundershower_with_hail, R.drawable.sleet,
			R.drawable.light_rain, R.drawable.moderate_rain,
			R.drawable.heavy_rain, R.drawable.storm, R.drawable.light_snow };

	/**
	 * 1、服务第一次开启后调用的方法，updatetime,和updateweather
	 */
	@Override
	public void onCreate() {
		
		// 1、 初始化RemoteViews(用来在homeUi中显示该远程view)
		remoteView = new RemoteViews(getApplication().getPackageName(),
				R.layout.appwidget_layout);

		// 点击桌面小控件，跳转到MainActivity的界面中
		Intent intentActivity = new Intent(getApplicationContext(),
				MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, intentActivity, 0);
		remoteView.setOnClickPendingIntent(R.id.ll_whole, pendingIntent);

		// 0、检查网络连接情况
		if (checkNetwork() == true) {

			// 2、 第一次安装时更新时间和天气
			updateTime();
			updateWeather();

			// 3、创建一个timer定时器用来每个半小时调用updateweather方法来更新天气

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

			Toast.makeText(getApplicationContext(), "网络连接异常，请您连接网络，并重新安装窗口小工具",
					Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * 2、每次显式start该服务都会调用该方法 在该方法中代码注册一个BroadcastReceiver用来接收time_tick广播
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

		/** 当接收到系统发送的TIME_TICK广播后就开始跟新时间 */
		@Override
		public void onReceive(Context context, Intent intent) {
			// 实时跟新天气
			updateTime();
		}

	}

	/**
	 * 更新appwidget显示时间操作
	 */
	private void updateTime() {

		// 1、得到当前时间：格式是hhmm
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("hhmm");
		String currentTime = format.format(date);

		// 2、将时间显示到appwidget的layout中
		for (int i = 0; i < currentTime.length(); i++) {
			String number = currentTime.substring(i, i + 1);
			remoteView.setImageViewResource(timeViewID[i],
					timeDrawableID[Integer.valueOf(number).intValue()]);
		}

		// 3、将改变更新给appwidget
		ComponentName componentName = new ComponentName(getApplication()
				.getPackageName(), "com.gcl.simpleweather.MyAppWidget");
		AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(
				componentName, remoteView);

	}

	/**
	 * 更新appwidget天气信息操作
	 */
	private void updateWeather() {

		// 将对网络请求这样的费时操作放在子线程中
		new Thread() {

			@Override
			public void run() {
				// 获取天气信息
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
	 * 用来处理子线程与主线程通信 1、updateweather子线程更新界面操作 2、固定频率updateweather任务
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_WEATHER:// 更新天气信息的操作
				Weather weather = (Weather) msg.obj;

				// 更新天气图片
				String weatherState = weather.getWeatherState();

				for (int i = 0; i < weatherCN.length; i++) {
					if (weatherState.equals(weatherCN[i])) {
						remoteView.setImageViewResource(R.id.weatherPhoto,
								weatherPH[i]);
					}
				}

				// 更新天气情况与城市名称
				remoteView.setTextViewText(R.id.weatherState, weatherState);
				remoteView.setTextViewText(R.id.city, weather.getCity());

				// 更新温度
				String temp = weather.getTemp();
				String h_temp = weather.getH_temp();
				String l_temp = weather.getL_temp();

				remoteView.setTextViewText(R.id.temp, temp + "℃");
				remoteView.setTextViewText(R.id.h_tmp, h_temp + "℃");
				remoteView.setTextViewText(R.id.l_tmp, l_temp + "℃");

				// 更新date
				String date = weather.getDate();
				String[] nums = date.split("-");
				String newDate = nums[1] + nums[2] + "-" + "周"
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
	 * 设置一个检查网络的方法
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
	 * 3、当服务被停止时调用该方法，在该方法中取消注册timeTickBroadcastReceiver
	 */
	@Override
	public void onDestroy() {
		unregisterReceiver(timeTickBroadcastReceiver);
		timeTickBroadcastReceiver = null;// 节省内存资源
		super.onDestroy();
		
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
