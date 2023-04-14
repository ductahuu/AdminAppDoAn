package com.example.tahuuduc_duan1_admin.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.tahuuduc_duan1_admin.R;
import com.example.tahuuduc_duan1_admin.activities.LoginActivity;
import com.example.tahuuduc_duan1_admin.activities.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

public class AccountFragment extends Fragment {
    private Toolbar toolbar;
    private TextView tvChinhSuaTaiKhoan;
    private TextView tvXemTaiKhoan;
    private TextView tvLogOut;
    private CardView cardview;

    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpToolbar();

        if (!LoginActivity.loginedUserName.equals("Admin")){
            cardview.setVisibility(View.GONE);
        }else {
            tvXemTaiKhoan.setVisibility(View.VISIBLE);
            tvXemTaiKhoan.setOnClickListener(v -> {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentFrame,new ListAccountFragment())
                        .addToBackStack(null)
                        .commit();
            });

            //thay doi mat khau,tai khoan
            tvChinhSuaTaiKhoan.setOnClickListener(v -> {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentFrame,new EditAccountFragment())
                        .addToBackStack(null)
                        .commit();
            });

            tvLogOut.setOnClickListener(v -> {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("FILE_LOGIN", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getContext(),LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            });
        }
    }

    private void setUpToolbar(){
        setHasOptionsMenu(true);
        mainActivity = (MainActivity) requireActivity();
        mainActivity.setSupportActionBar(toolbar);
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        tvChinhSuaTaiKhoan = view.findViewById(R.id.tvChinhSuaTaiKhoan);
        tvXemTaiKhoan = view.findViewById(R.id.tvXemTaiKhoan);
        tvLogOut = view.findViewById(R.id.tvLogOut);
        cardview = view.findViewById(R.id.cardview);
    }
}
