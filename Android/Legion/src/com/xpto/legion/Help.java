package com.xpto.legion;

import com.xpto.legion.data.DB;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Help {
	public static void fillHelpMap(View view) {
		String tag = "help-map";
		int idTitle = R.string.f_help_map_title;
		int idBody = R.string.f_help_map_body;

		fillHelp(view, tag, idTitle, idBody, true);
	}

	public static void fillHelpEvents(View view) {
		String tag = "help-events";
		int idTitle = R.string.f_help_events_title;
		int idBody = R.string.f_help_events_body;

		fillHelp(view, tag, idTitle, idBody, true);
	}

	public static void fillHelpAnswer(View view) {
		String tag = "help-answer";
		int idTitle = R.string.f_help_answer_title;
		int idBody = R.string.f_help_answer_body;

		fillHelp(view, tag, idTitle, idBody, false);
	}

	public static void fillHelpComment(View view) {
		String tag = "help-comment";
		int idTitle = R.string.f_help_comment_title;
		int idBody = R.string.f_help_comment_body;

		fillHelp(view, tag, idTitle, idBody, false);
	}

	public static void fillHelpEvent(View view) {
		String tag = "help-event";
		int idTitle = R.string.f_help_event_title;
		int idBody = R.string.f_help_event_body;

		fillHelp(view, tag, idTitle, idBody, true);
	}

	public static void fillHelpLogin(View view) {
		String tag = "help-login";
		int idTitle = R.string.f_help_login_title;
		int idBody = R.string.f_help_login_body;

		fillHelp(view, tag, idTitle, idBody, false);
	}

	public static void fillHelpNewEvent(View view) {
		String tag = "help-newevent";
		int idTitle = R.string.f_help_newevent_title;
		int idBody = R.string.f_help_newevent_body;

		fillHelp(view, tag, idTitle, idBody, false);
	}

	public static void fillHelpUpdateEvent(View view) {
		String tag = "help-updateevent";
		int idTitle = R.string.f_help_updateevent_title;
		int idBody = R.string.f_help_updateevent_body;

		fillHelp(view, tag, idTitle, idBody, false);
	}

	public static void fillHelpNewUser(View view) {
		String tag = "help-newuser";
		int idTitle = R.string.f_help_newuser_title;
		int idBody = R.string.f_help_newuser_body;

		fillHelp(view, tag, idTitle, idBody, false);
	}

	public static void fillHelpNotifications(View view) {
		String tag = "help-notifications";
		int idTitle = R.string.f_help_notifications_title;
		int idBody = R.string.f_help_notifications_body;

		fillHelp(view, tag, idTitle, idBody, true);
	}

	public static void fillHelpNoUser(View view) {
		String tag = "help-nouser";
		int idTitle = R.string.f_help_nouser_title;
		int idBody = R.string.f_help_nouser_body;

		fillHelp(view, tag, idTitle, idBody, false);
	}

	public static void fillHelpProfile(View view) {
		String tag = "help-profile";
		int idTitle = R.string.f_help_profile_title;
		int idBody = R.string.f_help_profile_body;

		fillHelp(view, tag, idTitle, idBody, false);
	}

	public static void fillHelpSubject(View view) {
		String tag = "help-subject";
		int idTitle = R.string.f_help_subject_title;
		int idBody = R.string.f_help_subject_body;

		fillHelp(view, tag, idTitle, idBody, false);
	}

	private static void fillHelp(View view, String tag, int idTitle, int idBody, boolean startOpen) {
		view.setOnClickListener(onClickClose);
		view.setTag(tag);
		if (DB.get(tag) == null && startOpen)
			view.setVisibility(View.VISIBLE);

		TextView txtHelpTitle = (TextView) view.findViewById(R.id.txtHelpTitle);
		txtHelpTitle.setText(idTitle);

		TextView txtHelpBody = (TextView) view.findViewById(R.id.txtHelpBody);
		txtHelpBody.setText(idBody);
	}

	private static View.OnClickListener onClickClose = new View.OnClickListener() {
		@Override
		public void onClick(final View v) {
			DB.set((String) v.getTag(), "1");

			v.setEnabled(false);
			Animation cameOut = AnimationUtils.loadAnimation(v.getContext(), R.anim.transition_dialog_out);
			cameOut.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					v.setEnabled(true);
				}
			});
			v.startAnimation(cameOut);
			v.setVisibility(View.GONE);
		}
	};
}
