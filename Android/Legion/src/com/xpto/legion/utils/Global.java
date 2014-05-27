package com.xpto.legion.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Application;

import com.xpto.legion.data.Cache;
import com.xpto.legion.data.DB;
import com.xpto.legion.models.Notification;
import com.xpto.legion.models.Place;
import com.xpto.legion.models.User;

public class Global extends Application {
	private static final String db_user = "user";
	private static final String db_time = "time";
	private static final String db_accuracy = "accuracy";
	private static final String db_latitude = "latitude";
	private static final String db_longitude = "longitude";

	private User logged;

	public User getLogged() {
		if (logged == null || logged.getId() == 0) {
			Cache cache = DB.get(db_user);
			if (cache != null && cache.getValue().length() > 0) {
				try {
					logged = new User();
					if (!logged.loadFromJSon(new JSONObject(cache.getValue())))
						throw new Exception();
				} catch (Exception e) {
					DB.del(db_user);
				}
			}
		}

		return logged;
	}

	public void setLogged(User _logged) {
		if (logged == null || logged.getId() == 0)
			logged = _logged;
		else if (logged.getId() == _logged.getId()) {
			logged.setLogin(_logged.getLogin());
			logged.setName(_logged.getName());
			logged.setDescription(_logged.getDescription());
			logged.setPoints(_logged.getPoints());
		} else
			return;

		DB.set(db_user, logged.toString());
	}

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
		else {
			for (int i = 0; i < nearPlaces.size(); i++)
				for (int j = i + 1; j < nearPlaces.size(); j++)
					if (nearPlaces.get(i).getId() == nearPlaces.get(j).getId()) {
						nearPlaces.remove(j);
						j--;
					}
		}
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

			for (int i = 0; i < places.length; i++)
				addPlace(places[i]);

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
				nearPlaces.set(index, _place);
				index = -1;
				break;
			} else if (place.getWhen() == null || _place.getWhen() == null || place.getWhen().getTime() > _place.getWhen().getTime())
				index = i;
		}

		if (index >= 0) {
			nearPlaces.add(index, _place);
			return true;
		} else
			return false;
	}

	private ArrayList<Notification> notifications;

	public ArrayList<Notification> getNotifications() {
		if (notifications == null)
			notifications = new ArrayList<Notification>();
		else {
			for (int i = 0; i < notifications.size(); i++)
				for (int j = i + 1; j < notifications.size(); j++)
					if (notifications.get(i).getId() == notifications.get(j).getId()) {
						notifications.remove(j);
						j--;
					}
		}
		return notifications;
	}

	public boolean addNotifications(JSONArray _notifications) {
		try {
			Notification[] notifications = new Notification[_notifications.length()];
			for (int i = 0; i < notifications.length; i++) {
				notifications[i] = new Notification();
				if (!notifications[i].loadFromJSon(_notifications.getJSONObject(i)))
					throw new Exception();
			}

			if (this.notifications != null)
				this.notifications.clear();
			for (int i = 0; i < notifications.length; i++)
				addNotification(notifications[i]);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean addNotification(Notification _notification) {
		if (notifications == null) {
			notifications = new ArrayList<Notification>();
			notifications.add(_notification);
			return true;
		}

		int index = 0;
		for (int i = notifications.size() - 1; i >= 0; i--) {
			Notification notification = notifications.get(i);

			if (notification.getId() == _notification.getId()) {
				index = -1;
				break;
			} else if (notification.getWhen().getTime() > _notification.getWhen().getTime())
				index = i;
		}

		if (index >= 0) {
			notifications.add(index, _notification);
			return true;
		} else
			return false;
	}
}
