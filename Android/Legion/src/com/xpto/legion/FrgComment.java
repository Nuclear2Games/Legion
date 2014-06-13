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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xpto.legion.adapters.AdpAllTypes;
import com.xpto.legion.data.Caller;
import com.xpto.legion.models.Answer;
import com.xpto.legion.models.Comment;
import com.xpto.legion.utils.LActivity;
import com.xpto.legion.utils.LCallback;
import com.xpto.legion.utils.LDialog;
import com.xpto.legion.utils.LEditText;
import com.xpto.legion.utils.LFragment;
import com.xpto.legion.utils.Like;
import com.xpto.legion.utils.Util;

public class FrgComment extends LFragment {
	private Comment comment;
	private long lastListUpdate;

	private AdpAllTypes adpAnswers;
	private ListView lst;
	private int marginBottom;

	private LEditText txt;
	private Button btn;

	private View layRefresh;

	private Answer sendingAnswer;

	private TextView txtEmpty;

	private View viwHelp;

	@Override
	public View createView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.frg_list, null);

		Util.loadFonts(view);

		lst = (ListView) view.findViewById(R.id.lst);
		adpAnswers = new AdpAllTypes((LActivity) getActivity(), null, null, onClickCommentLike, onClickCommentDislike, onClickAnswerLike, onClickAnswerDislike,
				true);
		adpAnswers.addItem(comment);
		lst.setAdapter(adpAnswers);

		marginBottom = ((FrameLayout.LayoutParams) lst.getLayoutParams()).bottomMargin;

		txt = (LEditText) view.findViewById(R.id.txt);
		txt.setOnFocusChange(onFocusChange);
		txt.setHint(R.string.f_comment_hint);

		int maxLength = 255;
		InputFilter[] fArray = new InputFilter[1];
		fArray[0] = new InputFilter.LengthFilter(maxLength);
		txt.setFilters(fArray);

		btn = (Button) view.findViewById(R.id.btn);
		btn.setOnClickListener(onClickSend);

		layRefresh = view.findViewById(R.id.layRefresh);
		layRefresh.setOnClickListener(onClickRefresh);

		txtEmpty = (TextView) view.findViewById(R.id.txtEmpty);
		txtEmpty.setText(R.string.f_comment_empty);

		Help.fillHelpComment(viwHelp = view.findViewById(R.id.layHelp));

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
		((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_COMMENT);
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
		if (comment != null && getActivity() != null && System.currentTimeMillis() - lastListUpdate > 5000) {
			lastListUpdate = System.currentTimeMillis();

			if (getActivity() != null)
				Caller.getAnswers(getActivity(), getAnswersSuccess, null, getAnswersFail, comment.getId());

			if (comment.getContent() == null || comment.getContent().length() == 0)
				Caller.getSubject(getActivity(), getCommentSuccess, null, null, comment.getId());
		}
	}

	private LCallback getAnswersSuccess = new LCallback() {
		@Override
		public void finished(Object _value) {
			try {
				if (_value == null || !(_value instanceof JSONObject))
					return;

				JSONObject json = (JSONObject) _value;
				if (json.getLong("Code") == 1) {
					// Convert json to objects
					ArrayList<Answer> answers = new ArrayList<Answer>();
					JSONArray jarray = json.getJSONArray("Content");
					for (int i = 0; i < jarray.length(); i++) {
						Answer answer = new Answer();
						if (answer.loadFromJSon(jarray.getJSONObject(i)))
							answers.add(answer);
					}

					// Add objects to adapter
					for (int i = 0; i < answers.size(); i++)
						adpAnswers.addItem(answers.get(i));
					adpAnswers.notifyDataSetChanged();

					if (adpAnswers.getCount() <= 1)
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

	private LCallback getAnswersFail = new LCallback() {
		@Override
		public void finished(Object _value) {
			Animation cameIn = AnimationUtils.loadAnimation(getActivity(), R.anim.transition_down_in);
			layRefresh.setVisibility(View.VISIBLE);
			layRefresh.startAnimation(cameIn);
		}
	};

	private LCallback getCommentSuccess = new LCallback() {
		@Override
		public void finished(Object _value) {
			try {
				if (_value == null || !(_value instanceof JSONObject))
					return;

				JSONObject json = (JSONObject) _value;
				if (json.getLong("Code") == 1) {
					comment.loadFromJSon(json.getJSONObject("Content"));
					adpAnswers.notifyDataSetChanged();
				} else
					throw new Exception();
			} catch (Exception e) {
			}
		}
	};

	public void setComment(Comment _comment) {
		comment = _comment;

		if (adpAnswers != null && adpAnswers.getCount() == 0)
			adpAnswers.addItem(comment);

		updateList();
	}

	private OnClickListener onClickCommentLike = new OnClickListener() {
		@Override
		public void onClick(View _view) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else
				Like.like(getActivity(), _view, getGlobal().getLogged().getId(), ActMain.CUSTOM_TYPE_COMMENT, true);
		}
	};

	private OnClickListener onClickCommentDislike = new OnClickListener() {
		@Override
		public void onClick(View _view) {
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				FrgNoUser frgNoUser = new FrgNoUser();
				((ActMain) getActivity()).setFragment(frgNoUser, ActMain.LEVEL_TOP);
			} else
				Like.like(getActivity(), _view, getGlobal().getLogged().getId(), ActMain.CUSTOM_TYPE_COMMENT, false);
		}
	};

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
					LDialog.openDialog((LActivity) getActivity(), R.string.f_comment_fill_name_title, R.string.f_comment_fill_name_subtitle, R.string.f_ok,
							false);
					return;
				}

				sendingAnswer = new Answer();
				sendingAnswer.setUserId(getGlobal().getLogged().getId());
				if (getGlobal().getLogged().getName() == null || getGlobal().getLogged().getName().trim().length() == 0)
					sendingAnswer.setUserName(getGlobal().getLogged().getLogin());
				else
					sendingAnswer.setUserName(getGlobal().getLogged().getName());
				sendingAnswer.setCommentId(comment.getId());
				sendingAnswer.setContent(content);
				sendingAnswer.setWhen(new Date());
				adpAnswers.addItem(sendingAnswer, 1);

				answerRetry.finished(null);
				Caller.newAnswer(getActivity(), answerSuccess, answerRetry, answerFail, getGlobal().getLogged().getId(), comment.getId(), content);
			}
		}
	};

	private LCallback answerSuccess = new LCallback() {
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

					sendingAnswer.setId(json.getLong("Content"));
					sendingAnswer = null;

					updateList();
				} else if (json.getLong("Code") == 6) {
					if (getActivity() != null)
						LDialog.openDialog((LActivity) getActivity(), R.string.f_comment_rating_title, R.string.f_comment_rating_subtitle, R.string.f_ok, false);
				} else
					throw new Exception();
			} catch (Exception e) {
			}
		}
	};

	private LCallback answerRetry = new LCallback() {
		@Override
		public void finished(Object _value) {
			if (getActivity() != null)
				((LActivity) getActivity()).startLoading(R.string.f_sending);

			txt.setEnabled(false);
			btn.setEnabled(false);
			lst.requestFocus();
		}
	};

	private LCallback answerFail = new LCallback() {
		@Override
		public void finished(Object _value) {
			LDialog.openDialog((LActivity) getActivity(), R.string.f_no_connection, R.string.f_comment_fill_cant_send, R.string.f_ok, false);
		}
	};
}
