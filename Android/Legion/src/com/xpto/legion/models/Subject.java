package com.xpto.legion.models;

import java.util.Date;

import org.json.JSONObject;

import com.xpto.legion.utils.Util;

public class Subject extends Default {
	private long placeId;

	public long getPlaceId() {
		return placeId;
	}

	public void setPlaceId(long _placeId) {
		if (_placeId >= 0)
			placeId = _placeId;
	}

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

	private Date when;

	public Date getWhen() {
		return when;
	}

	public void setWhen(Date _when) {
		if (_when != null)
			when = _when;
	}

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String _content) {
		if (_content != null)
			content = _content;
	}

	private long points;

	public long getPoints() {
		return points;
	}

	public void setPoints(long _points) {
		points = _points;
	}

	private long comments;

	public long getComments() {
		return comments;
	}

	public void setComments(long _comments) {
		if (_comments >= 0)
			comments = _comments;
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

			if (hasValue(_json, "placeId"))
				setPlaceId(_json.getLong("placeId"));

			if (hasValue(_json, "userId"))
				setUserId(_json.getLong("userId"));

			if (hasValue(_json, "userName"))
				setUserName(_json.getString("userName"));

			if (hasValue(_json, "date"))
				setWhen(Util.parseJSONDate(_json.getString("date")));

			if (hasValue(_json, "content"))
				setContent(_json.getString("content"));

			if (hasValue(_json, "points"))
				setPoints(_json.getLong("points"));

			if (hasValue(_json, "comments"))
				setComments(_json.getLong("comments"));

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
