package com.xpto.legion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xpto.legion.adapters.AdpNotifications;
import com.xpto.legion.models.Answer;
import com.xpto.legion.models.Comment;
import com.xpto.legion.models.Notification;
import com.xpto.legion.models.Place;
import com.xpto.legion.models.Subject;
import com.xpto.legion.utils.LActivity;
import com.xpto.legion.utils.LFragment;

public class FrgNotifications extends LFragment {
	private AdpNotifications adpAnswers;
	private ListView lst;

	@Override
	public View createView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.frg_notifications, null);

		lst = (ListView) view.findViewById(R.id.lst);
		adpAnswers = new AdpNotifications((LActivity) getActivity(), false);
		lst.setAdapter(adpAnswers);
		lst.setOnItemClickListener(onItemClick);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		adpAnswers.clear();
		adpAnswers.notifyDataSetChanged();
	}

	@Override
	public Animation getInAnimation() {
		return AnimationUtils.loadAnimation(getActivity(), R.anim.transition_up_in);
	}

	@Override
	public Animation getOutAnimation() {
		return AnimationUtils.loadAnimation(getActivity(), R.anim.transition_down_out);
	}

	@Override
	public boolean canBack() {
		((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_TOP);
		return false;
	}

	private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> _parent, View _view, int _position, long _id) {
			Notification notification = (Notification) adpAnswers.getItem(_position);

			switch (notification.getCustomTypeId()) {
			case ActMain.CUSTOM_TYPE_EVENT:
				Place place = new Place();
				place.setId(notification.getCustomId());

				FrgEvent frgEvent = new FrgEvent();
				frgEvent.setEvent(place);
				((ActMain) getActivity()).setFragment(frgEvent, ActMain.LEVEL_EVENT);
				((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_SUBJECT);
				((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_COMMENT);
				((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_ANSWER);
				
				canBack();
				break;

			case ActMain.CUSTOM_TYPE_SUBJECT:
				Subject subject = new Subject();
				subject.setId(notification.getCustomId());

				FrgSubject frgSubject = new FrgSubject();
				frgSubject.setSubject(subject);
				((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_EVENT);
				((ActMain) getActivity()).setFragment(frgSubject, ActMain.LEVEL_SUBJECT);
				((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_COMMENT);
				((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_ANSWER);
				
				canBack();
				break;

			case ActMain.CUSTOM_TYPE_COMMENT:
				Comment comment = new Comment();
				comment.setId(notification.getCustomId());

				FrgComment frgComment = new FrgComment();
				frgComment.setComment(comment);
				((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_EVENT);
				((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_SUBJECT);
				((ActMain) getActivity()).setFragment(frgComment, ActMain.LEVEL_COMMENT);
				((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_ANSWER);
				
				canBack();
				break;

			case ActMain.CUSTOM_TYPE_ANSWER:
				Answer answer = new Answer();
				answer.setId(notification.getCustomId());

				// FrgAnswer frgAnswer = new FrgAnswer();
				// frgAnswer.setAnswer(answer);
				// ((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_EVENT);
				// ((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_SUBJECT);
				// ((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_COMMENT);
				// ((ActMain) getActivity()).setFragment(frgAnswer, ActMain.LEVEL_ANSWER);
				
				canBack();
				break;
			}
		}
	};
}
