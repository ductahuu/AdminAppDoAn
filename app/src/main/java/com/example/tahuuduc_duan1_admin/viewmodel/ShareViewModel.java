package com.example.tahuuduc_duan1_admin.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tahuuduc_duan1_admin.model.BangThongKe;

public class ShareViewModel extends ViewModel { //android lifecycle
    public MutableLiveData<BangThongKe> data = new MutableLiveData<>();

    public void setData(BangThongKe data){
        this.data.setValue(data);
    }
}
