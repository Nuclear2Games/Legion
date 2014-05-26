package com.xpto.legion;

import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.xpto.legion.data.Caller;
import com.xpto.legion.models.User;
import com.xpto.legion.utils.LActivity;
import com.xpto.legion.utils.LCallback;
import com.xpto.legion.utils.LDialog;
import com.xpto.legion.utils.LFragment;
import com.xpto.legion.utils.Util;

public class FrgProfile extends LFragment {
	private EditText txtLogin;
	private EditText txtName;
	private EditText txtDescription;
	private Button btnSave;

	private View viwHelp;

	@Override
	public View createView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.frg_profile, null);

		Util.loadFonts(view);

		txtLogin = (EditText) view.findViewById(R.id.txtLogin);
		txtName = (EditText) view.findViewById(R.id.txtName);
		txtDescription = (EditText) view.findViewById(R.id.txtDescription);
		btnSave = (Button) view.findViewById(R.id.btnRegiter);
		btnSave.setOnClickListener(onClickRegister);

		fill();

		Help.fillHelpProfile(viwHelp = view.findViewById(R.id.layHelp));

		return view;
	}

	private void fill() {
		User logged = getGlobal().getLogged();

		txtLogin.setText(logged.getLogin());
		txtName.setText(logged.getName());
		txtDescription.setText(logged.getDescription());
	}

	@Override
	public Animation getInAnimation() {
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
				if (txtName.getText().toString().length() == 0) {
					txtName.requestFocus();
					Util.showKeyboard(txtName);
				}
			}
		});
		return cameIn;
	}

	@Override
	public Animation getOutAnimation() {
		return AnimationUtils.loadAnimation(getActivity(), R.anim.transition_dialog_out);
	}

	@Override
	public boolean canBack() {
		((ActMain) getActivity()).setFragment(null, ActMain.LEVEL_TOP);
		return false;
	}

	@Override
	public void showHelp() {
		Animation cameIn = AnimationUtils.loadAnimation(getActivity(), R.anim.transition_dialog_in);
		viwHelp.setVisibility(View.VISIBLE);
		viwHelp.startAnimation(cameIn);
	}

	private View.OnClickListener onClickRegister = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String name = txtName.getText().toString();
			if (name.length() < 4 || name.length() > 32) {
				LDialog.openDialog((LActivity) getActivity(), R.string.f_profile_fill_name_title, R.string.f_profile_fill_name_subtitle, R.string.f_ok, false);
				return;
			}

			String description = txtDescription.getText().toString();

			updateUserRetry.finished(null);
			Caller.updateUser(getActivity(), updateUserSuccess, updateUserRetry, updateUserFail, getGlobal().getLogged().getId(), name, description);
		}
	};

	private LCallback updateUserSuccess = new LCallback() {
		@Override
		public void finished(Object _value) {
			if (getActivity() != null)
				((LActivity) getActivity()).endLoading();

			txtName.setEnabled(true);
			txtDescription.setEnabled(true);
			btnSave.setEnabled(true);

			try {
				if (_value == null || !(_value instanceof JSONObject))
					return;

				JSONObject json = (JSONObject) _value;
				if (json.getLong("Code") == 1) {
					User logged = getGlobal().getLogged();
					logged.setName(txtName.getText().toString());
					logged.setDescription(txtDescription.getText().toString());
					getGlobal().setLogged(logged);

					canBack();
				} else
					throw new Exception();
			} catch (Exception e) {
			}
		}
	};

	private LCallback updateUserRetry = new LCallback() {
		@Override
		public void finished(Object _value) {
			if (getActivity() != null)
				((LActivity) getActivity()).startLoading(R.string.f_sending);

			txtName.setEnabled(false);
			txtDescription.setEnabled(false);
			btnSave.setEnabled(false);
		}
	};

	private LCallback updateUserFail = new LCallback() {
		@Override
		public void finished(Object _value) {
			LDialog.openDialog((LActivity) getActivity(), R.string.f_no_connection, R.string.f_profile_fail, R.string.f_ok, false);
		}
	};
}
