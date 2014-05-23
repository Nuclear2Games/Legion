package com.xpto.legion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.xpto.legion.data.Caller;
import com.xpto.legion.utils.LActivity;
import com.xpto.legion.utils.LCallback;
import com.xpto.legion.utils.LDialog;
import com.xpto.legion.utils.LFragment;
import com.xpto.legion.utils.Util;

public class FrgNewUser extends LFragment {
	private EditText txtLogin;
	private EditText txtPass1;
	private EditText txtPass2;
	private Button btnRegister;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frg_new_user, null);

		txtLogin = (EditText) view.findViewById(R.id.txtLogin);
		txtPass1 = (EditText) view.findViewById(R.id.txtPass1);
		txtPass2 = (EditText) view.findViewById(R.id.txtPass2);
		btnRegister = (Button) view.findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(onClickRegister);

		Animation cameIn = AnimationUtils.loadAnimation(getActivity(), R.anim.transition_dialog_in);
		cameIn.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				txtLogin.requestFocus();
				Util.showKeyboard(txtLogin);
			}
		});
		view.startAnimation(cameIn);

		return view;
	}

	@Override
	public boolean canBack() {
		((ActMain) getActivity()).setFragment(null);
		return false;
	}

	private View.OnClickListener onClickRegister = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String login = txtLogin.getText().toString();
			boolean hasSpecial = false;

			String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-";
			for (int i = 0; i < login.length(); i++)
				if (chars.indexOf(login.charAt(i)) == -1) {
					hasSpecial = true;
					break;
				}

			if (hasSpecial || login.length() < 4 || login.length() > 32) {
				LDialog.openDialog((LActivity) getActivity(), R.string.f_newuser_fill_login_title, R.string.f_newuser_fill_login_subtitle, R.string.f_ok, false);
				return;
			}

			String pass = txtPass1.getText().toString();
			if (pass.length() < 4 || pass.length() > 32) {
				LDialog.openDialog((LActivity) getActivity(), R.string.f_newuser_fill_pass_title, R.string.f_newuser_fill_pass_subtitle, R.string.f_ok, false);
				return;
			}

			String pass2 = txtPass2.getText().toString();
			if (!pass.equals(pass2)) {
				LDialog.openDialog((LActivity) getActivity(), R.string.f_newuser_fill_pass2_title, R.string.f_newuser_fill_pass2_subtitle, R.string.f_ok, false);
				return;
			}

			Caller.newUser(getActivity(), newUserSuccess, newUserRetry, newUserFail, login, pass);
		}
	};

	private LCallback newUserSuccess = new LCallback() {
		@Override
		public void finished(Object _value) {
			txtLogin.setEnabled(true);
			txtPass1.setEnabled(true);
			txtPass2.setEnabled(true);

			canBack();
		}
	};

	private LCallback newUserRetry = new LCallback() {
		@Override
		public void finished(Object _value) {
			txtLogin.setEnabled(false);
			txtPass1.setEnabled(false);
			txtPass2.setEnabled(false);
		}
	};

	private LCallback newUserFail = new LCallback() {
		@Override
		public void finished(Object _value) {
			LDialog.openDialog((LActivity) getActivity(), R.string.f_no_connection, R.string.f_newuser_fail, R.string.f_ok, false);
		}
	};
}
