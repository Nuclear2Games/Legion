package com.xpto.legion.models;

import org.json.JSONObject;

public abstract class Default {
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long _id) {
		if (_id >= 0)
			id = _id;
	}

	public abstract boolean loadFromJSon(JSONObject _json);

	public static boolean hasValue(JSONObject _json, String _tag) {
		if (!_json.has(_tag))
			return false;

		try {
			if (_json.get(_tag) == null)
				return false;

			if (_json.isNull(_tag))
				return false;

			if (_json.getString(_tag) == null)
				return false;

			if (_json.getString(_tag).equals(JSONObject.NULL))
				return false;

			if (_json.getString(_tag).trim().length() == 0)
				return false;

			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
