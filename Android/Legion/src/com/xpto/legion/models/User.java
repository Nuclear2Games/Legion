package com.xpto.legion.models;

import org.json.JSONObject;

public class User extends Default {
	private String login;

	public String getLogin() {
		return login;
	}

	public void setLogin(String _login) {
		if (_login != null)
			login = _login;
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

	private long points;

	public long getPoints() {
		return points;
	}

	public void setPoints(long _points) {
		points = _points;
	}

	@Override
	public boolean loadFromJSon(JSONObject _json) {
		try {
			if (_json == null)
				return false;

			if (hasValue(_json, "id"))
				setId(_json.getLong("id"));

			if (hasValue(_json, "login"))
				setLogin(_json.getString("login"));

			if (hasValue(_json, "name"))
				setName(_json.getString("name"));

			if (hasValue(_json, "description"))
				setDescription(_json.getString("description"));

			if (hasValue(_json, "points"))
				setPoints(_json.getLong("points"));

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
