package com.gcl.simpleweather.utils;

import android.util.Log;

public class LogUtils {
	public final static int VERBOSE = 1;
	public final static int DEBUG = 2;
	public final static int INFO = 3;
	public final static int WARN = 4;
	public final static int ERROR = 5;
	public final static int NOTHING = 6;

	public final static int LEVEL = VERBOSE;

	public static void verbose(String tag, String msg) {
		if (LEVEL <= VERBOSE) {
			Log.v(tag, msg);
		}
	}
	public static void debug(String tag, String msg) {
		if (LEVEL <= DEBUG) {
			Log.d(tag, msg);
		}
	}
	public static void info(String tag, String msg) {
		if (LEVEL <= INFO) {
			Log.i(tag, msg);
		}
	}
	public static void warn(String tag, String msg) {
		if (LEVEL <= WARN) {
			Log.w(tag, msg);
		}
	}
	public static void error(String tag, String msg) {
		if (LEVEL <= ERROR) {
			Log.e(tag, msg);
		}
	}
	System.out.println("hello world");

}
