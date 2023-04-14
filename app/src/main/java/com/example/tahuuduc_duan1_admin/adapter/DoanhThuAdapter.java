package com.example.tahuuduc_duan1_admin.adapter;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tahuuduc_duan1_admin.R;
import com.example.tahuuduc_duan1_admin.interface_.OnClickItem;
import com.example.tahuuduc_duan1_admin.model.DonHang;
import com.example.tahuuduc_duan1_admin.model.DonHangChiTiet;
import com.example.tahuuduc_duan1_admin.ultis.OverUtils;

import java.util.List;

public class DoanhThuAdapter extends RecyclerView.Adapter<DoanhThuAdapter.DoanhThuViewHolder> {
    private List<DonHang> donHangList;
    private OnClickItem onClickItem;

    public DoanhThuAdapter(List<DonHang> donHangList, OnClickItem onClickItem) {
        this.donHangList = donHangList;
        this.onClickItem = onClickItem;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<DonHang> donHangList){
        this.donHangList = donHangList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DoanhThuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doanh_thu,parent,false);
        return new DoanhThuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoanhThuViewHolder holder, int position) {
        DonHang donHang = donHangList.get(position);
        if (donHang == null){
            return;
        }

        holder.tvMaDonHang.setText("MĐH : "+donHang.getId());
        StringBuilder sb = new StringBuilder();
        for (DonHangChiTiet dhct : donHang.getDon_hang_chi_tiets()){
            sb.append(dhct.getProduct().getName() + ", ");
        }
        holder.tvSanPhams.setText("Sản phẩm : " + sb.toString());
        String tongTien = "Doanh thu : <font color='green'>" + OverUtils.currencyFormat.format(donHang.getTong_tien()) + "</font>";
        holder.tvDoanhThu.setText(Html.fromHtml(tongTien),TextView.BufferType.SPANNABLE);
        holder.item.setOnClickListener(v -> {
            onClickItem.onClickItem(donHang.getId());
        });
    }

    @Override
    public int getItemCount() {
        if(donHangList != null) {
            return donHangList.size();
        }
        return 0;
    }

    public class DoanhThuViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout item;
        private TextView tvMaDonHang;
        private TextView tvSanPhams;
        private TextView tvDoanhThu;

        public DoanhThuViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            tvMaDonHang = itemView.findViewById(R.id.tvMaDonHang);
            tvSanPhams = itemView.findViewById(R.id.tvSanPhams);
            tvDoanhThu = itemView.findViewById(R.id.tvDoanhThu);
        }
    }
}
