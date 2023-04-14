package com.example.tahuuduc_duan1_admin.fragment;

import static com.example.tahuuduc_duan1_admin.ultis.OverUtils.ERROR_MESSAGE;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tahuuduc_duan1_admin.R;
import com.example.tahuuduc_duan1_admin.activities.MainActivity;
import com.example.tahuuduc_duan1_admin.dao.ThongKeDao;
import com.example.tahuuduc_duan1_admin.interface_.IAfterQuery;
import com.example.tahuuduc_duan1_admin.model.BangThongKe;
import com.example.tahuuduc_duan1_admin.model.DonHang;
import com.example.tahuuduc_duan1_admin.ultis.OverUtils;
import com.example.tahuuduc_duan1_admin.viewmodel.ShareViewModel;
import com.google.firebase.database.DatabaseError;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ThongKeFragment extends Fragment {

    private Toolbar toolbar;
    private Button btnChonNgayBD;
    private TextView tvNgayBD;
    private Button btnChonNgayKT;
    private TextView tvNgayKT;
    private TextView tvSoDonHang;
    private TextView tvChiTietDonHang;
    private TextView tvSoDoanhThu;
    private TextView tvChiTietDoanhThu;
    private Button btnThongKe;

    private List<DonHang> donHangList;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private MainActivity activity;
    private ShareViewModel shareViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) requireActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_thongke,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        shareViewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);
        obServerData();
        activity = (MainActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
        setUpBtnChonNgayBD();
        setUpBtnChonNgayKT();
        setUpBtnThongKe();

        tvChiTietDonHang.setOnClickListener(v -> {
            if (donHangList == null || donHangList.size() == 0){
                OverUtils.makeToast(getContext(),"Không có hóa đơn nào được tìm thấy");
            }else {
                Fragment fragment = new DanhSachDonHangByTimeFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("don_hang", (Serializable) donHangList);
                fragment.setArguments(bundle);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        tvChiTietDoanhThu.setOnClickListener(v -> {
            if (donHangList == null || donHangList.size() == 0) {
                OverUtils.makeToast(getContext(), "Không có hóa đơn nào được tìm thấy");
            } else {
                Fragment fragment = new DanhSachDoanhThuDonHangFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("don_hang", (Serializable) donHangList);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void setUpBtnThongKe() {
        btnThongKe.setOnClickListener(v -> {
            String ngayBD = tvNgayBD.getText().toString().trim();
            String ngayKT = tvNgayKT.getText().toString().trim();
            if (validInput(ngayBD,ngayKT)){
                long timeBD = getTimeFromString(ngayBD);
                long timeKT = getTimeFromString(ngayKT);
                if (timeBD == 0 || timeKT == 0) {
                    OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                }else {
                    //truong hop ngay bat dau = ngay ket thuc
                    if (timeBD == timeKT){
                        timeKT = timeKT + (24 * 60 * 60 * 1000) -1000;
                    }
                    if (timeKT < timeBD){
                        long temp = timeBD;
                        timeBD = timeKT;
                        timeKT = temp;
                    }
                    ThongKeDao.getInstance().getDonHangByTime(timeBD, timeKT, new IAfterQuery() {
                        @Override
                        public void onResult(Object obj) {
                            donHangList = (List<DonHang>) obj;
                            tvSoDonHang.setText("Số đơn hàng : " + donHangList.size());
                            tvSoDoanhThu.setText("Doanh thu : " + getSoDoanhThu(donHangList));
                            shareViewModel.setData(new BangThongKe(ngayBD,ngayKT,donHangList));
                        }

                        @Override
                        public void onError(DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private long getTimeFromString(String ngayBD) {
        Date date;
        long result = 0;
        try {
            date = simpleDateFormat.parse(ngayBD);
            if (date != null){
                result = date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    //validate ngay nhap vao
    private boolean validInput(String ngayBD, String ngayKT) {
        if (ngayBD.equals("")) {
            OverUtils.makeToast(getContext(), "Vui lòng chọn ngày bắt đầu");
            return false;
        }
        if (ngayKT.equals("")) {
            OverUtils.makeToast(getContext(), "Vui lòng chọn ngày kết thúc");
            return false;
        }
        return true;
    }

    private void setUpBtnChonNgayBD() {
        btnChonNgayBD.setOnClickListener(v -> {
            clearForm();
            createDialog(tvNgayBD);
        });
    }

    private void setUpBtnChonNgayKT() {
        btnChonNgayKT.setOnClickListener(v -> createDialog(tvNgayKT));
    }

    //set ngay bd va ket thuc
    private void createDialog(TextView tvHienThi) {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String time = getTime(year,month,dayOfMonth);
                tvHienThi.setText(time);
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(getContext(),onDateSetListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private String getTime(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,dayOfMonth);
        Date date = calendar.getTime();
        return simpleDateFormat.format(date);
    }

    private void clearForm() {
        donHangList = null;
        tvSoDonHang.setText("Số đơn hàng");
        tvSoDoanhThu.setText("Doanh Thu");
    }

    private void obServerData() {
        shareViewModel.data.observe(getViewLifecycleOwner(),bangThongKe -> {
            tvNgayBD.setText(bangThongKe.getNgayBD());
            tvNgayKT.setText(bangThongKe.getNgayKT());
            donHangList = bangThongKe.getDonHangList();
            tvSoDonHang.setText("Số đơn hàng: " + donHangList.size());
            tvSoDoanhThu.setText("Doanh thu: " + getSoDoanhThu(donHangList));
        });
    }

    private String getSoDoanhThu(List<DonHang> donHangList) {
        long doanhThu = 0;
        for (DonHang donHang : donHangList){
            doanhThu += donHang.getTong_tien(); //tinh doanh thu
        }
        return OverUtils.currencyFormat.format(doanhThu);
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        btnChonNgayBD = view.findViewById(R.id.btn_ChonNgayBD);
        tvNgayBD = view.findViewById(R.id.tv_NgayBD);
        btnChonNgayKT = view.findViewById(R.id.btn_ChonNgayKT);
        tvNgayKT = view.findViewById(R.id.tv_NgayKT);
        tvSoDonHang = view.findViewById(R.id.tvSoDonHang);
        tvChiTietDonHang = view.findViewById(R.id.tvChiTietDonHang);
        tvSoDoanhThu = view.findViewById(R.id.tvSoDoanhThu);
        tvChiTietDoanhThu = view.findViewById(R.id.tvChiTietDoanhThu);
        btnThongKe = view.findViewById(R.id.btnThongKe);
    }
}
