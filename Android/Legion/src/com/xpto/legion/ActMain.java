package com.xpto.legion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.xpto.legion.data.Caller;
import com.xpto.legion.utils.MFActivity;
import com.xpto.legion.utils.MFCallback;

public class ActMain extends MFActivity implements ActionBar.TabListener {
	public ActMain() {
		super(true, MFActivity.TRANSITION_FADE);
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

		Caller.getNearPlaces(this, null, null, null, -23, -46);

		getLocation(callbackLocation, callbackNoLocation);
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
			// TODO
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

	private MFCallback callbackLocation = new MFCallback() {
		@Override
		public void finished(Object _value) {
		}
	};

	private MFCallback callbackNoLocation = new MFCallback() {
		@Override
		public void finished(Object _value) {
		}
	};

	public class PagerAdapter extends FragmentStatePagerAdapter {
		private FrgMap frgMap;
		private FrgEvents frgEvents;

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
				return getString(R.string.f_map);
			case 1:
				return getString(R.string.f_events);
			}

			return "";
		}
	}
}
