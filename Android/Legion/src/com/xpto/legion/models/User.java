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
	public String toString() {
		JSONObject json = new JSONObject();

		try {
			json.put("Id", getId());
			json.put("Login", getLogin());
			json.put("Name", getName());
			json.put("Description", getDescription());
			json.put("Points", getPoints());
		} catch (Exception e) {
		}

		return json.toString();
	}

	@Override
	public boolean loadFromJSon(JSONObject _json) {
		try {
			if (_json == null)
				return false;

			if (hasValue(_json, "Id"))
				setId(_json.getLong("Id"));

			if (hasValue(_json, "Login"))
				setLogin(_json.getString("Login"));

			if (hasValue(_json, "Name"))
				setName(_json.getString("Name"));

			if (hasValue(_json, "Description"))
				setDescription(_json.getString("Description"));

			if (hasValue(_json, "Points"))
				setPoints(_json.getLong("Points"));

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
