package com.xpto.legion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xpto.legion.adapters.AdpPlaces;
import com.xpto.legion.utils.LActivity;
import com.xpto.legion.utils.LFragment;

public class FrgEvents extends LFragment {
	private AdpPlaces adpPlaces;
	private ListView lst;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frg_list, null);

		lst = (ListView) view.findViewById(R.id.lst);
		adpPlaces = new AdpPlaces((LActivity) getActivity());
		lst.setAdapter(adpPlaces);

		return view;
	}

	@Override
	public boolean canBack() {
		return true;
	}

	public void updatePlaces() {
		adpPlaces.clear();
		adpPlaces.notifyDataSetChanged();
	}
}
