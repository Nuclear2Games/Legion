package com.xpto.legion.adapters;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xpto.legion.ActMain;
import com.xpto.legion.R;
import com.xpto.legion.models.Answer;
import com.xpto.legion.models.Comment;
import com.xpto.legion.models.Default;
import com.xpto.legion.models.Notification;
import com.xpto.legion.models.Place;
import com.xpto.legion.models.Subject;
import com.xpto.legion.utils.LActivity;
import com.xpto.legion.utils.Util;

public class AdpAllTypes extends AdpDefault<Default> {
	private LActivity activity;

	private OnClickListener onClickHeadEdit;
	private OnClickListener onClickHeadCheckin;

	private OnClickListener onClickHeadLike;
	private OnClickListener onClickHeadDislike;

	private OnClickListener onClickRowLike;
	private OnClickListener onClickRowDislike;

	private boolean useHead;

	protected LActivity getLActivity() {
		return activity;
	}

	public AdpAllTypes(LActivity _lActivity, OnClickListener _onClickHeadEdit, OnClickListener _onClickHeadCheckin, OnClickListener _onClickHeadLike,
			OnClickListener _onClickHeadDislike, OnClickListener _onClickRowLike, OnClickListener _onClickRowDislike, boolean _useHead) {
		super(_lActivity);
		activity = _lActivity;
		onClickHeadEdit = _onClickHeadEdit;
		onClickHeadCheckin = _onClickHeadCheckin;
		onClickHeadLike = _onClickHeadLike;
		onClickHeadDislike = _onClickHeadDislike;
		onClickRowLike = _onClickRowLike;
		onClickRowDislike = _onClickRowDislike;
		useHead = _useHead;
	}

	@Override
	public int getViewTypeCount() {
		return 5;
	}

	@Override
	public int getItemViewType(int _position) {
		Default item = getItem(_position);
		if (item instanceof Place)
			return 0;
		else if (item instanceof Subject)
			return 1;
		else if (item instanceof Comment)
			return 2;
		else if (item instanceof Answer)
			return 3;
		else if (item instanceof Notification)
			return 4;
		else
			return 0;
	}

	@Override
	public View getView(int _position, View _convertView) {
		View view;

		switch (getItemViewType(_position)) {
		default:
		case 0:
			if (_convertView == null) {
				if (useHead && _position == 0)
					view = getInflater().inflate(R.layout.event_head, null);
				else
					view = getInflater().inflate(R.layout.event_row, null);
				Util.loadFonts(view);
			} else
				view = _convertView;

			Place place = (Place) getItem(_position);

			fillEvent(_position, view, place);

			break;

		case 1:
			if (_convertView == null) {
				if (useHead && _position == 0)
					view = getInflater().inflate(R.layout.subject_head, null);
				else
					view = getInflater().inflate(R.layout.subject_row, null);
				Util.loadFonts(view);
			} else
				view = _convertView;

			Subject subject = (Subject) getItem(_position);

			fillSubject(_position, view, subject);

			break;

		case 2:
			if (_convertView == null) {
				if (useHead && _position == 0)
					view = getInflater().inflate(R.layout.comment_head, null);
				else
					view = getInflater().inflate(R.layout.comment_row, null);
				Util.loadFonts(view);
			} else
				view = _convertView;

			Comment comment = (Comment) getItem(_position);

			fillComment(_position, view, comment);

			break;

		case 3:
			if (_convertView == null) {
				view = getInflater().inflate(R.layout.answer_row, null);
				Util.loadFonts(view);
			} else
				view = _convertView;

			Answer answer = (Answer) getItem(_position);

			fillAnswer(_position, view, answer);

			break;

		case 4:
			if (_convertView == null) {
				view = getInflater().inflate(R.layout.notification_row, null);
				Util.loadFonts(view);
			} else
				view = _convertView;

			Notification notification = (Notification) getItem(_position);

			fillNotification(view, notification);

			break;
		}

		return view;
	}

