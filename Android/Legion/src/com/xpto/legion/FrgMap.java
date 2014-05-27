package com.xpto.legion;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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
	private final float mapZoom = 14;
	private GoogleMap map;
	private ArrayList<Long> ids;
	private ArrayList<Marker> markers;

	private View btnCenter;

	private View viwHelp;

	@Override
	public View createView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.frg_map, null);

		Util.loadFonts(view);

		btnCenter = view.findViewById(R.id.btnCenter);
		btnCenter.setOnClickListener(onClickCenter);

		Help.fillHelpMap(viwHelp = view.findViewById(R.id.layHelp));

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
					map.setOnInfoWindowClickListener(onMapInfoClick);
				}
			}
		}
	}

	@Override
	public boolean canBack() {
		return true;
	}

	@Override
	public void showHelp() {
		Animation cameIn = AnimationUtils.loadAnimation(getActivity(), R.anim.transition_dialog_in);
		viwHelp.setVisibility(View.VISIBLE);
		viwHelp.startAnimation(cameIn);
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

	private GoogleMap.OnInfoWindowClickListener onMapInfoClick = new GoogleMap.OnInfoWindowClickListener() {
		@Override
		public void onInfoWindowClick(Marker marker) {
			int index = markers.indexOf(marker);

			Place place = new Place();
			place.setId(ids.get(index));

			FrgEvent frgEvent = new FrgEvent();
			frgEvent.setEvent(place);
			((ActMain) getActivity()).setFragment(frgEvent, ActMain.LEVEL_EVENT);
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
		mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_red));
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
