package com.example.tahuuduc_duan1_admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tahuuduc_duan1_admin.R;
import com.example.tahuuduc_duan1_admin.model.LoaiSP;

import java.util.List;

public class LoaiSPSpinnerAdapter extends BaseAdapter {
    Context context;
    List<LoaiSP> loaiSPList;


    public LoaiSPSpinnerAdapter(Context context, List<LoaiSP> loaiSPList) {
        this.context = context;
        this.loaiSPList = loaiSPList;
    }

    public void setData(List<LoaiSP> loaiSPList) {
        this.loaiSPList = loaiSPList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(loaiSPList != null) {
            return loaiSPList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return loaiSPList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_sp_loai_sp,null);
        TextView tvTenLoaiSP = view.findViewById(R.id.tvLoaiSP);

        LoaiSP loaiSP = loaiSPList.get(i);
        if (loaiSP != null){
            tvTenLoaiSP.setText(loaiSP.getName());
        }
        return view;
    }

    @Override
    public View getDropDownView(int i, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.item_sp_loai_sp,null);

        TextView tvTenLoaiSP = convertView.findViewById(R.id.tvLoaiSP);

        LoaiSP loaiSP = loaiSPList.get(i);
        if (loaiSP != null){
            tvTenLoaiSP.setText(loaiSP.getName());
        }
        return convertView;
    }
}
