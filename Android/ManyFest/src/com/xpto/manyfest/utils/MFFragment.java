package com.xpto.manyfest.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;

public abstract class MFFragment extends Fragment {
	private Global global;

	public Global getGlobal() {
		return this.global;
	}

	public void setGlobal(Activity activity) {
		if (activity != null && activity instanceof MFActivity)
			this.global = ((MFActivity) activity).getGlobal();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		setGlobal(activity);
	}

	public abstract boolean canBack();
}
