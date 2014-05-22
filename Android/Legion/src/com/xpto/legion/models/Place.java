package com.xpto.legion.models;

import java.util.Date;

import org.json.JSONObject;

import com.xpto.legion.utils.Util;

public class Place extends Default {
	private long userId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long _userId) {
		if (_userId >= 0)
			userId = _userId;
	}

	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String _userName) {
		if (_userName != null)
			userName = _userName;
	}

	private double latitude;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double _latitude) {
		latitude = _latitude;
	}

	private double longitude;

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double _longitude) {
		longitude = _longitude;
	}

	private long type;

	public long getType() {
		return type;
	}

	public void setType(long _type) {
		if (_type >= 0)
			type = _type;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String _name) {
		if (_name != null)
			name = _name;
	}

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String _description) {
		if (_description != null)
			description = _description;
	}

	private Date when;

	public Date getWhen() {
		return when;
	}

	public void setWhen(Date _when) {
		if (_when != null)
			when = _when;
	}

	private long points;

	public long getPoints() {
		return points;
	}

	public void setPoints(long _points) {
		points = _points;
	}

	private long subjects;

	public long getSubjects() {
		return subjects;
	}

	public void setSubjects(long _subjects) {
		if (_subjects >= 0)
			subjects = _subjects;
	}

	private long likes;

	public long getLikes() {
		return likes;
	}

	public void setLikes(long _likes) {
		if (_likes >= 0)
			likes = _likes;
	}

	private long dislikes;

	public long getDislikes() {
		return dislikes;
	}

	public void setDislikes(long _dislikes) {
		if (_dislikes >= 0)
			dislikes = _dislikes;
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

			if (hasValue(_json, "userName"))
				setUserName(_json.getString("userName"));

			if (hasValue(_json, "latitude"))
				setLatitude(_json.getDouble("latitude"));

			if (hasValue(_json, "longitude"))
				setLongitude(_json.getDouble("longitude"));

			if (hasValue(_json, "type"))
				setType(_json.getLong("type"));

			if (hasValue(_json, "name"))
				setName(_json.getString("name"));

			if (hasValue(_json, "description"))
				setDescription(_json.getString("description"));

			if (hasValue(_json, "date"))
				setWhen(Util.parseJSONDate(_json.getString("date")));

			if (hasValue(_json, "points"))
				setPoints(_json.getLong("points"));

			if (hasValue(_json, "subjects"))
				setSubjects(_json.getLong("subjects"));

			if (hasValue(_json, "likes"))
				setLikes(_json.getLong("likes"));

			if (hasValue(_json, "dislikes"))
				setDislikes(_json.getLong("dislikes"));

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
