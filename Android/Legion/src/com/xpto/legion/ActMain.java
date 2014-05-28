package com.xpto.legion;

import org.json.JSONObject;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.xpto.legion.data.Caller;
import com.xpto.legion.models.Place;
import com.xpto.legion.utils.LActivity;
import com.xpto.legion.utils.LAsyncTask;
import com.xpto.legion.utils.LCallback;
import com.xpto.legion.utils.LDialog;
import com.xpto.legion.utils.LFragment;

public class ActMain extends LActivity implements ActionBar.TabListener {
	public static final int LEVEL_TOP = 1;
	public static final int LEVEL_ANSWER = 2;
	public static final int LEVEL_COMMENT = 3;
	public static final int LEVEL_SUBJECT = 4;
	public static final int LEVEL_EVENT = 5;

	public static final int CUSTOM_TYPE_EVENT = 1;
	public static final int CUSTOM_TYPE_SUBJECT = 2;
	public static final int CUSTOM_TYPE_COMMENT = 3;
	public static final int CUSTOM_TYPE_ANSWER = 4;

	// List
	private boolean searchPlaces;

	// Fragment layers
	private View viwCoverEvent;
	private LFragment fragmentEvent;
	private View viwCoverSubject;
	private LFragment fragmentSubject;
	private View viwCoverComment;
	private LFragment fragmentComment;
	private View viwCoverAnswer;
	private LFragment fragmentAnswer;
	private View viwCoverTop;
	private LFragment fragmentTop;
	// Fixed children fragments
	private FrgMap frgMap;
	private FrgEvents frgEvents;

	// Notification "service"
	private long executeAfter = 30000;
	private long lastExecution;
	private Handler handler;
	private Runnable timedExecution = new Runnable() {
		@Override
		public void run() {
			if (getGlobal().getLogged() != null && getGlobal().getLogged().getId() > 0)
				Caller.getNotifications(ActMain.this, notificationsSuccess, null, null, getGlobal().getLogged().getId());

			if (System.currentTimeMillis() - lastGetNearPlaces >= executeAfter * 2)
				getNearPlaces();

			if (handler != null && System.currentTimeMillis() - lastExecution >= executeAfter) {
				handler.postDelayed(timedExecution, executeAfter);
				lastExecution = System.currentTimeMillis();
			}
		}
	};

	public ActMain() {
		super(true, LActivity.TRANSITION_FADE);
	}

