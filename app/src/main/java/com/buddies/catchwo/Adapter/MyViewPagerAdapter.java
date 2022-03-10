package com.buddies.catchwo.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.buddies.catchwo.ui.Step1Fragment;
import com.buddies.catchwo.ui.Step2Fragment;
import com.buddies.catchwo.ui.Step3Fragment;
import com.buddies.catchwo.ui.Step4Fragment;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    public MyViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return Step1Fragment.getInstance();
            case 1:
                return Step2Fragment.getInstance();
            case 2:
                return Step3Fragment.getInstance();
            case 3:
                return Step4Fragment.getInstance();
        }

        return null;

    }

    @Override
    public int getCount() {
        return 4;
    }
}
