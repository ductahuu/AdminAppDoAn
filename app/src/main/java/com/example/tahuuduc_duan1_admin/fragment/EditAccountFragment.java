package com.example.tahuuduc_duan1_admin.fragment;

import static com.example.tahuuduc_duan1_admin.ultis.OverUtils.ERROR_MESSAGE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.tahuuduc_duan1_admin.R;
import com.example.tahuuduc_duan1_admin.activities.LoginActivity;
import com.example.tahuuduc_duan1_admin.dao.AdminDao;
import com.example.tahuuduc_duan1_admin.interface_.IAfterGetAllObject;
import com.example.tahuuduc_duan1_admin.interface_.IAfterUpdateObject;
import com.example.tahuuduc_duan1_admin.model.Admin;
import com.example.tahuuduc_duan1_admin.ultis.OverUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseError;

//doi password fragment
public class EditAccountFragment extends Fragment {
    private Toolbar toolbar;
    private TextInputEditText edtTenDangNhap;
    private TextInputEditText edtOldPassword;
    private TextInputEditText edtNewPassword;
    private TextInputEditText edtReNewPassword;
    private MaterialButton btnCancel;
    private MaterialButton btnChangePass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_account,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpToolbar();
        setUpData();
        setUpBtnCancel();
        setUpBtnChangePass();
    }

    private void setUpBtnChangePass() {
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edtTenDangNhap.getText().toString().trim();
                String password = edtOldPassword.getText().toString().trim();
                String newPassword = edtNewPassword.getText().toString().trim();
                String reNewPassword = edtReNewPassword.getText().toString().trim();

                if (password.isEmpty() || newPassword.isEmpty() || reNewPassword.isEmpty()){
                    OverUtils.makeToast(getContext(),"Vui lòng nhập đầy đủ thông tin");
                    return;
                }
                if (!newPassword.equals(reNewPassword)){
                    OverUtils.makeToast(getContext(),"Mật khẩu mới không trùng khớp");
                    return;
                }

                AdminDao.getInstance().getAdminByUserName(userName, new IAfterGetAllObject() {
                    @Override
                    public void iAfterGetAllObject(Object obj) {
                        Admin admin = (Admin) obj;
                        if (admin != null && admin.getUserName() != null){
                            if (password.equals(admin.getPassword())) {
                                admin.setPassword(newPassword);
                                AdminDao.getInstance().updateAdmin(admin, new IAfterUpdateObject() {
                                    @Override
                                    public void onSuccess(Object obj) {
                                        OverUtils.makeToast(getContext(), "Đổi mật khẩu thành công");
                                        clearForm(1);
                                    }

                                    @Override
                                    public void onError(DatabaseError error) {
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
    }

    private void setUpBtnCancel() {
        btnCancel.setOnClickListener(v -> {
            clearForm(0);
        });
    }

    private void clearForm(int type) {
        if (type == 0){
            edtOldPassword.setText("");
        }
        edtNewPassword.setText("");
        edtReNewPassword.setText("");
    }

    private void setUpData() {
        edtTenDangNhap.setText(LoginActivity.loginedUserName);
    }

    private void setUpToolbar() {
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        edtTenDangNhap = view.findViewById(R.id.edtTenDangNhap);
        edtOldPassword = view.findViewById(R.id.edtOldPassword);
        edtNewPassword = view.findViewById(R.id.edtNewPassword);
        edtReNewPassword = view.findViewById(R.id.edtReNewPassword);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnChangePass = view.findViewById(R.id.btnChangePass);
    }
}
