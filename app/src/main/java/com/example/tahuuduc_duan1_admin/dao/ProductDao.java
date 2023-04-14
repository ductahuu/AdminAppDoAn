package com.example.tahuuduc_duan1_admin.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tahuuduc_duan1_admin.interface_.IAfterDeleteObject;
import com.example.tahuuduc_duan1_admin.interface_.IAfterGetAllObject;
import com.example.tahuuduc_duan1_admin.interface_.IAfterInsertObject;
import com.example.tahuuduc_duan1_admin.interface_.IAfterUpdateObject;
import com.example.tahuuduc_duan1_admin.model.LoaiSP;
import com.example.tahuuduc_duan1_admin.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductDao {
    private static ProductDao instance;

    private ProductDao() {
    }

    public static ProductDao getInstance() {
        if (instance == null) {
            instance = new ProductDao();
        }
        return instance;
    }

    public void getAllProductListener(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("san_pham")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Product> result = new ArrayList<>();
                        for (DataSnapshot obj : snapshot.getChildren()) {
                            Product product = obj.getValue(Product.class);
                            if (product != null) {
                                result.add(product);
                            }
                        }
                        iAfterGetAllObject.iAfterGetAllObject(result);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iAfterGetAllObject.onError(error);
                    }
                });
    }

    public void getAllProduct(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("san_pham")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            if (snapshot != null) {
                                List<Product> productList = new ArrayList<>();
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    Product product = data.getValue(Product.class);
                                    productList.add(product);
                                }
                                iAfterGetAllObject.iAfterGetAllObject(productList);
                            } else {
                                iAfterGetAllObject.iAfterGetAllObject(new ArrayList<Product>());
                            }
                        } else {
                            iAfterGetAllObject.iAfterGetAllObject(null);
                        }
                    }
                });
    }

    public void getProductByIdListener(String id, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("san_pham").child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Product product = snapshot.getValue(Product.class);
                        iAfterGetAllObject.iAfterGetAllObject(product);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iAfterGetAllObject.onError(error);
                    }
                });
    }

    public void insertProduct(Product product, IAfterInsertObject iAfterInsertObject) {
        FirebaseDatabase.getInstance().getReference().child("san_pham").child(product.getId())
                .setValue(product, (error, ref) -> {
                    if (error == null) {
                        iAfterInsertObject.onSuccess(product);
                    } else {
                        iAfterInsertObject.onError(error);
                    }

                });
    }

    public void updateProduct(Product product, Map<String, Object> map, IAfterUpdateObject iAfterUpdateObject) {
        FirebaseDatabase.getInstance().getReference().child("san_pham").child(product.getId())
                .updateChildren(map, (error, ref) -> {
                    if (error == null) {
                        iAfterUpdateObject.onSuccess(product);
                    } else {
                        iAfterUpdateObject.onError(error);
                    }
                });
    }

    public void updateProduct(Product product, Map<String, Object> map) {
        FirebaseDatabase.getInstance().getReference().child("san_pham").child(product.getId())
                .updateChildren(map);
    }

    public void deleteProduct(Product product, IAfterDeleteObject iAfterDeleteObject) {
        FirebaseDatabase.getInstance().getReference().child("san_pham").child(product.getId()).removeValue((error, ref) -> {
            if (error == null) {
                iAfterDeleteObject.onSuccess(product);
            } else {
                iAfterDeleteObject.onError(error);
            }
        });
    }

    public void getProductById(String id, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("san_pham").child(id)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        if (dataSnapshot != null) {
                            Product product = dataSnapshot.getValue(Product.class);
                            iAfterGetAllObject.iAfterGetAllObject(product);
                        } else {
                            iAfterGetAllObject.iAfterGetAllObject(null);
                        }
                    } else {
                        iAfterGetAllObject.iAfterGetAllObject(null);
                    }
                });
    }

    public void getProductByProductType(LoaiSP loaiSP, IAfterGetAllObject iAfterGetAllObject) {
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("san_pham").orderByChild("loai_sp").equalTo(loaiSP.getId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> result = new ArrayList<>();
                for (DataSnapshot obj : snapshot.getChildren()) {
                    Product product = obj.getValue(Product.class);
                    if (product != null) {
                        result.add(product);
                    }
                }
                iAfterGetAllObject.iAfterGetAllObject(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iAfterGetAllObject.onError(error);
            }
        });
    }

    public void getProductByProductType2(LoaiSP loaiSP, IAfterGetAllObject iAfterGetAllObject) {
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("san_pham").orderByChild("loai_sp").equalTo(loaiSP.getId());
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    List<Product> result = new ArrayList<>();
                    for (DataSnapshot obj : snapshot.getChildren()) {
                        Product product = obj.getValue(Product.class);
                        if (product != null) {
                            result.add(product);
                        }
                    }
                    iAfterGetAllObject.iAfterGetAllObject(result);
                }

            }
        });
    }

    public void isDuplicateProductName(String productName, IAfterGetAllObject iAfterGetAllObject, int child) {
        FirebaseDatabase.getInstance().getReference().child("san_pham")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DataSnapshot snapshot = task.getResult();
                        int count = 0;
                        if(snapshot != null) {
                            for(DataSnapshot data : snapshot.getChildren()) {
                                Product product = data.getValue(Product.class);
                                if(product != null) {
                                    if(product.getName().equals(productName)) {
                                        count++;
                                    }
                                }

                            }
                        }
                        if (count == child) {
                            iAfterGetAllObject.iAfterGetAllObject(false);
                        } else {
                            iAfterGetAllObject.iAfterGetAllObject(true);
                        }

                    } else {
                        Log.e("TAG", "task to check product name is not successful");
                    }
                });
    }

}