	private void fillEvent(int _position, View view, Place place) {
		// Name
		TextView txtPlaceName = (TextView) view.findViewById(R.id.txtName);
		txtPlaceName.setText(place.getName());

		// Date
		TextView txtPlaceDate = (TextView) view.findViewById(R.id.txtDate);
		if (place.getWhen() != null)
			txtPlaceDate.setText(Util.formatToLongDateTime(place.getWhen()));
		else
			txtPlaceDate.setVisibility(View.GONE);

		// Description
		TextView txtPlaceDescription = (TextView) view.findViewById(R.id.txtDescription);
		txtPlaceDescription.setText(place.getDescription());

		// Owner
		TextView txtPlaceOwner = (TextView) view.findViewById(R.id.txtOwner);
		txtPlaceOwner.setText(view.getContext().getString(R.string.l_by) + " " + place.getUserName());

		// Edit
		View btnPlaceEdit = view.findViewById(R.id.btnEdit);
		if (useHead && _position == 0 && btnPlaceEdit != null) {
			btnPlaceEdit.setOnClickListener(onClickHeadEdit);
			btnPlaceEdit.setTag(place.getId());
		}

		// Checkin
		View btnPlaceCheckin = view.findViewById(R.id.btnCheckin);
		if (useHead && _position == 0 && btnPlaceCheckin != null) {
			btnPlaceCheckin.setOnClickListener(onClickHeadCheckin);
			btnPlaceCheckin.setTag(place.getId());
		}

		TextView txtPlaceCheckins = (TextView) view.findViewById(R.id.txtCheckins);
		if (txtPlaceCheckins != null)
			txtPlaceCheckins.setText("" + place.getCheckins());

		// Like
		View btnPlaceLike = view.findViewById(R.id.btnLike);
		if (useHead && _position == 0)
			btnPlaceLike.setOnClickListener(onClickHeadLike);
		else
			btnPlaceLike.setOnClickListener(onClickRowLike);
		btnPlaceLike.setTag(place.getId());

		TextView txtPlaceLikes = (TextView) view.findViewById(R.id.txtLikes);
		txtPlaceLikes.setText("" + place.getLikes());

		// Dislike
		View btnPlaceDislike = view.findViewById(R.id.btnDislike);
		if (useHead && _position == 0)
			btnPlaceDislike.setOnClickListener(onClickHeadDislike);
		else
			btnPlaceDislike.setOnClickListener(onClickRowDislike);
		btnPlaceDislike.setTag(place.getId());

		TextView txtPlaceDislikes = (TextView) view.findViewById(R.id.txtDislikes);
		txtPlaceDislikes.setText("" + place.getDislikes());

		// Subjects
		TextView txtPlaceSubjects = (TextView) view.findViewById(R.id.txtSubjects);
		if (txtPlaceSubjects != null) {
			if (place.getSubjects() == 1)
				txtPlaceSubjects.setText(place.getSubjects() + " " + getLActivity().getString(R.string.l_subject));
			else
				txtPlaceSubjects.setText(place.getSubjects() + " " + getLActivity().getString(R.string.l_subjects));
		}
	}

