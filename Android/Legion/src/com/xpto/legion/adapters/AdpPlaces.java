package com.xpto.legion.adapters;

import java.util.ArrayList;

import android.view.View;
import android.widget.TextView;

import com.xpto.legion.R;
import com.xpto.legion.models.Place;
import com.xpto.legion.utils.LActivity;

public class AdpPlaces extends AdpDefault<Place> {
	private LActivity activity;

	public AdpPlaces(LActivity lActivity) {
		super(lActivity);
		activity = lActivity;
	}

	@Override
	protected ArrayList<Place> createItems() {
		if (activity == null || activity.getGlobal() == null || activity.getGlobal().getNearPlaces() == null)
			return new ArrayList<Place>();
		else
			return activity.getGlobal().getNearPlaces();
	}

	@Override
	public View getView(int _position, View _convertView) {
		View view;
		if (_convertView == null)
			view = getInflater().inflate(R.layout.event_row, null);
		else
			view = _convertView;

		Place place = getItem(_position);

		TextView txtName = (TextView) view.findViewById(R.id.txtName);
		txtName.setText(place.getName());

		TextView txtDescription = (TextView) view.findViewById(R.id.txtDescription);
		txtDescription.setText(place.getDescription());

		return view;
	}
}
