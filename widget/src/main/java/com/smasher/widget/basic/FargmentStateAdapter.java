package com.smasher.widget.basic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * @author matao
 * @date 2019/5/29
 */
public class FargmentStateAdapter extends FragmentStatePagerAdapter {

    private ArrayList<MFragment> mArrayList = new ArrayList<>();

    public FargmentStateAdapter(FragmentManager fm) {
        super(fm);
        createData();
    }


    private void createData() {
        for (int i = 0; i < 4; i++) {
            MFragment mFragment = new MFragment();
            Bundle bundle = new Bundle();
            bundle.putString("index", String.valueOf(i));
            mFragment.setArguments(bundle);
            mArrayList.add(mFragment);
        }
    }


    @Override
    public Fragment getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }
}
