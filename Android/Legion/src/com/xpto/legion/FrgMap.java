package com.xpto.legion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.xpto.legion.utils.LFragment;

public class FrgMap extends LFragment {
	// Map
	private final float mapZoom = 16;
	private GoogleMap map;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frg_map, null);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (map == null) {
			((ActMain) getActivity()).startTracking();

			SupportMapFragment mapFrag = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.layMapHole);

			if (mapFrag != null) {
				map = mapFrag.getMap();
				if (map != null)
					map.getUiSettings().setZoomControlsEnabled(false);
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
}
