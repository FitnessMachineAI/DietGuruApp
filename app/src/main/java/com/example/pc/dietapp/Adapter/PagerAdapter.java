package com.example.pc.dietapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.pc.dietapp.Tab1Fragment;
import com.example.pc.dietapp.Tab2Fragment;
import com.example.pc.dietapp.Tab3Fragment;

/**
 * Created by jikur on 2017-07-27.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs)
    {
        super(fm);
        this.mNumOfTabs=numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                Tab1Fragment tab1 = new Tab1Fragment();
                return tab1;
            case 1:
                Tab2Fragment tab2 = new Tab2Fragment();
                return tab2;
            case 2:
                Tab3Fragment tab3 = new Tab3Fragment();
                return tab3;

        }


        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
