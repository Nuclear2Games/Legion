package com.xpto.legion;

import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;

import com.xpto.legion.adapters.AdpAllTypes;
import com.xpto.legion.data.Caller;
import com.xpto.legion.models.Answer;
import com.xpto.legion.utils.LActivity;
import com.xpto.legion.utils.LCallback;
import com.xpto.legion.utils.LEditText;
import com.xpto.legion.utils.LFragment;
import com.xpto.legion.utils.Like;
import com.xpto.legion.utils.Util;

public class FrgAnswer extends LFragment {
	private Answer answer;

	private AdpAllTypes adp;
	private ListView lst;

	private LEditText txt;
	private Button btn;

	private View viwHelp;

	@Override
	public View createView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.frg_list, null);

		Util.loadFonts(view);

		lst = (ListView) view.findViewById(R.id.lst);
		adp = new AdpAllTypes((LActivity) getActivity(), null, null, null, null, onClickAnswerLike, onClickAnswerDislike, false);
		adp.addItem(answer);
		lst.setAdapter(adp);

		txt = (LEditText) view.findViewById(R.id.txt);
		txt.setEnabled(false);

		btn = (Button) view.findViewById(R.id.btn);
		btn.setEnabled(false);

		((View) txt.getParent()).setVisibility(View.GONE);

		Help.fillHelpAnswer(viwHelp = view.findViewById(R.id.layHelp));

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
		((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_ANSWER);
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
		if (answer != null && getActivity() != null) {
			if (answer.getContent() == null || answer.getContent().length() == 0)
				Caller.getAnswer(getActivity(), getAnswerSuccess, null, null, answer.getId());
		}
	}

	private LCallback getAnswerSuccess = new LCallback() {
		@Override
		public void finished(Object _value) {
			try {
				if (_value == null || !(_value instanceof JSONObject))
					return;

				JSONObject json = (JSONObject) _value;
				if (json.getLong("Code") == 1) {
					answer.loadFromJSon(json.getJSONObject("Content"));
					adp.notifyDataSetChanged();
				} else
					throw new Exception();
			} catch (Exception e) {
			}
		}
	};

	public void setAnswer(Answer _answer) {
		answer = _answer;

		if (adp != null && adp.getCount() == 0)
			adp.addItem(answer);

		updateList();
	}

	private OnClickListener onClickAnswerLike = new OnClickListener() {
		@Override
		public void onClick(View _view) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else
				Like.like(getActivity(), _view, getGlobal().getLogged().getId(), ActMain.CUSTOM_TYPE_ANSWER, true);
		}
	};

	private OnClickListener onClickAnswerDislike = new OnClickListener() {
		@Override
		public void onClick(View _view) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else
				Like.like(getActivity(), _view, getGlobal().getLogged().getId(), ActMain.CUSTOM_TYPE_ANSWER, false);
		}
	};
}
