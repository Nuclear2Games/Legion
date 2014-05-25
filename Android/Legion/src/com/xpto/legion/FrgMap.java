package com.xpto.legion;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xpto.legion.models.Place;
import com.xpto.legion.utils.LFragment;
import com.xpto.legion.utils.Util;

public class FrgMap extends LFragment {
	// Map
	private final float mapZoom = 16;
	private GoogleMap map;
	private ArrayList<Long> ids;
	private ArrayList<Marker> markers;

	private View btnCenter;

	@Override
	public View createView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.frg_map, null);

		btnCenter = view.findViewById(R.id.btnCenter);
		btnCenter.setOnClickListener(onClickCenter);

		return view;
	}

	@Override
	public Animation getInAnimation() {
		return null;
	}

	@Override
	public Animation getOutAnimation() {
		return null;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (map == null) {
			((ActMain) getActivity()).startTracking();

			SupportMapFragment mapFrag = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.layMapHole);

			if (mapFrag != null) {
				map = mapFrag.getMap();
				if (map != null) {
					map.getUiSettings().setZoomControlsEnabled(false);
					map.setOnMapLongClickListener(onMapLongClick);
				}
			}
		}
	}

	@Override
	public boolean canBack() {
		return true;
	}

	public void centerMap(double _latitude, double _logitude) {
		if (map != null) {
			// Center map
			LatLng ll = new LatLng(_latitude, _logitude);
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, mapZoom));
		}
	}

	private GoogleMap.OnMapLongClickListener onMapLongClick = new GoogleMap.OnMapLongClickListener() {
		@Override
		public void onMapLongClick(LatLng _point) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else {
				FrgNewEvent frgNewEvent = new FrgNewEvent();
				frgNewEvent.setLocation(_point.latitude, _point.longitude);
				((ActMain) getActivity()).setFragment(frgNewEvent, ActMain.LEVEL_TOP);
			}
		}
	};

	public void updatePlaces() {
		if (ids == null) {
			ids = new ArrayList<Long>();
			markers = new ArrayList<Marker>();
		}

		ArrayList<Place> np = getGlobal().getNearPlaces();
		for (int i = 0; i < np.size(); i++)
			addPlace(np.get(i));
	}

	public void addPlace(Place _place) {
		if (_place == null)
			return;

		if (ids.contains(_place.getId()))
			return;

		MarkerOptions mo = new MarkerOptions();
		mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_place));
		mo.position(new LatLng(_place.getLatitude(), _place.getLongitude()));

		mo.title(_place.getName());
		if (_place.getWhen() != null)
			mo.snippet(Util.formatToLongDateTime(_place.getWhen()));

		ids.add(_place.getId());
		markers.add(map.addMarker(mo));
	}

	private View.OnClickListener onClickCenter = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (getActivity() != null)
				((ActMain) getActivity()).startTracking();
		}
	};
}
