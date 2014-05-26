package com.xpto.legion.utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

public abstract class LFragment extends Fragment {
	private Global global;

	public Global getGlobal() {
		return this.global;
	}

	public void setGlobal(Activity activity) {
		if (activity != null && activity instanceof LActivity)
			this.global = ((LActivity) activity).getGlobal();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = createView(inflater);

		if (view != null) {
			Animation cameIn = getInAnimation();
			if (cameIn != null)
				view.startAnimation(cameIn);
		}

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		setGlobal(activity);
	}

	public abstract View createView(LayoutInflater inflater);

	public abstract Animation getInAnimation();

	public abstract Animation getOutAnimation();

	public abstract boolean canBack();
	
	public abstract void showHelp();
}
