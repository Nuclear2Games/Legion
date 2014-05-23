package com.xpto.legion.data;

import org.json.JSONObject;

import android.app.Activity;

import com.xpto.legion.utils.LCallback;
import com.xpto.legion.utils.WSCaller;

public class Caller {
	private static final String URL_WS = "http://www.xptogames.com.br/Legion/Legion.svc/";
	// private static final String URL_WS = "http://192.168.0.31/legion.svc/";
	// private static final String URL_WS = "http://10.20.0.59/legion.svc/";
	private static final int RETRY_NO = 0;
	private static final int RETRY_YES = 5;

	public static void newUser(Activity activity, LCallback success, LCallback retry, LCallback fail, String login, String password) {
		try {
			JSONObject params = new JSONObject();

			params.put("login", login);
			params.put("password", password);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "NewUser", params, WSCaller.WS_CACHE_NO, RETRY_NO, true);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}

	public static void login(Activity activity, LCallback success, LCallback retry, LCallback fail, String login, String password) {
		try {
			JSONObject params = new JSONObject();

			params.put("login", login);
			params.put("password", password);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "Login", params, WSCaller.WS_CACHE_NO, RETRY_YES, true);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}

	public static void updateUser(Activity activity, LCallback success, LCallback retry, LCallback fail, long id, String name, String description) {
		try {
			JSONObject params = new JSONObject();

			params.put("id", id);
			params.put("name", name);
			params.put("description", description);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "UpdateUser", params, WSCaller.WS_CACHE_NO, RETRY_NO, true);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}

	public static void updatePassword(Activity activity, LCallback success, LCallback retry, LCallback fail, long id, String passwordOld, String passwordNew) {
		try {
			JSONObject params = new JSONObject();

			params.put("id", id);
			params.put("passwordOld", passwordOld);
			params.put("passwordNew", passwordNew);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "UpdatePassword", params, WSCaller.WS_CACHE_NO, RETRY_NO, true);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}

	public static void newPlace(Activity activity, LCallback success, LCallback retry, LCallback fail, long user, double latitude, double longitude,
			long type, String name, String description) {
		try {
			JSONObject params = new JSONObject();

			params.put("user", user);
			params.put("latitude", latitude);
			params.put("longitude", longitude);
			params.put("type", type);
			params.put("name", name);
			params.put("description", description);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "NewPlace", params, WSCaller.WS_CACHE_NO, RETRY_NO, true);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}

	public static void updatePlace(Activity activity, LCallback success, LCallback retry, LCallback fail, long user, long place, double latitude,
			double longitude, String name, String description) {
		try {
			JSONObject params = new JSONObject();

			params.put("user", user);
			params.put("place", place);
			params.put("latitude", latitude);
			params.put("longitude", longitude);
			params.put("name", name);
			params.put("description", description);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "UpdatePlace", params, WSCaller.WS_CACHE_NO, RETRY_NO, true);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}

	public static void newSubject(Activity activity, LCallback success, LCallback retry, LCallback fail, long user, long place, String content) {
		try {
			JSONObject params = new JSONObject();

			params.put("user", user);
			params.put("place", place);
			params.put("content", content);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "NewSubject", params, WSCaller.WS_CACHE_NO, RETRY_NO, true);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}

	public static void newComment(Activity activity, LCallback success, LCallback retry, LCallback fail, long user, long subject, String content) {
		try {
			JSONObject params = new JSONObject();

			params.put("user", user);
			params.put("subject", subject);
			params.put("content", content);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "NewComment", params, WSCaller.WS_CACHE_NO, RETRY_NO, true);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}

	public static void newAnswer(Activity activity, LCallback success, LCallback retry, LCallback fail, long user, long comment, String content) {
		try {
			JSONObject params = new JSONObject();

			params.put("user", user);
			params.put("comment", comment);
			params.put("content", content);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "NewAnswer", params, WSCaller.WS_CACHE_NO, RETRY_NO, true);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}

	public static void newLike(Activity activity, LCallback success, LCallback retry, LCallback fail, long user, long customId, long customTypeId,
			boolean isLike) {
		try {
			JSONObject params = new JSONObject();

			params.put("user", user);
			params.put("customId", customId);
			params.put("customTypeId", customTypeId);
			params.put("isLike", isLike);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "NewLike", params, WSCaller.WS_CACHE_NO, RETRY_NO, true);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}

	public static void getUser(Activity activity, LCallback success, LCallback retry, LCallback fail, long user) {
		try {
			JSONObject params = new JSONObject();

			params.put("user", user);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "GetUser", params, WSCaller.WS_CACHE_FAST, RETRY_YES, false);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}

	public static void getNotifications(Activity activity, LCallback success, LCallback retry, LCallback fail, long user) {
		try {
			JSONObject params = new JSONObject();

			params.put("user", user);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "GetNotifications", params, WSCaller.WS_CACHE_FAST, RETRY_YES, false);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}

	public static void readNotifications(Activity activity, LCallback success, LCallback retry, LCallback fail, long user) {
		try {
			JSONObject params = new JSONObject();

			params.put("user", user);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "ReadNotifications", params, WSCaller.WS_CACHE_NO, RETRY_YES, false);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}

	public static void getNearPlaces(Activity activity, LCallback success, LCallback retry, LCallback fail, double latitude, double longitude) {
		try {
			JSONObject params = new JSONObject();

			params.put("latitude", latitude);
			params.put("longitude", longitude);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "GetNearPlaces", params, WSCaller.WS_CACHE_FAST, RETRY_YES, false);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}

	public static void getSubjects(Activity activity, LCallback success, LCallback retry, LCallback fail, long place) {
		try {
			JSONObject params = new JSONObject();

			params.put("place", place);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "GetSubjects", params, WSCaller.WS_CACHE_FAST, RETRY_YES, false);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}

	public static void getComments(Activity activity, LCallback success, LCallback retry, LCallback fail, long subject) {
		try {
			JSONObject params = new JSONObject();

			params.put("subject", subject);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "GetComments", params, WSCaller.WS_CACHE_FAST, RETRY_YES, false);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}

	public static void getAnswers(Activity activity, LCallback success, LCallback retry, LCallback fail, long comment) {
		try {
			JSONObject params = new JSONObject();

			params.put("comment", comment);

			WSCaller.asyncCall(activity, success, retry, fail, URL_WS, "GetAnswers", params, WSCaller.WS_CACHE_FAST, RETRY_YES, false);
		} catch (Exception e) {
			success.finished(null);
			fail.finished(e);
		}
	}
}