	private ViewPager pgrMain;
	private PagerAdapter adpMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);

		// Configure ViewPager
		pgrMain = (ViewPager) findViewById(R.id.pgrMain);
		adpMain = new PagerAdapter(getSupportFragmentManager());
		pgrMain.setAdapter(adpMain);
		adpMain.notifyDataSetChanged();

		// Get dark cover of each layer
		viwCoverEvent = findViewById(R.id.viwCoverEvent);
		viwCoverSubject = findViewById(R.id.viwCoverSubject);
		viwCoverComment = findViewById(R.id.viwCoverComment);
		viwCoverAnswer = findViewById(R.id.viwCoverAnswer);
		viwCoverTop = findViewById(R.id.viwCoverTop);
	}

	@Override
	protected void onResume() {
		super.onResume();

		searchPlaces = false;

		// Start notification service
		if (handler == null) {
			handler = new Handler();
			handler.postDelayed(timedExecution, 1000);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		handler = null;
	}

	@Override
	public void onBackPressed() {
		// Call back of layers
		if (fragmentTop == null || fragmentTop.canBack())
			if (fragmentAnswer == null || fragmentAnswer.canBack())
				if (fragmentComment == null || fragmentComment.canBack())
					if (fragmentSubject == null || fragmentSubject.canBack())
						if (fragmentEvent == null || fragmentEvent.canBack())
							super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.act_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_help:
			// Show specific helps
			if (fragmentTop != null)
				fragmentTop.showHelp();
			else if (fragmentAnswer != null)
				fragmentAnswer.showHelp();
			else if (fragmentComment != null)
				fragmentComment.showHelp();
			else if (fragmentSubject != null)
				fragmentSubject.showHelp();
			else if (fragmentEvent != null)
				fragmentEvent.showHelp();
			else if (pgrMain.getCurrentItem() == 0)
				frgMap.showHelp();
			else if (pgrMain.getCurrentItem() == 1)
				frgEvents.showHelp();
			break;

		case R.id.action_notifications:
			// Show/Hide notifications
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				// Ask user, if not logged
				if (fragmentTop == null || !(fragmentTop instanceof FrgNoUser) && !(fragmentTop instanceof FrgLogin) && !(fragmentTop instanceof FrgNewUser)) {
					FrgNoUser frgNoUser = new FrgNoUser();
					setFragment(frgNoUser, ActMain.LEVEL_TOP);
				}
			} else {
				if (fragmentTop == null || !(fragmentTop instanceof FrgNotifications)) {
					FrgNotifications frgNotifications = new FrgNotifications();
					setFragment(frgNotifications, ActMain.LEVEL_TOP);
				} else if (fragmentTop != null && fragmentTop instanceof FrgNotifications)
					setFragment(null, ActMain.LEVEL_TOP);
			}
			break;

		case R.id.action_profile:
			// Shoe/hide profile
			if (getGlobal().getLogged() == null || getGlobal().getLogged().getId() == 0) {
				// Ask user, if not logged
				if (fragmentTop == null || !(fragmentTop instanceof FrgNoUser) && !(fragmentTop instanceof FrgLogin) && !(fragmentTop instanceof FrgNewUser)) {
					FrgNoUser frgNoUser = new FrgNoUser();
					setFragment(frgNoUser, ActMain.LEVEL_TOP);
				}
			} else {
				if (fragmentTop == null || !(fragmentTop instanceof FrgProfile)) {
					FrgProfile frgProfile = new FrgProfile();
					setFragment(frgProfile, ActMain.LEVEL_TOP);
				} else if (fragmentTop != null && fragmentTop instanceof FrgProfile)
					setFragment(null, ActMain.LEVEL_TOP);
			}
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		pgrMain.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	public void startTracking() {
		getLocation(callbackLocation, callbackNoLocation, true);
	}

	private LCallback callbackLocation = new LCallback() {
		@Override
		public void finished(Object _value) {
			if (_value != null && _value instanceof Location) {
				Location loc = (Location) _value;

				if (getGlobal().getTime() == 0) {
					// Hold the very first location
					getGlobal().setAccuracy(loc.getAccuracy());
					getGlobal().setTime(loc.getTime());
					getGlobal().setLatitude(loc.getLatitude());
					getGlobal().setLongitude(loc.getLongitude());
				} else if (loc.getAccuracy() <= 500) {
					// For any other locations, compare against the last
					double acc1 = getGlobal().getAccuracy();
					double acc2 = loc.getAccuracy();

					// Each second let 1 m less effective
					acc2 -= (loc.getTime() - getGlobal().getTime()) / 1000f;

					if (acc1 >= acc2) {
						// Hold better location
						getGlobal().setAccuracy(loc.getAccuracy());
						getGlobal().setTime(loc.getTime());
						getGlobal().setLatitude(loc.getLatitude());
						getGlobal().setLongitude(loc.getLongitude());
					}
				}

				// Get near places with acceptable location
				if (!searchPlaces) {
					searchPlaces = true;
					getNearPlaces();
				}

				if (loc.getAccuracy() <= 200)
					// Stop tracking with a good location to keep battery
					stopTracking();

				frgMap.centerMap(loc.getLatitude(), loc.getLongitude());
			}
		}
	};

	private LCallback callbackNoLocation = new LCallback() {
		@Override
		public void finished(Object _value) {
			frgMap.centerMap(getGlobal().getLatitude(), getGlobal().getLongitude());
		}
	};

	private long lastGetNearPlaces;

	// Get near places
	private void getNearPlaces() {
		lastGetNearPlaces = System.currentTimeMillis();
		Caller.getNearPlaces(ActMain.this, placesSuccess, null, placesFail, getGlobal().getLatitude(), getGlobal().getLongitude());
	}

	private LCallback placesSuccess = new LCallback() {
		@Override
		public void finished(Object _value) {
			try {
				// If return in error
				if (_value == null || !(_value instanceof JSONObject))
					return;

				JSONObject json = (JSONObject) _value;

				// If there is problem in server
				if (json.getInt("Code") != 1)
					throw new Exception();

				// Add place to place list 
				if (!getGlobal().addPlaces(json.getJSONArray("Content")))
					throw new Exception();

				// Propagate update
				frgMap.updatePlaces();
				frgEvents.updatePlaces();
			} catch (Exception e) {
				placesFail.finished(_value);
			}
		}
	};

	private LCallback placesFail = new LCallback() {
		@Override
		public void finished(Object _value) {
			LDialog.openDialog(ActMain.this, R.string.f_no_connection, R.string.f_main_events_fail, R.string.f_ok, false, placeFailResult);
		}
	};

	private LDialog.DialogResult placeFailResult = new LDialog.DialogResult() {
		@Override
		public void result(int result, String info) {
			new LAsyncTask(ActMain.this) {
				@Override
				protected void doInBackground() {
					try {
						// Wait 20s for the next try
						long wait = 20000 - (System.currentTimeMillis() - lastGetNearPlaces);
						if (wait < 2000)
							wait = 2000;
						sleep(wait);
					} catch (Exception e) {
					}
				}

				@Override
				protected void onPostExecute() {
					// Try again
					getNearPlaces();
				}
			}.start();
		}
	};

	private LCallback notificationsSuccess = new LCallback() {
		@Override
		public void finished(Object _value) {
			try {
				// If return in error
				if (_value == null || !(_value instanceof JSONObject))
					return;

				JSONObject json = (JSONObject) _value;

				// If there is problem in server
				if (json.getInt("Code") != 1)
					throw new Exception();

				// Add notificatin to notificatin list
				if (!getGlobal().addNotifications(json.getJSONArray("Content")))
					throw new Exception();
			} catch (Exception e) {
			}
		}
	};

	// Set fragment in informed layer
	public void setFragment(final LFragment _fragment, final int _level) {
		switch (_level) {
		case LEVEL_TOP:
			replaceFragment(viwCoverTop, fragmentTop, _fragment, R.id.layContentTop);
			break;

		case LEVEL_ANSWER:
			replaceFragment(viwCoverAnswer, fragmentAnswer, _fragment, R.id.layContentAnswer);
			break;

		case LEVEL_COMMENT:
			replaceFragment(viwCoverComment, fragmentComment, _fragment, R.id.layContentComment);
			break;

		case LEVEL_SUBJECT:
			replaceFragment(viwCoverSubject, fragmentSubject, _fragment, R.id.layContentSubject);
			break;

		case LEVEL_EVENT:
			replaceFragment(viwCoverEvent, fragmentEvent, _fragment, R.id.layContentEvent);
			break;
		}
	}

	// Replace fragment in chosen layer 
	private void replaceFragment(View _cover, final LFragment _current, final LFragment _new, final int _idLayContent) {
		if (_current != null) {
			// If there is a fragment in that layer, animate it out
			Animation cameOut = _current.getOutAnimation();
			if (cameOut == null) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.detach(_current);
				ft.commit();

				addFragment(_new, _idLayContent);
			} else {
				cameOut.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						FragmentManager fragmentManager = getSupportFragmentManager();
						FragmentTransaction ft = fragmentManager.beginTransaction();
						ft.detach(_current);
						ft.commit();

						addFragment(_new, _idLayContent);
					}
				});
				_current.getView().startAnimation(cameOut);
			}
		} else
			addFragment(_new, _idLayContent);

		if (_new == null) {
			if (_cover.getVisibility() == View.VISIBLE) {
				Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.transition_fade_out);
				_cover.startAnimation(fadeOut);
				_cover.setVisibility(View.GONE);
			}
		} else {
			if (_cover.getVisibility() != View.VISIBLE) {
				Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.transition_fade_in);
				_cover.setVisibility(View.VISIBLE);
				_cover.startAnimation(fadeIn);
			}
		}
	}

	// Add the new fragment with their animation
	private void addFragment(LFragment _new, int _idLayContent) {
		if (_new != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.replace(_idLayContent, _new);
			ft.commit();
		}

		switch (_idLayContent) {
		case R.id.layContentTop:
			fragmentTop = _new;
			break;

		case R.id.layContentAnswer:
			fragmentAnswer = _new;
			break;

		case R.id.layContentComment:
			fragmentComment = _new;
			break;

		case R.id.layContentSubject:
			fragmentSubject = _new;
			break;

		case R.id.layContentEvent:
			fragmentEvent = _new;
			break;
		}

		if (fragmentTop != null)
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		else
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	// Propagate when new place is crated 
	public void propagateNewPlace(Place _place) {
		getGlobal().addPlace(_place);

		frgMap.updatePlaces();
		frgEvents.updatePlaces();

		FrgEvent frgEvent = new FrgEvent();
		frgEvent.setEvent(_place);
		setFragment(frgEvent, LEVEL_EVENT);
	}

	public class PagerAdapter extends FragmentStatePagerAdapter {
		public PagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public LFragment getItem(int position) {
			LFragment frg;

			switch (position) {
			default:
			case 0:
				if (frgMap == null)
					frgMap = new FrgMap();
				frg = frgMap;
				break;

			case 1:
				if (frgEvents == null)
					frgEvents = new FrgEvents();
				frg = frgEvents;
				break;
			}

			return frg;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.f_main_map);
			case 1:
				return getString(R.string.f_main_events);
			}

			return "";
		}
	}
}
