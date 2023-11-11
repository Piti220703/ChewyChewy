package com.example.chewychewy.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.chewychewy.Activity.DiaChiActivity;
import com.example.chewychewy.Activity.DoiMatKhauActivity;
import com.example.chewychewy.Activity.HoSoNguoiDungActivity;
import com.example.chewychewy.Activity.LichSuDonHangActivity;
import com.example.chewychewy.Activity.LoginActivity;
import com.example.chewychewy.R;
import com.google.firebase.auth.FirebaseAuth;


public class TaiKhoanFragment extends Fragment {


    ListView lstoptaikhoan;
    ImageView anhcanhan;

    Button btnTKhoan;

    private FirebaseAuth auth;

    private Context context;

    private SwipeRefreshLayout pullToRefresh;
    public TaiKhoanFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lstoptaikhoan = view.findViewById(R.id.optiontaikhoan);
        //anhcanhan = view.findViewById(R.id.anhtaikhoan);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        btnTKhoan = view.findViewById(R.id.btnTK);
        String[] options = {"Địa Chỉ", "Hồ Sơ Người Dùng","Lịch Sử Mua Hàng","Đăng Xuất"};

        auth = FirebaseAuth.getInstance();
        ArrayAdapter<String> op = new ArrayAdapter<>(getContext(),R.layout.layout_listview_itemtaikhoan,options);
        lstoptaikhoan.setAdapter(op);



        lstoptaikhoan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




                        switch (position){
                            case 0: startActivity(new Intent(context, DiaChiActivity.class));break;
                            case 1: startActivity(new Intent(context, HoSoNguoiDungActivity.class));break;
                            case 2: startActivity(new Intent(context, LichSuDonHangActivity.class)); break;
                            case 3: auth.signOut();
                                startActivity(new Intent(getContext(), LoginActivity.class));
                                    getActivity().finish();
                                break;

                        }


            }
        });

    }

    public static TaiKhoanFragment newInstance() {
        TaiKhoanFragment fragment = new TaiKhoanFragment(newInstance().context);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tai_khoan, container, false);

    }
}