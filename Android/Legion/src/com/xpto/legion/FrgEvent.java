package com.xpto.legion;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xpto.legion.adapters.AdpAllTypes;
import com.xpto.legion.data.Caller;
import com.xpto.legion.models.Place;
import com.xpto.legion.models.Subject;
import com.xpto.legion.utils.Checkin;
import com.xpto.legion.utils.LActivity;
import com.xpto.legion.utils.LCallback;
import com.xpto.legion.utils.LDialog;
import com.xpto.legion.utils.LEditText;
import com.xpto.legion.utils.LFragment;
import com.xpto.legion.utils.Like;
import com.xpto.legion.utils.Util;

public class FrgEvent extends LFragment {
	private Place place;
	private long lastListUpdate;

	private AdpAllTypes adpSubjects;
	private ListView lst;
	private int marginBottom;

	private LEditText txt;
	private Button btn;

	private View layRefresh;

	private Subject sendingSubject;

	private TextView txtEmpty;

	private View viwHelp;

	@Override
	public View createView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.frg_list, null);

		Util.loadFonts(view);

		lst = (ListView) view.findViewById(R.id.lst);
		adpSubjects = new AdpAllTypes((LActivity) getActivity(), onClickEventEdit, onClickEventCheckin, onClickEventLike, onClickEventDislike,
				onClickSubjectLike, onClickSubjectDislike, true);
		adpSubjects.addItem(place);
		lst.setAdapter(adpSubjects);
		lst.setOnItemClickListener(onItemClick);

		marginBottom = ((FrameLayout.LayoutParams) lst.getLayoutParams()).bottomMargin;

		txt = (LEditText) view.findViewById(R.id.txt);
		txt.setOnFocusChange(onFocusChange);
		txt.setHint(R.string.f_event_hint);

		int maxLength = 255;
		InputFilter[] fArray = new InputFilter[1];
		fArray[0] = new InputFilter.LengthFilter(maxLength);
		txt.setFilters(fArray);

		btn = (Button) view.findViewById(R.id.btn);
		btn.setOnClickListener(onClickSend);

		layRefresh = view.findViewById(R.id.layRefresh);
		layRefresh.setOnClickListener(onClickRefresh);

		txtEmpty = (TextView) view.findViewById(R.id.txtEmpty);
		txtEmpty.setText(R.string.f_event_empty);

		Help.fillHelpEvent(viwHelp = view.findViewById(R.id.layHelp));

		return view;
	}

	@Override
	public Animation getInAnimation() {
		return AnimationUtils.loadAnimation(getActivity(), R.anim.transition_down_in);
	}

	@Override
	public Animation getOutAnimation() {
		return AnimationUtils.loadAnimation(getActivity(), R.anim.transition_up_out);
	}

	@Override
	public boolean canBack() {
		((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_EVENT);
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();

		updateList();
	}

	@Override
	public void showHelp() {
		Animation cameIn = AnimationUtils.loadAnimation(getActivity(), R.anim.transition_dialog_in);
		viwHelp.setVisibility(View.VISIBLE);
		viwHelp.startAnimation(cameIn);
	}

	private void updateList() {
		if (place != null && getActivity() != null && System.currentTimeMillis() - lastListUpdate > 5000) {
			lastListUpdate = System.currentTimeMillis();

			if (getActivity() != null)
				Caller.getSubjects(getActivity(), getSubjectsSuccess, null, getSubjectsFail, place.getId());

			if (place.getName() == null || place.getName().length() == 0)
				Caller.getPlace(getActivity(), getPlaceSuccess, null, null, place.getId());
		}
	}

	private LCallback getSubjectsSuccess = new LCallback() {
		@Override
		public void finished(Object _value) {
			try {
				if (_value == null || !(_value instanceof JSONObject))
					return;

				JSONObject json = (JSONObject) _value;
				if (json.getLong("Code") == 1) {
					// Convert json to objects
					ArrayList<Subject> subjects = new ArrayList<Subject>();
					JSONArray jarray = json.getJSONArray("Content");
					for (int i = 0; i < jarray.length(); i++) {
						Subject subject = new Subject();
						if (subject.loadFromJSon(jarray.getJSONObject(i)))
							subjects.add(subject);
					}

					// Add objects to adapter
					for (int i = 0; i < subjects.size(); i++)
						adpSubjects.addItem(subjects.get(i));
					adpSubjects.notifyDataSetChanged();

					if (adpSubjects.getCount() == 0)
						txtEmpty.setVisibility(View.VISIBLE);
					else
						txtEmpty.setVisibility(View.GONE);

					// Update list after 15 sec.s
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							updateList();
						}
					}, 15000);
				} else
					throw new Exception();
			} catch (Exception e) {
			}
		}
	};

	private LCallback getSubjectsFail = new LCallback() {
		@Override
		public void finished(Object _value) {
			Animation cameIn = AnimationUtils.loadAnimation(getActivity(), R.anim.transition_down_in);
			layRefresh.setVisibility(View.VISIBLE);
			layRefresh.startAnimation(cameIn);
		}
	};

	private LCallback getPlaceSuccess = new LCallback() {
		@Override
		public void finished(Object _value) {
			try {
				if (_value == null || !(_value instanceof JSONObject))
					return;

				JSONObject json = (JSONObject) _value;
				if (json.getLong("Code") == 1) {
					place.loadFromJSon(json.getJSONObject("Content"));
					adpSubjects.notifyDataSetChanged();
				} else
					throw new Exception();
			} catch (Exception e) {
			}
		}
	};

	public void setEvent(Place _place) {
		place = _place;

		if (adpSubjects != null && adpSubjects.getCount() == 0)
			adpSubjects.addItem(place);

		updateList();
	}

	private OnClickListener onClickEventEdit = new OnClickListener() {
		@Override
		public void onClick(View _view) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else if (place.getUserId() != getGlobal().getLogged().getId()) {
				LDialog.openDialog((LActivity) getActivity(), R.string.f_event_cant_edit_title, R.string.f_event_cant_edit_subtitle, R.string.f_ok, false);
			} else {
				FrgUpdateEvent frgUpdateEvent = new FrgUpdateEvent();
				frgUpdateEvent.setPlace(place);
				((ActMain) getActivity()).setFragment(frgUpdateEvent, ActMain.LEVEL_TOP);
			}
		}
	};

	private OnClickListener onClickEventCheckin = new OnClickListener() {
		@Override
		public void onClick(View _view) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else
				Checkin.checkin(getActivity(), _view, getGlobal().getLogged().getId());
		}
	};

	private OnClickListener onClickEventLike = new OnClickListener() {
		@Override
		public void onClick(View _view) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else
				Like.like(getActivity(), _view, getGlobal().getLogged().getId(), ActMain.CUSTOM_TYPE_EVENT, true);
		}
	};

	private OnClickListener onClickEventDislike = new OnClickListener() {
		@Override
		public void onClick(View _view) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else
				Like.like(getActivity(), _view, getGlobal().getLogged().getId(), ActMain.CUSTOM_TYPE_EVENT, false);
		}
	};

	private OnClickListener onClickSubjectLike = new OnClickListener() {
		@Override
		public void onClick(View _view) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else
				Like.like(getActivity(), _view, getGlobal().getLogged().getId(), ActMain.CUSTOM_TYPE_SUBJECT, true);
		}
	};

	private OnClickListener onClickSubjectDislike = new OnClickListener() {
		@Override
		public void onClick(View _view) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else
				Like.like(getActivity(), _view, getGlobal().getLogged().getId(), ActMain.CUSTOM_TYPE_SUBJECT, false);
		}
	};

	private LEditText.OnFocusChange onFocusChange = new LEditText.OnFocusChange() {
		@Override
		public void onFocusChange(boolean _hasFocus) {
			if (_hasFocus) {
				FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) lst.getLayoutParams();
				lp.setMargins(0, 0, 0, marginBottom * 2);
				lst.setLayoutParams(lp);

				lp = (FrameLayout.LayoutParams) ((FrameLayout) txt.getParent()).getLayoutParams();
				lp.height = marginBottom * 2;
				((FrameLayout) txt.getParent()).setLayoutParams(lp);
			} else {
				FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) lst.getLayoutParams();
				lp.setMargins(0, 0, 0, marginBottom);
				lst.setLayoutParams(lp);

				lp = (FrameLayout.LayoutParams) ((FrameLayout) txt.getParent()).getLayoutParams();
				lp.height = marginBottom;
				((FrameLayout) txt.getParent()).setLayoutParams(lp);

				lst.requestFocus();
			}
		}
	};

	private OnClickListener onClickRefresh = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Animation cameOut = AnimationUtils.loadAnimation(getActivity(), R.anim.transition_up_out);
			layRefresh.startAnimation(cameOut);
			layRefresh.setVisibility(View.GONE);

			lastListUpdate = 0;
			updateList();
		}
	};

	private OnClickListener onClickSend = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (getGlobal().getLogged() == null) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else {
				String content = txt.getText().toString();
				if (content.length() < 4) {
					LDialog.openDialog((LActivity) getActivity(), R.string.f_event_fill_name_title, R.string.f_event_fill_name_subtitle, R.string.f_ok, false);
					return;
				}

				sendingSubject = new Subject();
				sendingSubject.setUserId(getGlobal().getLogged().getId());
				if (getGlobal().getLogged().getName() == null || getGlobal().getLogged().getName().trim().length() == 0)
					sendingSubject.setUserName(getGlobal().getLogged().getLogin());
				else
					sendingSubject.setUserName(getGlobal().getLogged().getName());
				sendingSubject.setPlaceId(place.getId());
				sendingSubject.setContent(content);
				sendingSubject.setWhen(new Date());
				adpSubjects.addItem(sendingSubject, 1);

				subjectRetry.finished(null);
				Caller.newSubject(getActivity(), subjectSuccess, subjectRetry, subjectFail, getGlobal().getLogged().getId(), place.getId(), content);
			}
		}
	};

	private LCallback subjectSuccess = new LCallback() {
		@Override
		public void finished(Object _value) {
			if (getActivity() != null)
				((LActivity) getActivity()).endLoading();

			txt.setEnabled(true);
			btn.setEnabled(true);

			try {
				if (_value == null || !(_value instanceof JSONObject))
					return;

				JSONObject json = (JSONObject) _value;
				if (json.getLong("Code") == 1) {
					txt.setText(null);

					sendingSubject.setId(json.getLong("Content"));
					sendingSubject = null;

					updateList();
				} else if (json.getLong("Code") == 6) {
					if (getActivity() != null)
						LDialog.openDialog((LActivity) getActivity(), R.string.f_event_rating_title, R.string.f_event_rating_subtitle, R.string.f_ok, false);
				} else
					throw new Exception();
			} catch (Exception e) {
			}
		}
	};

	private LCallback subjectRetry = new LCallback() {
		@Override
		public void finished(Object _value) {
			if (getActivity() != null)
				((LActivity) getActivity()).startLoading(R.string.f_sending);

			txt.setEnabled(false);
			btn.setEnabled(false);
			lst.requestFocus();
		}
	};

	private LCallback subjectFail = new LCallback() {
		@Override
		public void finished(Object _value) {
			LDialog.openDialog((LActivity) getActivity(), R.string.f_no_connection, R.string.f_event_fill_cant_send, R.string.f_ok, false);
		}
	};

	private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> _parent, View _view, int _position, long _id) {
			if (_position > 0) {
				FrgSubject frgSubject = new FrgSubject();
				frgSubject.setSubject((Subject) adpSubjects.getItem(_position));
				((ActMain) getActivity()).setFragment(frgSubject, ActMain.LEVEL_SUBJECT);
			}
		}
	};
}
