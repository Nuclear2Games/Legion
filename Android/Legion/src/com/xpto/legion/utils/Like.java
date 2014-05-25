package com.xpto.legion.utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.xpto.legion.R;
import com.xpto.legion.data.Caller;
import com.xpto.legion.data.DB;

public class Like {
	public static void like(Activity _activity, View _view, long _user, int _customTypeId, final boolean _isLike) {
		try {
			final long id = Long.parseLong(_view.getTag().toString());
			final String db_id = "like_" + _customTypeId + "_" + id;
			final TextView txtLikes = (TextView) _view.findViewById(R.id.txtLikes);
			final TextView txtDislikes = (TextView) _view.findViewById(R.id.txtDislikes);

			LCallback likeFail = new LCallback() {
				@Override
				public void finished(Object _value) {
					DB.del(db_id);

					try {
						if (txtLikes != null) {
							long likes = Long.parseLong(txtLikes.getText().toString()) - 1;
							txtLikes.setText("" + likes);
						} else if (txtDislikes != null) {
							long dislikes = Long.parseLong(txtDislikes.getText().toString()) - 1;
							txtDislikes.setText("" + dislikes);
						}
					} catch (Exception e) {
					}
				}
			};

			if (DB.get(db_id) == null) {
				DB.set(db_id, "1");

				try {
					if (txtLikes != null) {
						long likes = Long.parseLong(txtLikes.getText().toString()) + 1;
						txtLikes.setText("" + likes);
					} else if (txtDislikes != null) {
						long dislikes = Long.parseLong(txtDislikes.getText().toString()) + 1;
						txtDislikes.setText("" + dislikes);
					}
				} catch (Exception e) {
				}

				Caller.newLike(_activity, null, null, likeFail, _user, id, _customTypeId, _isLike);
			}
		} catch (Exception e) {
		}
	}
}
