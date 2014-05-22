package com.xpto.legion.utils;

import java.util.ArrayList;

import org.json.JSONArray;

import com.xpto.legion.data.Cache;
import com.xpto.legion.data.DB;
import com.xpto.legion.models.Place;

import android.app.Application;

public class Global extends Application {
	private static final String db_time = "time";
	private static final String db_accuracy = "accuracy";
	private static final String db_latitude = "latitude";
	private static final String db_longitude = "longitude";

	private long time;

	public double getTime() {
		if (time == 0) {
			Cache cache = DB.get(db_time);
			if (cache != null && cache.getValue().length() > 0) {
				try {
					time = Long.parseLong(cache.getValue());
				} catch (Exception e) {
					DB.del(db_time);
				}
			}
		}

		return time;
	}

	public void setTime(long _time) {
		time = _time;

		DB.set(db_time, time + "");
	}

	private double accuracy;

	public double getAccuracy() {
		if (accuracy == 0) {
			Cache cache = DB.get(db_accuracy);
			if (cache != null && cache.getValue().length() > 0) {
				try {
					accuracy = Double.parseDouble(cache.getValue());
				} catch (Exception e) {
					DB.del(db_accuracy);
				}
			}
		}

		return accuracy;
	}

	public void setAccuracy(double _accuracy) {
		accuracy = _accuracy;

		DB.set(db_accuracy, accuracy + "");
	}

	private double latitude;

	public double getLatitude() {
		if (latitude == 0) {
			Cache cache = DB.get(db_latitude);
			if (cache != null && cache.getValue().length() > 0) {
				try {
					latitude = Double.parseDouble(cache.getValue());
				} catch (Exception e) {
					DB.del(db_latitude);
				}
			}
		}

		return latitude;
	}

	public void setLatitude(double _latitude) {
		latitude = _latitude;

		DB.set(db_latitude, latitude + "");
	}

	private double longitude;

	public double getLongitude() {
		if (longitude == 0) {
			Cache cache = DB.get(db_longitude);
			if (cache != null && cache.getValue().length() > 0) {
				try {
					longitude = Double.parseDouble(cache.getValue());
				} catch (Exception e) {
					DB.del(db_longitude);
				}
			}
		}

		return longitude;
	}

	public void setLongitude(double _longitude) {
		longitude = _longitude;

		DB.set(db_longitude, longitude + "");
	}

	private ArrayList<Place> nearPlaces;

	public ArrayList<Place> getNearPlaces() {
		if (nearPlaces == null)
			nearPlaces = new ArrayList<Place>();
		return nearPlaces;
	}

	public boolean addPlaces(JSONArray _places) {
		try {
			Place[] places = new Place[_places.length()];
			for (int i = 0; i < places.length; i++) {
				places[i] = new Place();
				if (!places[i].loadFromJSon(_places.getJSONObject(i)))
					throw new Exception();
			}

			for (int i = 0; i < places.length; i++) {
				addPlace(places[i]);
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean addPlace(Place _place) {
		if (nearPlaces == null) {
			nearPlaces = new ArrayList<Place>();
			nearPlaces.add(_place);
			return true;
		}

		int index = 0;
		for (int i = nearPlaces.size() - 1; i >= 0; i--) {
			Place place = nearPlaces.get(i);

			if (place.getId() == _place.getId()) {
				index = -1;
				break;
			} else if (place.getWhen().getTime() > _place.getWhen().getTime())
				index = i;
		}

		if (index >= 0) {
			nearPlaces.add(index, _place);
			return true;
		} else
			return false;
	}
}
