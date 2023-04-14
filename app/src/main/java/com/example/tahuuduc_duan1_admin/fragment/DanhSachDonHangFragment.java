package com.example.tahuuduc_duan1_admin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tahuuduc_duan1_admin.R;
import com.example.tahuuduc_duan1_admin.activities.MainActivity;
import com.example.tahuuduc_duan1_admin.adapter.DanhSachDonHangPagerAdapter;
import com.example.tahuuduc_duan1_admin.model.TrangThai;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DanhSachDonHangFragment extends Fragment {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewpager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_danh_sach_don_hang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpToolbar();
        setUpTabLayoutAndViewPager();
    }
    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewpager = view.findViewById(R.id.viewpager);
    }

    private void setUpToolbar(){
        MainActivity activity = (MainActivity) requireActivity();
        setHasOptionsMenu(true);
        activity.setSupportActionBar(toolbar);
    }

    private void setUpTabLayoutAndViewPager(){
        DanhSachDonHangPagerAdapter danhSachDonHangPagerAdapter = new DanhSachDonHangPagerAdapter(requireActivity());
        viewpager.setAdapter(danhSachDonHangPagerAdapter);
        new TabLayoutMediator(tabLayout,viewpager,(tab, position) -> tab.setText(TrangThai.values()[position].getTrangThai())).attach();
        viewpager.setUserInputEnabled(true);
    }
}
