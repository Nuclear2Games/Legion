package com.xpto.legion.adapters;

import java.util.ArrayList;

import com.xpto.legion.models.Default;
import com.xpto.legion.utils.LActivity;

public class AdpNotifications extends AdpAllTypes {
	public AdpNotifications(LActivity _lActivity, boolean _useHead) {
		super(_lActivity, null, null, null, null, null, null, _useHead);
	}

	@Override
	protected ArrayList<Default> createItems() {
		if (getLActivity() == null || getLActivity().getGlobal() == null || getLActivity().getGlobal().getNearPlaces() == null)
			return new ArrayList<Default>();
		else {
			ArrayList<Default> items = new ArrayList<Default>();
			items.addAll(getLActivity().getGlobal().getNotifications());
			return items;
		}
	}
}