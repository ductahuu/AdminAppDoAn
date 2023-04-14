package com.example.tahuuduc_duan1_admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tahuuduc_duan1_admin.R;
import com.example.tahuuduc_duan1_admin.model.Shipper;

import java.util.List;

public class ShipperSpinnerAdapter extends ArrayAdapter<Shipper> {
    private Context context;
    private List<Shipper> shipperList;
    private TextView tvShipper;

    public ShipperSpinnerAdapter(@NonNull Context context, @NonNull List<Shipper> objects) {
        super(context, R.layout.support_simple_spinner_dropdown_item,objects);
        this.context = context;
        this.shipperList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.item_spinner_shipper,null);
        }
        final Shipper shipper = shipperList.get(position);
        if(shipper != null) {
            tvShipper = v.findViewById(R.id.tvShipper);
            tvShipper.setText(shipper.getName() + "     " + shipper.getPhone_number());
        }

        return v;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.item_spinner_shipper,null);
        }
        final Shipper shipper = shipperList.get(position);
        if(shipper != null) {
            tvShipper = v.findViewById(R.id.tvShipper);
            tvShipper.setText(shipper.getName() + "     " + shipper.getPhone_number());
        }

        return v;
    }
}
