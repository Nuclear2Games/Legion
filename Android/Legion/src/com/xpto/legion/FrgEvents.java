package com.xpto.legion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xpto.legion.adapters.AdpPlaces;
import com.xpto.legion.models.Place;
import com.xpto.legion.utils.LActivity;
import com.xpto.legion.utils.LFragment;
import com.xpto.legion.utils.Like;
import com.xpto.legion.utils.Util;

public class FrgEvents extends LFragment {
	private AdpPlaces adpPlaces;
	private ListView lst;

	private TextView txtEmpty;

	private View viwHelp;

	@Override
	public View createView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.frg_events, null);

		Util.loadFonts(view);

		lst = (ListView) view.findViewById(R.id.lst);
		adpPlaces = new AdpPlaces((LActivity) getActivity(), onClickLike, onClickDislike, false);
		lst.setAdapter(adpPlaces);
		lst.setOnItemClickListener(onItemClick);

		txtEmpty = (TextView) view.findViewById(R.id.txtEmpty);
		txtEmpty.setText(R.string.f_events_empty);

		Help.fillHelpEvents(viwHelp = view.findViewById(R.id.layHelp));

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
	public boolean canBack() {
		return true;
	}

	@Override
	public void showHelp() {
		Animation cameIn = AnimationUtils.loadAnimation(getActivity(), R.anim.transition_dialog_in);
		viwHelp.setVisibility(View.VISIBLE);
		viwHelp.startAnimation(cameIn);
	}

	public void updatePlaces() {
		adpPlaces.clear();
		adpPlaces.notifyDataSetChanged();

		if (adpPlaces.getCount() == 1)
			txtEmpty.setVisibility(View.VISIBLE);
		else
			txtEmpty.setVisibility(View.GONE);
	}

	private OnClickListener onClickLike = new OnClickListener() {
		@Override
		public void onClick(View _view) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else
				Like.like(getActivity(), _view, getGlobal().getLogged().getId(), ActMain.CUSTOM_TYPE_EVENT, true);
		}
	};

	private OnClickListener onClickDislike = new OnClickListener() {
		@Override
		public void onClick(View _view) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else
				Like.like(getActivity(), _view, getGlobal().getLogged().getId(), ActMain.CUSTOM_TYPE_EVENT, false);
		}
	};

	private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> _parent, View _view, int _position, long _id) {
			FrgEvent frgEvent = new FrgEvent();
			frgEvent.setEvent((Place) adpPlaces.getItem(_position));
			((ActMain) getActivity()).setFragment(frgEvent, ActMain.LEVEL_EVENT);
		}
	};
}
