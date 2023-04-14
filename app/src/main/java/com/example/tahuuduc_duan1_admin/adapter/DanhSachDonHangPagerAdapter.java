package com.example.tahuuduc_duan1_admin.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tahuuduc_duan1_admin.fragment.DanhSachDonHangByTTFragment;
import com.example.tahuuduc_duan1_admin.model.TrangThai;

public class DanhSachDonHangPagerAdapter extends FragmentStateAdapter {
    TrangThai[] trangThais = TrangThai.values();

    public DanhSachDonHangPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new DanhSachDonHangByTTFragment();
        Bundle args = new Bundle();
        args.putSerializable("trang_thai",trangThais[position]);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return trangThais.length;
    }
}
