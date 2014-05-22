package com.xpto.legion.models;

import java.util.Date;

import org.json.JSONObject;

import com.xpto.legion.utils.Util;

public class Notification extends Default {
	private long userId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long _userId) {
		if (_userId >= 0)
			userId = _userId;
	}

	private long customId;

	public long getCustomId() {
		return customId;
	}

	public void setCustomId(long _customId) {
		if (_customId >= 0)
			customId = _customId;
	}

	private byte customTypeId;

	public byte getCustomTypeId() {
		return customTypeId;
	}

	public void setCustomTypeId(byte _customTypeId) {
		if (_customTypeId >= 0)
			customTypeId = _customTypeId;
	}

	private long what;

	public long getWhat() {
		return what;
	}

	public void setWhat(long _what) {
		if (_what >= 0)
			what = _what;
	}

	private Date when;

	public Date getWhen() {
		return when;
	}

	public void setWhen(Date _when) {
		if (_when != null)
			when = _when;
	}

	private boolean seen;

	public boolean getSeen() {
		return seen;
	}

	public void setSeen(boolean _seen) {
		seen = _seen;
	}

	@Override
	public boolean loadFromJSon(JSONObject _json) {
		try {
			if (_json == null)
				return false;

			if (hasValue(_json, "id"))
				setId(_json.getLong("id"));

			if (hasValue(_json, "userId"))
				setUserId(_json.getLong("userId"));

			if (hasValue(_json, "customId"))
				setCustomId(_json.getLong("customId"));

			if (hasValue(_json, "customTypeId"))
				setCustomTypeId((byte) _json.getInt("customTypeId"));

			if (hasValue(_json, "what"))
				setWhat(_json.getLong("what"));

			if (hasValue(_json, "date"))
				setWhen(Util.parseJSONDate(_json.getString("date")));

			if (hasValue(_json, "seen"))
				setSeen(_json.getBoolean("seen"));

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
