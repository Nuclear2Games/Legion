package com.xpto.legion.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xpto.legion.R;
import com.xpto.legion.models.Place;

public class AdpPlaces extends AdpDefault<Place> {
	public AdpPlaces(Context context) {
		super(context);
	}

	@Override
	public View getView(int _position, View _convertView) {
		View view;
		if (_convertView == null)
			view = getInflater().inflate(R.layout.event_row, null);
		else
			view = _convertView;

		Place place = getItem(_position);
		
		TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		txtTitle.setText(place.getName());

		TextView txtDescription = (TextView) view.findViewById(R.id.txtDescription);
		txtDescription.setText(place.getDescription());

		return view;
	}
}
