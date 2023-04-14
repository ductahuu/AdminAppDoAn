package com.example.tahuuduc_duan1_admin.dao;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tahuuduc_duan1_admin.interface_.IAfterDeleteObject;
import com.example.tahuuduc_duan1_admin.interface_.IAfterGetAllObject;
import com.example.tahuuduc_duan1_admin.interface_.IAfterInsertObject;
import com.example.tahuuduc_duan1_admin.interface_.IAfterUpdateObject;
import com.example.tahuuduc_duan1_admin.model.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminDao {
    private AdminDao(){}

    public static AdminDao instance;
    public static AdminDao getInstance() {
        if(instance == null) {
            instance = new AdminDao();
        }
        return instance;
    }

    public void getAllAdminListener(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("admin")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Admin> adminList = new ArrayList<>();
                        for(DataSnapshot data : snapshot.getChildren()) {
                            Admin admin = data.getValue(Admin.class);
                            adminList.add(admin);
                        }
                        iAfterGetAllObject.iAfterGetAllObject(adminList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iAfterGetAllObject.onError(error);
                    }
                });
    }

    public void insertAdmin(Admin admin, IAfterInsertObject iAfterInsertObject) {
        FirebaseDatabase.getInstance().getReference().child("admin").child(admin.getUserName())
                .setValue(admin, (error, ref) -> {
                    if(error == null) {
                        iAfterInsertObject.onSuccess(admin);
                    } else {
                        iAfterInsertObject.onError(error);
                    }
                });
    }

    public void getAdminByUserName(String userName, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("admin").child(userName)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null) {
                            DataSnapshot snapshot = task.getResult();
                            if(snapshot != null) {
                                Admin admin = snapshot.getValue(Admin.class);
                                if(admin != null) {
                                    iAfterGetAllObject.iAfterGetAllObject(admin);
                                } else {
                                    iAfterGetAllObject.iAfterGetAllObject(null);
                                }

                            }
                        } else {
                            Log.w("TAG", task.getException());
                        }
                    }
                });
    }

    public void deleteAdmin(Context context, Admin admin, IAfterDeleteObject iAfterDeleteObject) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa admin")
                .setMessage("Bạn có chắc chắn muốn xóa?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa", (dialog, i) -> {
                    FirebaseDatabase.getInstance().getReference().child("admin").child(admin.getUserName())
                            .removeValue((error, ref) -> {
                                if(error == null) {
                                    iAfterDeleteObject.onSuccess(admin);
                                } else {
                                    iAfterDeleteObject.onError(error);
                                }
                            });
                })
                .show();
    }

    public void updateAdmin(Admin admin, IAfterUpdateObject iAfterUpdateObject){
        FirebaseDatabase.getInstance().getReference().child("admin").child(admin.getUserName())
                .updateChildren(admin.toMap(), (error, ref) -> {
                    if(error == null) {
                        iAfterUpdateObject.onSuccess(admin);
                    } else {
                        iAfterUpdateObject.onError(error);
                    }
                });
    }

    public void getAllAdmin(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("admin")
                .get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        List<Admin> adminList = new ArrayList<>();
                        for(DataSnapshot data : dataSnapshot.getChildren()) {
                            Admin admin = data.getValue(Admin.class);
                            adminList.add(admin);
                        }
                        iAfterGetAllObject.iAfterGetAllObject(adminList);
                    }
                });
    }

}
