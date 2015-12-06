package com.sugan.chatapplication.MainActivity.ViewPager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by sugan on 6/12/15.
 */
public class Adapter extends FragmentPagerAdapter {

    private Context _context;
    private CharSequence titles[];
    private static final int TOTAL_PAGE=3;
    public Adapter(Context context, FragmentManager fm, CharSequence titles[]) {
        super(fm);
        _context=context;
        this.titles = titles;

    }
    @Override
    public Fragment getItem(int position) {
        Fragment f = new Fragment();
        Log.d("pos", String.valueOf(position));
        switch(position){
            case 0:
                f=GroupsFragment.newInstance(_context);
                break;
            case 1:
                f= ChatsFragment.newInstance(_context);
                break;
            case 2:
                f=ContactsFragment.newInstance(_context);
                break;
        }
        return f;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
    @Override
    public int getCount() {
        return TOTAL_PAGE;
    }


}

