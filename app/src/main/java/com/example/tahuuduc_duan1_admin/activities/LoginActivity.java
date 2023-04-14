package com.example.tahuuduc_duan1_admin.activities;

import static com.example.tahuuduc_duan1_admin.ultis.OverUtils.ERROR_MESSAGE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.example.tahuuduc_duan1_admin.R;
import com.example.tahuuduc_duan1_admin.dao.AdminDao;
import com.example.tahuuduc_duan1_admin.interface_.IAfterGetAllObject;
import com.example.tahuuduc_duan1_admin.interface_.IDone;
import com.example.tahuuduc_duan1_admin.model.Admin;
import com.example.tahuuduc_duan1_admin.ultis.OverUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseError;

public class LoginActivity extends AppCompatActivity {
    private ImageView imageView2;
    private TextInputLayout tILEdtTenDangNhap;
    private TextInputEditText edtTenDangNhap;
    private TextInputLayout textInputLayout;
    private TextInputEditText edtMatKhau;
    private AppCompatCheckBox chkLuuMatKhau;
    private AppCompatButton btnDangNhap;
    private AppCompatButton btnHuyDangNhap;

    public static String loginedUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        readInfo();

        btnHuyDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm();
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenDangNhap = edtTenDangNhap.getText().toString().trim();
                String matKhau = edtMatKhau.getText().toString().trim();
                boolean remember;
                if(chkLuuMatKhau.isChecked()) {
                    remember = true;
                } else {
                    remember = false;
                }
                if(tenDangNhap.isEmpty() || matKhau.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
                    return;
                }
                verifyAccount(tenDangNhap, matKhau, done -> {
                    if(done) {
                        remember(tenDangNhap, matKhau, remember);
                        loginedUserName = tenDangNhap;
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
    private void clearForm() {
        edtTenDangNhap.setText("");
        edtMatKhau.setText("");
        chkLuuMatKhau.setChecked(false);
    }

    private void verifyAccount(String tenDangNhap, String matKhau, IDone iDone) {
        AdminDao.getInstance().getAdminByUserName(tenDangNhap, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                if(obj != null) {
                    Admin admin = (Admin) obj;
                    if(admin.getUserName() == null) {
                        OverUtils.makeToast(LoginActivity.this, "Tài khoản không tồn tại");
                        iDone.onDone(false);
                    } else {
                        if(!matKhau.equals(admin.getPassword())) {
                            OverUtils.makeToast(LoginActivity.this, "Mật khẩu không đúng");
                            iDone.onDone(false);
                        } else {
                            iDone.onDone(true);
                        }
                    }
                } else {
                    OverUtils.makeToast(LoginActivity.this, "Tài khoản không tồn tại");
                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(LoginActivity.this, ERROR_MESSAGE);
                iDone.onDone(false);
            }
        });
    }

    private void readInfo(){
        //SharedPreferences : nơi bạn có thể lưu trữ các thông tin dưới dạng key-value
        SharedPreferences sharedPreferences = getSharedPreferences("FILE_LOGIN", Context.MODE_PRIVATE);
        boolean remember = sharedPreferences.getBoolean("remember",false);
        String tenDangNhap = sharedPreferences.getString("username","");
        String matKhau = sharedPreferences.getString("password","");
        if (remember){
            edtTenDangNhap.setText(tenDangNhap);
            edtMatKhau.setText(matKhau);
            chkLuuMatKhau.setChecked(true);
        }
    }

    private void remember(String tenDangNhap,String matKhau,boolean remember){
        SharedPreferences sharedPreferences = getSharedPreferences("FILE_LOGIN",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(remember) {
            editor.putString("username", tenDangNhap);
            editor.putString("password", matKhau);
            editor.putBoolean("remember", remember);
            editor.apply();
        } else {
            editor.clear();
        }
    }

    private void initView() {
        imageView2 = findViewById(R.id.imageView2);
        tILEdtTenDangNhap = findViewById(R.id.tIL_edtTenDangNhap);
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        textInputLayout = findViewById(R.id.textInputLayout);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        chkLuuMatKhau = findViewById(R.id.chkLuuMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnHuyDangNhap = findViewById(R.id.btnHuyDangNhap);
    }
}
