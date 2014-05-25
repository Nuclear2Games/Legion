package com.xpto.legion.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class LEditText extends EditText {
	private OnFocusChange onFocusChange;

	public LEditText(Context context) {
		super(context);

		setOnFocusChangeListener(onFocusChangeListener);
	}

	public LEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		setOnFocusChangeListener(onFocusChangeListener);
	}

	public LEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setOnFocusChangeListener(onFocusChangeListener);
	}

	public void setOnFocusChange(OnFocusChange _onFocusChange) {
		onFocusChange = _onFocusChange;
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && onFocusChange != null)
			onFocusChange.onFocusChange(false);
		return super.onKeyPreIme(keyCode, event);
	}

	private View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (onFocusChange != null)
				onFocusChange.onFocusChange(hasFocus);
		}
	};

	public static abstract class OnFocusChange {
		public abstract void onFocusChange(boolean _hasFocus);
	}
}