	private void fillSubject(int _position, View view, Subject subject) {
		// Description
		TextView txtSubjectDescription = (TextView) view.findViewById(R.id.txtDescription);
		txtSubjectDescription.setText(subject.getContent());

		// Date
		TextView txtSubjectDate = (TextView) view.findViewById(R.id.txtDate);
		if (subject.getWhen() != null)
			txtSubjectDate.setText(Util.formatToLongDateTime(subject.getWhen()));
		else
			txtSubjectDate.setVisibility(View.GONE);

		// Owner
		TextView txtSubjectOwner = (TextView) view.findViewById(R.id.txtOwner);
		txtSubjectOwner.setText(view.getContext().getString(R.string.l_by) + " " + subject.getUserName());

		// Like
		View btnSubjectLike = view.findViewById(R.id.btnLike);
		if (useHead && _position == 0)
			btnSubjectLike.setOnClickListener(onClickHeadLike);
		else
			btnSubjectLike.setOnClickListener(onClickRowLike);
		btnSubjectLike.setTag(subject.getId());

		TextView txtSubjectLikes = (TextView) view.findViewById(R.id.txtLikes);
		txtSubjectLikes.setText("" + subject.getLikes());

		// Dislike
		View btnSubjectDislike = view.findViewById(R.id.btnDislike);
		if (useHead && _position == 0)
			btnSubjectDislike.setOnClickListener(onClickHeadDislike);
		else
			btnSubjectDislike.setOnClickListener(onClickRowDislike);
		btnSubjectDislike.setTag(subject.getId());

		TextView txtSubjectDislikes = (TextView) view.findViewById(R.id.txtDislikes);
		txtSubjectDislikes.setText("" + subject.getDislikes());

		// Comments
		TextView txtSubjectComments = (TextView) view.findViewById(R.id.txtComments);
		if (txtSubjectComments != null) {
			if (subject.getComments() == 1)
				txtSubjectComments.setText(subject.getComments() + " " + getLActivity().getString(R.string.l_comment));
			else
				txtSubjectComments.setText(subject.getComments() + " " + getLActivity().getString(R.string.l_comments));
		}
	}

	private void fillComment(int _position, View view, Comment comment) {
		// Description
		TextView txtCommentDescription = (TextView) view.findViewById(R.id.txtDescription);
		txtCommentDescription.setText(comment.getContent());

		// Date
		TextView txtCommentDate = (TextView) view.findViewById(R.id.txtDate);
		if (comment.getWhen() != null)
			txtCommentDate.setText(Util.formatToLongDateTime(comment.getWhen()));
		else
			txtCommentDate.setVisibility(View.GONE);

		// Owner
		TextView txtCommentOwner = (TextView) view.findViewById(R.id.txtOwner);
		txtCommentOwner.setText(view.getContext().getString(R.string.l_by) + " " + comment.getUserName());

		// Like
		View btnCommentLike = view.findViewById(R.id.btnLike);
		if (useHead && _position == 0)
			btnCommentLike.setOnClickListener(onClickHeadLike);
		else
			btnCommentLike.setOnClickListener(onClickRowLike);
		btnCommentLike.setTag(comment.getId());

		TextView txtCommentLikes = (TextView) view.findViewById(R.id.txtLikes);
		txtCommentLikes.setText("" + comment.getLikes());

		// Dislike
		View btnCommentDislike = view.findViewById(R.id.btnDislike);
		if (useHead && _position == 0)
			btnCommentDislike.setOnClickListener(onClickHeadDislike);
		else
			btnCommentDislike.setOnClickListener(onClickRowDislike);
		btnCommentDislike.setTag(comment.getId());

		TextView txtCommentDislikes = (TextView) view.findViewById(R.id.txtDislikes);
		txtCommentDislikes.setText("" + comment.getDislikes());

		// Answers
		TextView txtCommentAnswers = (TextView) view.findViewById(R.id.txtAnswers);
		if (txtCommentAnswers != null) {
			if (comment.getAnswers() == 1)
				txtCommentAnswers.setText(comment.getAnswers() + " " + getLActivity().getString(R.string.l_answer));
			else
				txtCommentAnswers.setText(comment.getAnswers() + " " + getLActivity().getString(R.string.l_answers));
		}
	}

