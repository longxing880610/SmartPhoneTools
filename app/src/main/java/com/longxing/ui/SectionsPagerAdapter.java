package com.longxing.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longxing.R;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter extends FragmentPagerAdapter {

    private View[] mViewPages = new View[3];
    private static final int[] mResIds = {R.layout.tab_log, R.layout.tab_sd_card, R.layout.tab_version};

    SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return MainActivity.PlaceholderFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SECTION 1";
            case 1:
                return "SECTION 2";
            case 2:
                return "SECTION 3";
        }
        return null;
    }

    View getPageView(int position, LayoutInflater inflater, ViewGroup container) {
        View rootView = mViewPages[position];
        if (rootView == null) {
            int resId = mResIds[position];
            rootView = inflater.inflate(resId, container, false);
            IUI_TabMain tab = null;
            if (resId == R.layout.tab_log) {
                tab = UI_TabLog.getInstance();
            } else if (resId == R.layout.tab_sd_card) {
                tab = UI_TabSdFiles.getInstance();
            }
            if (tab != null) {  // has instance
                tab.initUI(rootView);
            }
            mViewPages[position] = rootView;
        }
        return rootView;
    }
}