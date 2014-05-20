package com.xpto.manyfest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import com.xpto.manyfest.data.Caller;
import com.xpto.manyfest.utils.MFActivity;

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