	private void fillAnswer(int _position, View view, Answer answer) {
		// Description
		TextView txtAnswerDescription = (TextView) view.findViewById(R.id.txtDescription);
		txtAnswerDescription.setText(answer.getContent());

		// Date
		TextView txtAnswerDate = (TextView) view.findViewById(R.id.txtDate);
		if (answer.getWhen() != null)
			txtAnswerDate.setText(Util.formatToLongDateTime(answer.getWhen()));
		else
			txtAnswerDate.setVisibility(View.GONE);

		// Owner
		TextView txtAnswerOwner = (TextView) view.findViewById(R.id.txtOwner);
		txtAnswerOwner.setText(view.getContext().getString(R.string.l_by) + " " + answer.getUserName());

		// Like
		View btnAnswerLike = view.findViewById(R.id.btnLike);
		if (useHead && _position == 0)
			btnAnswerLike.setOnClickListener(onClickHeadLike);
		else
			btnAnswerLike.setOnClickListener(onClickRowLike);
		btnAnswerLike.setTag(answer.getId());

		TextView txtAnswerLikes = (TextView) view.findViewById(R.id.txtLikes);
		txtAnswerLikes.setText("" + answer.getLikes());

		// Dislike
		View btnAnswerDislike = view.findViewById(R.id.btnDislike);
		if (useHead && _position == 0)
			btnAnswerDislike.setOnClickListener(onClickHeadDislike);
		else
			btnAnswerDislike.setOnClickListener(onClickRowDislike);
		btnAnswerDislike.setTag(answer.getId());

		TextView txtAnswerDislikes = (TextView) view.findViewById(R.id.txtDislikes);
		txtAnswerDislikes.setText("" + answer.getDislikes());
	}

	private void fillNotification(View view, Notification notification) {
		String description = "";

		description += notification.getQuantity();
		if (notification.getQuantity() == 1) {
			description += " " + view.getContext().getString(R.string.l_notification_person);

			if (notification.getWhat() == 1)
				description += " " + view.getContext().getString(R.string.l_notification_liked);
			else if (notification.getWhat() == 2)
				description += " " + view.getContext().getString(R.string.l_notification_disliked);
			else if (notification.getWhat() == 3)
				description += " " + view.getContext().getString(R.string.l_notification_comment);
			else if (notification.getWhat() == 4)
				description += " " + view.getContext().getString(R.string.l_notification_cehckin);
		} else {
			description += " " + view.getContext().getString(R.string.l_notification_people);

			if (notification.getWhat() == 1)
				description += " " + view.getContext().getString(R.string.l_notification_likeds);
			else if (notification.getWhat() == 2)
				description += " " + view.getContext().getString(R.string.l_notification_dislikeds);
			else if (notification.getWhat() == 3)
				description += " " + view.getContext().getString(R.string.l_notification_comments);
			else if (notification.getWhat() == 4)
				description += " " + view.getContext().getString(R.string.l_notification_cehckins);
		}

		switch (notification.getCustomTypeId()) {
		case ActMain.CUSTOM_TYPE_EVENT:
			if (notification.getWhat() <= 2)
				description += " " + view.getContext().getString(R.string.l_notification_of_your_event);
			else
				description += " " + view.getContext().getString(R.string.l_notification_in_your_event);
			break;

		case ActMain.CUSTOM_TYPE_SUBJECT:
			if (notification.getWhat() <= 2)
				description += " " + view.getContext().getString(R.string.l_notification_of_your_subject);
			else
				description += " " + view.getContext().getString(R.string.l_notification_in_your_subject);
			break;

		case ActMain.CUSTOM_TYPE_COMMENT:
			if (notification.getWhat() <= 2)
				description += " " + view.getContext().getString(R.string.l_notification_of_your_comment);
			else
				description += " " + view.getContext().getString(R.string.l_notification_in_your_comment);
			break;

		case ActMain.CUSTOM_TYPE_ANSWER:
			if (notification.getWhat() <= 2)
				description += " " + view.getContext().getString(R.string.l_notification_of_your_answer);
			else
				description += " " + view.getContext().getString(R.string.l_notification_in_your_answer);
			break;
		}

		// Description
		TextView txtNotificationDescription = (TextView) view.findViewById(R.id.txtDescription);
		txtNotificationDescription.setText(description);

		// Date
		TextView txtNotificationDate = (TextView) view.findViewById(R.id.txtDate);
		if (notification.getWhen() != null)
			txtNotificationDate.setText(Util.formatToLongDateTime(notification.getWhen()));
		else
			txtNotificationDate.setVisibility(View.GONE);
	}
}
