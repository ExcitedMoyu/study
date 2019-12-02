package com.smasher.widget.activity.basic;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * @author matao
 * @date 2019/5/29
 */
public class FragmentStateAdapter extends FragmentStatePagerAdapter {


    public FragmentStateAdapter(FragmentManager fm, ArrayList<MFragment> list) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return buildFragment(position);
    }

    @Override
    public int getCount() {
        return 4;
    }


    private Fragment buildFragment(int position) {
        MFragment mFragment = new MFragment();
        Bundle bundle = new Bundle();
        bundle.putString("index", String.valueOf(position));
        mFragment.setArguments(bundle);
        return mFragment;
    }
}
