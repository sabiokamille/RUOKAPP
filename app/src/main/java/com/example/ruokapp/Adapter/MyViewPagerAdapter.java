package com.example.ruokapp.Adapter;

import android.app.FragmentManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ruokapp.Fragments.BookingStep1Fragment;
import com.example.ruokapp.Fragments.BookingStep2Fragment;
import com.example.ruokapp.Fragments.BookingStep3Fragment;
import com.example.ruokapp.Fragments.BookingStep4Fragment;

import org.checkerframework.checker.nullness.qual.NonNull;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                new BookingStep1Fragment();
                return BookingStep1Fragment.getInstance();
            case 1:
                new BookingStep2Fragment();
                return BookingStep2Fragment.getInstance();
            case 2:
                new BookingStep3Fragment();
                return BookingStep3Fragment.getInstance();
            case 3:
                new BookingStep4Fragment();
                return BookingStep4Fragment.getInstance();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
