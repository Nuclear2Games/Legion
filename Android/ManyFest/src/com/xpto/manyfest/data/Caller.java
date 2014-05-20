package com.xpto.manyfest.data;

import org.json.JSONObject;

import com.xpto.manyfest.utils.MFCallback;
import com.xpto.manyfest.utils.WSCaller;

import android.app.Activity;

public class Caller {
	private static final String URL_WS = "http://54.242.85.210/nanyfest/manyfest.svc/";
	private static final int RETRY_NO = 0;
	private static final int RETRY_YES = 5;

	public static void getNearPlaces(Activity activity, MFCallback success, MFCallback retry, MFCallback fail, double longitude, double latitude) {
		try {
			JSONObject params = new JSONObject();

			params.put("latitude", "" + latitude);
			params.put("longitude", "" + longitude);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "GetNearPlaces", params, WSCaller.WS_CACHE_NO, RETRY_YES, false);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}
}
