package com.example.tahuuduc_duan1_admin.fragment;

import static com.example.tahuuduc_duan1_admin.ultis.OverUtils.ERROR_MESSAGE;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tahuuduc_duan1_admin.R;
import com.example.tahuuduc_duan1_admin.activities.MainActivity;
import com.example.tahuuduc_duan1_admin.adapter.ShipperAdapter;
import com.example.tahuuduc_duan1_admin.dao.ShiperDao;
import com.example.tahuuduc_duan1_admin.interface_.IAfterGetAllObject;
import com.example.tahuuduc_duan1_admin.interface_.IAfterInsertObject;
import com.example.tahuuduc_duan1_admin.interface_.OnClickItem;
import com.example.tahuuduc_duan1_admin.model.Shipper;
import com.example.tahuuduc_duan1_admin.ultis.OverUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShipperFragment extends Fragment implements OnClickItem {
    private Toolbar toolbar;
    private RecyclerView rcvShipper;
    private FloatingActionButton btnThemShipper;

    private List<Shipper> shipperList;
    private ShipperAdapter shipperAdapter;
    private MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shipper,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpToolbar(view);

        btnThemShipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_them_shipper);

                EditText edtThemTenShipper;
                EditText edtThemSDTShipper;
                TextView tvTitleThemShipper;
                Button btnHuy;
                Button btnThemShipper;

                edtThemTenShipper = dialog.findViewById(R.id.edtThemTenShipper);
                edtThemSDTShipper = dialog.findViewById(R.id.edtThemSDTShipper);
                tvTitleThemShipper = dialog.findViewById(R.id.tvTitleThemShipper);
                btnHuy = dialog.findViewById(R.id.btnHuy);
                btnThemShipper = dialog.findViewById(R.id.btnThemShipper);

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnThemShipper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tenShipper = edtThemTenShipper.getText().toString().trim();
                        String sdtShipper = edtThemSDTShipper.getText().toString().trim();
                        if (tenShipper.length() == 0 || sdtShipper.length() == 0){
                            OverUtils.makeToast(getContext(),"Vui lòng nhập đầy đủ thông tin");
                            return;
                        }
                        ShiperDao.getInstance().getAllShipper(new IAfterGetAllObject() {
                            @Override
                            public void iAfterGetAllObject(Object obj) {
                                if (obj != null){
                                    shipperList = (List<Shipper>) obj;
                                    boolean validShipper = validShipper(sdtShipper,shipperList);
                                    if (validShipper){
                                        String key = FirebaseDatabase.getInstance().getReference().push().getKey();
                                        Shipper shipper = new Shipper();
                                        shipper.setId(key);
                                        shipper.setName(tenShipper);
                                        shipper.setPhone_number(sdtShipper);
                                        ShiperDao.getInstance().insertShipper(shipper, new IAfterInsertObject() {
                                            @Override
                                            public void onSuccess(Object obj) {
                                                OverUtils.makeToast(getContext(),"Thêm thành công");
                                            }

                                            @Override
                                            public void onError(DatabaseError exception) {
                                                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onError(DatabaseError error) {

                            }
                        });
                    }
                });
                dialog.show();
            }
        });
        setUpListShipper();
    }

    private void setUpToolbar(View view) {
        setHasOptionsMenu(true);
        activity = (MainActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
    }

    private void setUpListShipper() {
        shipperList = new ArrayList<>();
        shipperAdapter = new ShipperAdapter(shipperList,this);
        rcvShipper.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvShipper.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        rcvShipper.setAdapter(shipperAdapter);

        ShiperDao.getInstance().getAllShipperListener(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                shipperList = (List<Shipper>) obj;
                shipperAdapter.setData(shipperList);
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    //không cho trùng số điện thoại
    private boolean validShipper(String sdtShipper, List<Shipper> shipperList) {
        for (Shipper shipper : shipperList){
            if (shipper.getPhone_number().equals(sdtShipper)){
                OverUtils.makeToast(getContext(),"Số điện thoại này đang thuộc 1 shipper khác");
                return false;
            }
        }
        return true;
    }

    public void initView(View view){
        toolbar = view.findViewById(R.id.toolbar);
        rcvShipper = view.findViewById(R.id.rcvShipper);
        btnThemShipper = view.findViewById(R.id.btnThemShipper);
    }

    @Override
    public void onClickItem(Object obj) {

    }

    @Override
    public void onUpdateItem(Object obj) {

    }

    @Override
    public void onDeleteItem(Object obj) {

    }
}
