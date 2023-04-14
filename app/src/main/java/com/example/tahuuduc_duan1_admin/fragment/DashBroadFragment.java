package com.example.tahuuduc_duan1_admin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.tahuuduc_duan1_admin.R;
import com.example.tahuuduc_duan1_admin.activities.MainActivity;

public class DashBroadFragment extends Fragment {
    private Toolbar toolbar;
    private TextView tvEditModule;
    private TextView tvMainBanner;


    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dash_broad,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initView(view);
//        setHasOptionsMenu(true);
//        mainActivity = (MainActivity) requireActivity();
//        mainActivity.setSupportActionBar(toolbar);
//
//        tvEditModule.setOnClickListener(v -> {
//            requireActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.contentFrame,new CustomModuleListFragment())
//        });
    }
}
