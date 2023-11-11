package com.example.chewychewy.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chewychewy.Activity.ThanhToanActivity;
import com.example.chewychewy.Adapter.GioHangAdapter;
import com.example.chewychewy.Models.Banh;
import com.example.chewychewy.Models.GioHang;
import com.example.chewychewy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GioHangFragment extends Fragment {
    ArrayList<GioHang> itemArrayList;
    ArrayList<GioHang> itemArrayListDis;
    RecyclerView recyclerView;
    TextView tvTotalPrice;
    EditText editText;
    GioHangAdapter gioHangAdapter;
    Button Thanhtoan;

    FirebaseFirestore firebaseFirestore;

    FirebaseDatabase firebaseDatabase;

    FirebaseAuth firebaseAuth;

    private Context context;

    int Totalprice = 0;

    public GioHangFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }


    public static GioHangFragment newInstance() {
        GioHangFragment fragment = new GioHangFragment(newInstance().context);

        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycleitem);
        firebaseAuth = FirebaseAuth.getInstance();
        itemArrayList = new ArrayList<>();
        itemArrayListDis = new ArrayList<>();
        editText = view.findViewById(R.id.soluong);
        tvTotalPrice = view.findViewById(R.id.id_giatamtinh);
        Thanhtoan = view.findViewById(R.id.giohang);
        createItemList();

    }
    public void createItemList() {
        String customerId = firebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        Thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(),ThanhToanActivity.class);
                startActivity(intent);
            }
        });
        firebaseFirestore.collection("GioHang").whereEqualTo("customer_id", customerId).whereEqualTo("thanhToan",false)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //itemArrayList.clear();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                GioHang cartItem = new GioHang(doc.getString("id"),doc.getString("banh_id"),
                                        Integer.parseInt(doc.get("soLuong").toString()),
                                        Boolean.parseBoolean(doc.get("thanhToan").toString()),
                                        doc.getString("timestamp"),
                                        Float.parseFloat(doc.get("tongGia").toString()));
                                itemArrayList.add(cartItem);
                                Totalprice += cartItem.getTongGia();
                                String totalPriceText = Totalprice + " Đ";
                                tvTotalPrice.setText(totalPriceText);
                            }
                            for (GioHang gioHang : itemArrayList) {
                                locTonTai(gioHang);
                            }
                            gioHangAdapter = new GioHangAdapter(context,itemArrayListDis,recyclerView,tvTotalPrice);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setAdapter(gioHangAdapter);
                            gioHangAdapter.notifyDataSetChanged();
                        }
                    }
                });

        if (editText != null) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String soluongText = s.toString().trim();
                    if (soluongText.isEmpty()) {
                        return;
                    }
                    int soluong = Integer.parseInt(soluongText);

                    int position = gioHangAdapter.getSelectedPosition();
                    if (position < 0) {
                        return;
                    }
                    GioHang item = itemArrayList.get(position);
                    //item.setSoLuong(soluong);
                    //item.setTongGia(soluong * item.getHoa().getGia());

                    Totalprice = 0;
                    for (GioHang cartItem : itemArrayList) {
                        Totalprice += cartItem.getTongGia();
                    }
                    String totalPriceText = Totalprice + " Đ";
                    tvTotalPrice.setText(totalPriceText);

                    //gioHangAdapter.notifyItemChanged(position);
                    gioHangAdapter.notifyDataSetChanged();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
        /*Thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    private void locTonTai(GioHang hang){
        for (GioHang gioHang : itemArrayListDis){
            if(gioHang.getBanh_id().equals(hang.getBanh_id())){
                gioHang.setSoLuong(gioHang.getSoLuong() + hang.getSoLuong());
                gioHang.setTongGia(gioHang.getTongGia() + hang.getTongGia());
                return;
            }
        }
        itemArrayListDis.add(hang);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gio_hang, container, false);
    }
}