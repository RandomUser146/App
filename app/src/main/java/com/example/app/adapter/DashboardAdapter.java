package com.example.app.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.app.fragments.StatusFragment;
import com.example.app.fragments.TasksFragment;
import com.example.app.fragments.VitalsFragment;

public class DashboardAdapter extends FragmentStateAdapter {

    public DashboardAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: {
                Log.e("DashboardAdapter", "createFragment: " + position);
                return new VitalsFragment();
            }
            case 1: {
                Log.e("DashboardAdapter", "createFragment: " + position);
                return new TasksFragment();
            }
            case 2: {
                Log.e("DashboardAdapter", "createFragment: " + position);
                return new StatusFragment();
            }
        }
        Log.e("DashboardAdapter", "createFragment: " + position);
        return new VitalsFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
