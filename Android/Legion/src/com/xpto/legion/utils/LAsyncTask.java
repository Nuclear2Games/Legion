package com.xpto.legion.utils;

import android.app.Activity;

public abstract class LAsyncTask extends Thread {
	private Activity activityOwner;
	private boolean started;
	private boolean ended;

	public boolean HasStarted() {
		return started;
	}

	public boolean HasEnded() {
		return ended;
	}

	public LAsyncTask(Activity caller) {
		activityOwner = caller;
		started = false;

		onPreExecute();
	}

	@Override
	public void run() {
		started = true;

		doInBackground();
		if (activityOwner != null)
			activityOwner.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					onPostExecute();
					ended = true;
				}
			});

		super.run();
	}

	protected void onPreExecute() {
		// Do nothing
	}

	protected abstract void doInBackground();

	protected abstract void onPostExecute();
}
