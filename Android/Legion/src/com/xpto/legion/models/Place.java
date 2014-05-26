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

	private long checkins;

	public long getCheckins() {
		return checkins;
	}

	public void setCheckins(long _checkins) {
		if (_checkins >= 0)
			checkins = _checkins;
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

			if (hasValue(_json, "Id"))
				setId(_json.getLong("Id"));

			if (hasValue(_json, "UserId"))
				setUserId(_json.getLong("UserId"));

			if (hasValue(_json, "UserName"))
				setUserName(_json.getString("UserName"));

			if (hasValue(_json, "Latitude"))
				setLatitude(_json.getDouble("Latitude"));

			if (hasValue(_json, "Longitude"))
				setLongitude(_json.getDouble("Longitude"));

			if (hasValue(_json, "Type"))
				setType(_json.getLong("Type"));

			if (hasValue(_json, "Name"))
				setName(_json.getString("Name"));

			if (hasValue(_json, "Description"))
				setDescription(_json.getString("Description"));

			if (hasValue(_json, "Date"))
				setWhen(Util.parseJSONDate(_json.getString("Date")));

			if (hasValue(_json, "Points"))
				setPoints(_json.getLong("Points"));

			if (hasValue(_json, "Checkins"))
				setCheckins(_json.getLong("Checkins"));
			
			if (hasValue(_json, "Subjects"))
				setSubjects(_json.getLong("Subjects"));

			if (hasValue(_json, "Likes"))
				setLikes(_json.getLong("Likes"));

			if (hasValue(_json, "Dislikes"))
				setDislikes(_json.getLong("Dislikes"));

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
