package com.xpto.legion.utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.xpto.legion.R;
import com.xpto.legion.data.Caller;
import com.xpto.legion.data.DB;

public class Checkin {
	public static void checkin(Activity _activity, View _view, long _user) {
		try {
			final long id = Long.parseLong(_view.getTag().toString());
			final String db_id = "checkin_" + id;
			final TextView txtCheckins = (TextView) _view.findViewById(R.id.txtCheckins);

			LCallback checkinFail = new LCallback() {
				@Override
				public void finished(Object _value) {
					DB.del(db_id);

					try {
						if (txtCheckins != null) {
							long checkins = Long.parseLong(txtCheckins.getText().toString()) - 1;
							txtCheckins.setText("" + checkins);
						}
					} catch (Exception e) {
					}
				}
			};

			if (DB.get(db_id) == null) {
				DB.set(db_id, "1");

				try {
					if (txtCheckins != null) {
						long checkins = Long.parseLong(txtCheckins.getText().toString()) + 1;
						txtCheckins.setText("" + checkins);
					}
				} catch (Exception e) {
				}

				Caller.newCheckin(_activity, null, null, checkinFail, _user, id);
			}
		} catch (Exception e) {
		}
	}
}
