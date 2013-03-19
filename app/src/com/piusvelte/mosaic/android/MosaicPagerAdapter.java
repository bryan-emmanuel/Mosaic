package com.piusvelte.mosaic.android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MosaicPagerAdapter extends FragmentPagerAdapter {
	
	private static final int mapFragment = 0;
	private static final String mapTitle = "Map";

    public MosaicPagerAdapter(FragmentManager fm) {
        super(fm);
    }
	
    @Override
    public Fragment getItem(int position) {
    	if (position == mapFragment)
    		return new MosaicMap();
		return null;
	}

	@Override
	public int getCount() {
		return mapFragment + 1;
	}

    @Override
    public CharSequence getPageTitle(int position) {
    	if (position == mapFragment)
    		return mapTitle;
    	return null;
    }

}
