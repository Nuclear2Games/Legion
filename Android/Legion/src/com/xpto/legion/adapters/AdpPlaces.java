package com.xpto.legion.adapters;

import java.util.ArrayList;

import android.view.View.OnClickListener;

import com.xpto.legion.models.Default;
import com.xpto.legion.utils.LActivity;

public class AdpPlaces extends AdpAllTypes {
	public AdpPlaces(LActivity _lActivity, OnClickListener _onClickRowLike, OnClickListener _onClickRowDislike, boolean _useHead) {
		super(_lActivity, null, null, _onClickRowLike, _onClickRowDislike, _useHead);
	}

	@Override
	protected ArrayList<Default> createItems() {
		if (getLActivity() == null || getLActivity().getGlobal() == null || getLActivity().getGlobal().getNearPlaces() == null)
			return new ArrayList<Default>();
		else {
			ArrayList<Default> items = new ArrayList<Default>();
			items.addAll(getLActivity().getGlobal().getNearPlaces());
			return items;
		}
	}
}