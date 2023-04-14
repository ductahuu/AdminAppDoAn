package com.example.tahuuduc_duan1_admin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tahuuduc_duan1_admin.R;
import com.example.tahuuduc_duan1_admin.adapter.DoanhThuAdapter;
import com.example.tahuuduc_duan1_admin.interface_.OnClickItem;
import com.example.tahuuduc_duan1_admin.model.DonHang;

import java.util.List;

public class DanhSachDoanhThuDonHangFragment extends Fragment implements OnClickItem {
    private Toolbar toolbar;
    private RecyclerView rcvDoanhThuByTime;

    private List<DonHang> donHangList;
    private DoanhThuAdapter doanhThuAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_danh_sach_doanh_thu_don_hang,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        setUpToolbar();
        getData();
        setUpDoanhThuList();

    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        rcvDoanhThuByTime = view.findViewById(R.id.rcvDoanhThuByTime);
    }

    private void setUpToolbar(){
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void getData(){
        Bundle bundle = getArguments();
        donHangList = (List<DonHang>) bundle.getSerializable("don_hang");
    }

    private void setUpDoanhThuList(){
        doanhThuAdapter = new DoanhThuAdapter(donHangList,this);
        rcvDoanhThuByTime.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvDoanhThuByTime.setAdapter(doanhThuAdapter);
    }

    @Override
    public void onClickItem(Object obj) {
        Fragment fragment = new ChiTietDonHangFragment();
        Bundle args = new Bundle();
        args.putString("ma_don_hang",String.valueOf(obj));
        fragment.setArguments(args);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentFrame,fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onUpdateItem(Object obj) {

    }

    @Override
    public void onDeleteItem(Object obj) {

    }
}
