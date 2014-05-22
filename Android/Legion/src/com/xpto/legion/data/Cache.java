package com.xpto.legion.data;

import java.util.Date;

public class Cache {
	private String name;
	private String value;
	private long when;

	public Cache(String _name, String _value, long _when) {
		setName(_name);
		setValue(_value);
		setWhen(_when);
	}

	public Cache(String _name, String _value) {
		this(_name, _value, new Date().getTime());
	}

	public String getName() {
		return this.name;
	}

	public void setName(String _name) {
		this.name = _name;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String _value) {
		this.value = _value;
	}

	public long getWhen() {
		return this.when;
	}

	public void setWhen(long _when) {
		if (_when >= 0)
			this.when = _when;
	}
}
