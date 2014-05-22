package com.xpto.legion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xpto.legion.utils.MFFragment;

public class FrgEvents extends MFFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frg_list, null);
		return view;
	}

	@Override
	public boolean canBack() {
		return true;
	}
}
