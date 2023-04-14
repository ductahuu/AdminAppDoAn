package com.example.tahuuduc_duan1_admin.fragment;

import static com.example.tahuuduc_duan1_admin.ultis.OverUtils.ERROR_MESSAGE;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.tahuuduc_duan1_admin.R;
import com.example.tahuuduc_duan1_admin.adapter.LoaiSPSpinnerAdapter;
import com.example.tahuuduc_duan1_admin.dao.ProductDao;
import com.example.tahuuduc_duan1_admin.dao.ProductTypeDao;
import com.example.tahuuduc_duan1_admin.dialog.SingleChoiceDialog;
import com.example.tahuuduc_duan1_admin.interface_.IAfterGetAllObject;
import com.example.tahuuduc_duan1_admin.interface_.IAfterInsertObject;
import com.example.tahuuduc_duan1_admin.model.LoaiSP;
import com.example.tahuuduc_duan1_admin.model.Product;
import com.example.tahuuduc_duan1_admin.ultis.OverUtils;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ThemSanPhamFragment extends Fragment {
    private Toolbar toolbar;
    private ImageView imgAnhChinhSP;
    private EditText edTenSP;
    private Spinner spLoaiSP;
    private EditText edGiaBan;
    private EditText edGiamGia;
    private EditText edMoTa;
    private EditText edBaoQuan;
    private EditText edHuongDanBaoQuan;
    private EditText edThoiGianCheBien;
    private Button btnLuuSanPham;
    private EditText edKhauPhan;
    private TextView tvTrangThai;

    private Context mContext;
    private Uri imgUri;
    private String loaiSPId;


    private List<LoaiSP> loaiSPList;
    private LoaiSPSpinnerAdapter loaiSPAdapter;
    private ProgressDialog progressDialog;

    //chinh anh
    private final ActivityResultLauncher<String> getImg = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null){
                    imgAnhChinhSP.setImageURI(uri);
                    imgUri = uri;
                }
            });

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sanpham,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        setUpToolbar();
        setSpUpLoaiSP();
        setUpChonTrangThai();
        setUpGetImg();
        setUpSaveProduct();
    }

    private void setUpGetImg() {
        imgAnhChinhSP.setOnClickListener(v -> getImg.launch("image/*"));
    }

    private void setUpChonTrangThai() {
        tvTrangThai.setOnClickListener(v -> {
            String[] trangThaiSPList = requireActivity().getResources().getStringArray(R.array.choice_trang_thai_sp);
            SingleChoiceDialog singleChoiceDialog = new SingleChoiceDialog(trangThaiSPList,
                    0, "Chọn trạng thái sản phẩm", "Chọn", "Hủy"
                    , new SingleChoiceDialog.ISingleChoiceDialog() {
                @Override
                public void onChoice(Object[] objList, int position) {
                    tvTrangThai.setText((String) objList[position]);
                }

                @Override
                public void onCancel() {

                }
            });
            singleChoiceDialog.show(requireActivity().getSupportFragmentManager(),"Single choice dialog");
        });
    }

    private void setSpUpLoaiSP() {
        loaiSPList = new ArrayList<>();
        loaiSPAdapter = new LoaiSPSpinnerAdapter(getContext(),loaiSPList);
        spLoaiSP.setAdapter(loaiSPAdapter);
        spLoaiSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                LoaiSP loaiSP = loaiSPList.get(i);
                loaiSPId = loaiSP.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ProductTypeDao.getInstance().getAllProductType(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                loaiSPList = (List<LoaiSP>) obj;
                loaiSPAdapter.setData(loaiSPList);
                spLoaiSP.setSelection(0);
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    private void setUpToolbar() {
        toolbar.setNavigationOnClickListener(view -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        imgAnhChinhSP = view.findViewById(R.id.img_anhChinhSP);
        edTenSP = view.findViewById(R.id.ed_TenSP);
        spLoaiSP = view.findViewById(R.id.sp_LoaiSP);
        edGiaBan = view.findViewById(R.id.ed_GiaBan);
        edGiamGia = view.findViewById(R.id.ed_GiamGia);
        edMoTa = view.findViewById(R.id.ed_MoTa);
        edBaoQuan = view.findViewById(R.id.ed_BaoQuan);
        edHuongDanBaoQuan = view.findViewById(R.id.ed_HuongDanBaoQuan);
        edThoiGianCheBien = view.findViewById(R.id.ed_ThoiGianCheBien);
        btnLuuSanPham = view.findViewById(R.id.btn_LuuSanPham);
        edKhauPhan = view.findViewById(R.id.ed_KhauPhan);
        tvTrangThai = view.findViewById(R.id.tvTrangThai);
        edGiamGia.setText("0");
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Đang thêm sản phẩm...");
    }

    //save sp
    private void setUpSaveProduct(){
        btnLuuSanPham.setOnClickListener(v -> {
            getAndValidateInput(new IAfterGetAllObject() {
                @Override
                public void iAfterGetAllObject(Object obj) {
                    if (obj != null){
                        Product product = (Product) obj;
                        progressDialog.show();
                        getProductImg(new IAfterGetAllObject() {
                            @Override
                            public void iAfterGetAllObject(Object obj) {
                                if (obj != null){
                                   Uri uri = (Uri) obj;
                                   String imgLink = String.valueOf(uri);
                                   product.setImage(imgLink);
                                   insertProduct(product);
                                }else {
                                    OverUtils.makeToast(mContext,"Lưu ảnh thất bại, Thử lại!");
                                    progressDialog.cancel();
                                }
                            }

                            @Override
                            public void onError(DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onError(DatabaseError error) {

                }
            });
        });
    }

    private void insertProduct(Product product) {
        String key = FirebaseDatabase.getInstance().getReference("san_pham").push().getKey();
        product.setId(key);
        ProductDao.getInstance().insertProduct(product, new IAfterInsertObject() {
            @Override
            public void onSuccess(Object obj) {
                OverUtils.makeToast(getContext(),"Thêm thành công");
                progressDialog.cancel();
                clearForm();
                capNhatSoSanPhamCuaLoai(product.getLoai_sp());
                Fragment fragment = new ShowProductFragment();
                Bundle args = new Bundle();
                args.putString("product_id",product.getId());
                fragment.setArguments(args);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.contentFrame,fragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onError(DatabaseError exception) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }

    private void capNhatSoSanPhamCuaLoai(String loai_spId) {
        ProductTypeDao.getInstance().getProductTypeById(loai_spId, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                LoaiSP loaiSP = (LoaiSP) obj;
                if (loaiSP.getId() != null){
                    loaiSP.setSoSanPhamThuocLoai(loaiSP.getSoSanPhamThuocLoai() + 1);
                    ProductTypeDao.getInstance().updateProductType(loaiSP,loaiSP.toMapSoLuongSanPham());
                }
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    private void clearForm() {
        edTenSP.setText("");
        edGiamGia.setText("0");
        edBaoQuan.setText("");
        edMoTa.setText("");
        edGiaBan.setText("");
        edHuongDanBaoQuan.setText("");
        edThoiGianCheBien.setText("");
        spLoaiSP.setSelection(0);
        imgAnhChinhSP.setImageResource(R.drawable.ic_add);
        imgUri = null;
        edKhauPhan.setText("");
        tvTrangThai.setText(getString(R.string.nhan_de_chon_trang_thai));
    }

    //lay img trong database storage
    private void getProductImg(IAfterGetAllObject iAfterGetAllObject){
        StorageReference fileRef =
                FirebaseStorage.getInstance().getReference().child(System.currentTimeMillis() + "." + OverUtils.getExtensionFile(mContext,imgUri));
        fileRef.putFile(imgUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                .addOnSuccessListener(iAfterGetAllObject::iAfterGetAllObject))
                .addOnFailureListener(e -> {
                    OverUtils.makeToast(mContext,ERROR_MESSAGE);
                    iAfterGetAllObject.iAfterGetAllObject(null);
                });

    }

    private void getAndValidateInput(IAfterGetAllObject iAfterGetAllObject){
        Product product = new Product();
        String name = edTenSP.getText().toString().trim();
        String giaBan = edGiaBan.getText().toString().trim();
        String loaiSP = loaiSPId;
        String giamGia = edGiamGia.getText().toString().trim();
        String moTaSP = edMoTa.getText().toString().trim();
        String baoQuan = edBaoQuan.getText().toString().trim();
        String huongDanBaoQuan = edHuongDanBaoQuan.getText().toString().trim();
        String thoiGianCheBien = edThoiGianCheBien.getText().toString().trim();
        String khauPhan = edKhauPhan.getText().toString().trim();
        String trangThai = tvTrangThai.getText().toString().trim();

        if (name.isEmpty()) {
            OverUtils.makeToast(getContext(), "Cần thêm thông tin sản phẩm");
            return;
        }
        if (giaBan.isEmpty()) {
            OverUtils.makeToast(getContext(), "Cần thêm giá bán");
            return;
        }else {
            try {
                Integer.parseInt(giaBan);
            } catch (Exception e) {
                OverUtils.makeToast(getContext(), "thông tin giảm giá là một số");
                return;
            }
        }
        if (giamGia.isEmpty()) {
            OverUtils.makeToast(getContext(), "Cần thêm thông tin giảm giá");
        } else {
            try {
                Double.parseDouble(giamGia);
            } catch (Exception e) {
                OverUtils.makeToast(getContext(), "thông tin giảm giá là một số ( vd: 0.3 ) ");
                return;
            }
        }

        try {
            Integer.parseInt(thoiGianCheBien);
        } catch (Exception e) {
            OverUtils.makeToast(getContext(), "thời gian chế biến là một số (phút) ");
            return;
        }
        if (khauPhan.isEmpty()) {
            OverUtils.makeToast(getContext(), "Cần nhập thông tin khẩu phần");
            return;
        }
        if (trangThai.equals(getString(R.string.nhan_de_chon_trang_thai))) {
            OverUtils.makeToast(getContext(), "Cần chọn trạng thái trước khi lưu sản phẩm");
            return;
        }

        if(imgUri == null) {
            OverUtils.makeToast(getContext(), "Vui lòng chọn ảnh sản phẩm");
            return;
        }

        ProductDao.getInstance().isDuplicateProductName(name, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                if (obj != null){
                    if ((Boolean) obj){
                        OverUtils.makeToast(getContext(),"Trùng tên sản phẩm");
                    }else {
                        product.setName(name);
                        product.setGia_ban(Integer.parseInt(giaBan));
                        product.setLoai_sp(loaiSP);
                        product.setKhuyen_mai(Float.parseFloat(giamGia));
                        product.setBao_quan(baoQuan);
                        product.setMota(moTaSP);
                        product.setThong_tin_bao_quan(huongDanBaoQuan);
                        product.setThoiGianCheBien(Integer.parseInt(thoiGianCheBien));
                        product.setKhau_phan(khauPhan);
                        product.setTrang_thai(trangThai);
                        iAfterGetAllObject.iAfterGetAllObject(product);
                    }
                }else {
                    OverUtils.makeToast(getContext(),ERROR_MESSAGE);
                }
            }

            @Override
            public void onError(DatabaseError error) {

            }
        },0);
    }
}
