package com.xpto.legion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.xpto.legion.utils.LFragment;

public class FrgNoUser extends LFragment {
	private Button btnRegister;
	private Button btnLogin;

	@Override
	public View createView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.frg_no_user, null);

		btnRegister = (Button) view.findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(onClickRegister);

		btnLogin = (Button) view.findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(onClickLogin);

		return view;
	}

	@Override
	public Animation getInAnimation() {
		return AnimationUtils.loadAnimation(getActivity(), R.anim.transition_dialog_in);
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

	private View.OnClickListener onClickRegister = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			FrgNewUser frgNewUser = new FrgNewUser();
			((ActMain) getActivity()).setFragment(frgNewUser, ActMain.LEVEL_TOP);
		}
	};

	private View.OnClickListener onClickLogin = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			FrgLogin frgLogin = new FrgLogin();
			((ActMain) getActivity()).setFragment(frgLogin, ActMain.LEVEL_TOP);
		}
	};
}
