package com.xpto.legion;

import org.json.JSONObject;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.xpto.legion.data.Caller;
import com.xpto.legion.utils.LActivity;
import com.xpto.legion.utils.LCallback;
import com.xpto.legion.utils.LDialog;
import com.xpto.legion.utils.LFragment;

public class ActMain extends LActivity implements ActionBar.TabListener {
	// List
	private boolean searchPlaces;

	// Fragments
	private Fragment fragment;
	private View viwCover;
	// Fixed children
	private FrgMap frgMap;
	private FrgEvents frgEvents;

	public ActMain() {
		super(true, LActivity.TRANSITION_FADE);
	}

	private ViewPager pgrMain;
	private PagerAdapter adpMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);

		pgrMain = (ViewPager) findViewById(R.id.pgrMain);
		adpMain = new PagerAdapter(getSupportFragmentManager());
		pgrMain.setAdapter(adpMain);
		adpMain.notifyDataSetChanged();

		viwCover = findViewById(R.id.viwCover);
	}

	@Override
	protected void onResume() {
		super.onResume();

		searchPlaces = false;
	}

	@Override
	public void onBackPressed() {
		if (fragment == null || !(fragment instanceof LFragment) || ((LFragment) fragment).canBack())
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
			// TODO
			break;

		case R.id.action_notifications:
			// TODO
			break;

		case R.id.action_profile:
			FrgNoUser frgNoUser = new FrgNoUser();
			setFragment(frgNoUser);
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

	private void getNearPlaces() {
		Caller.getNearPlaces(ActMain.this, placesSuccess, null, placesFail, getGlobal().getLatitude(), getGlobal().getLongitude());
	}

	private LCallback placesSuccess = new LCallback() {
		@Override
		public void finished(Object _value) {
			try {
				if (_value == null || !(_value instanceof JSONObject))
					throw new Exception();

				JSONObject json = (JSONObject) _value;

				if (json.getInt("Code") != 1)
					throw new Exception();

				if (!getGlobal().addPlaces(json.getJSONArray("Content")))
					throw new Exception();
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
			getNearPlaces();
		}
	};

	public void setFragment(final Fragment _fragment) {
		if (fragment != null) {
			Animation cameOut = AnimationUtils.loadAnimation(this, R.anim.transition_dialog_out);
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
					ft.detach(fragment);
					ft.commit();

					addFragment(_fragment);
				}

			});
			fragment.getView().startAnimation(cameOut);
		} else
			addFragment(_fragment);

		if (_fragment == null) {
			if (viwCover.getVisibility() == View.VISIBLE) {
				Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.transition_fade_out);
				viwCover.startAnimation(fadeOut);
				viwCover.setVisibility(View.GONE);
			}
		} else {
			if (viwCover.getVisibility() != View.VISIBLE) {
				Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.transition_fade_in);
				viwCover.setVisibility(View.VISIBLE);
				viwCover.startAnimation(fadeIn);
			}

		}
	}

	private void addFragment(Fragment _fragment) {
		if (_fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.replace(R.id.layContent, _fragment);
			ft.commit();
		}

		fragment = _fragment;
	}

	public class PagerAdapter extends FragmentStatePagerAdapter {
		public PagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment frg;

			switch (position) {
			default:
				frg = new Fragment();
				break;

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
