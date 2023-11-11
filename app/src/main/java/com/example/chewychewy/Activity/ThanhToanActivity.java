package com.example.chewychewy.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chewychewy.Adapter.GioHangAdapter;
import com.example.chewychewy.Adapter.ThanhToanAdapter;
import com.example.chewychewy.Models.Bill;
import com.example.chewychewy.Models.DiaChi;
import com.example.chewychewy.Models.GioHang;
import com.example.chewychewy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ThanhToanActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseFirestore firebaseFirestore;
    //GridView gridItem;
    RecyclerView recyclerView;
    ThanhToanAdapter mAdapter;
    AutoCompleteTextView autodiachi;
    ArrayAdapter<String> diachiadapter;
    String[] itemdiachi;
    TextView txtDay, txtAddress, txtTime,tongtien;
    Button btnYes, themdiachi;
    ImageButton btnDay, btnTime, btnBack;
    Calendar now;
    List<GioHang> items;
    List<GioHang> itemDis;
    String day = "", time = "", address = "";
    DatePicker datePicker;
    public static final String SHARE_PRES = "com.example.chewchey";
    public static final String Address = "diachi";

    public static final String Day = "ngay";

    public static final String Time = "gio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        //gridItem = findViewById(R.id.gridItem);
        recyclerView = findViewById(R.id.recycleitem);
        txtTime = findViewById(R.id.txtTime);
        txtDay = findViewById(R.id.txtDay);
        tongtien = findViewById(R.id.tongTien);
        btnBack = findViewById(R.id.btnBack);
        btnTime = findViewById(R.id.btnTime);
        btnDay = findViewById(R.id.btnDay);
        btnYes = findViewById(R.id.btnYes);
        itemDis = new ArrayList<>();
        autodiachi = findViewById(R.id.listdiachi);
        themdiachi = findViewById(R.id.themdiachi);
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();

        /*Lay intent list items*/
        Intent intent = getIntent();
        Bundle msg = intent.getExtras();
        if (msg != null) {
            byte i = 0;
            String ite = "item";
            while (msg.getSerializable(ite.concat(String.valueOf(i))) != null) {
                GioHang item = (GioHang) msg.getSerializable(ite);
                items.add(item);
                i++;
            }
            if (msg.getSerializable("item1") != null) {
                GioHang item = (GioHang) msg.getSerializable("item1");
                items = new ArrayList<>();
                items.add(item);
                mAdapter = new ThanhToanAdapter(getBaseContext(), items);
                //gridItem.setAdapter(mAda..pter);
            }
        } else {
            firebaseFirestore.collection("GioHang").whereEqualTo("customer_id", auth.getCurrentUser().getUid()).whereEqualTo("thanhToan",false)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                items = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    GioHang item = new GioHang(doc.getString("id"),
                                            doc.getString("banh_id"),
                                            Integer.parseInt(doc.get("soLuong").toString()),
                                            Boolean.parseBoolean(doc.get("thanhToan").toString()),
                                            doc.getString("timestamp"),
                                            Float.parseFloat(doc.get("tongGia").toString()));
                                    items.add(item);
                                    //DiaChi diaChi = new DiaChi(doc.getString("id"),doc.getString("userId"),doc.getString("hoTen"),doc.getString("sdt"),doc.getString("diaChi"));
                                }
                                for (GioHang gioHang : items) {
                                    locTonTai(gioHang);
                                }
                                mAdapter = new ThanhToanAdapter(getBaseContext(), itemDis);
                                int tong = 0;
                                for(GioHang hang : itemDis){
                                    tong += hang.getTongGia();
                                }
                                tongtien.setText("Tổng tiền:" +  tong +" đ");
                                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                                recyclerView.setAdapter(mAdapter);
                                //gridItem.setAdapter(mAdapter);

                            }
                        }
                    });
//            database.getReference("GioHang").child(auth.getCurrentUser().getUid().trim()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    items = new ArrayList<>();
//                    for (DataSnapshot dt : snapshot.getChildren()) {
//                        Item item = dt.getValue(Item.class);
//                        items.add(item);
//                    }
//                    mAdapter = new ItemListAdapter(getBaseContext(), items, true);
//                    gridItem.setAdapter(mAdapter);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
        }
         /*themdiachi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ThemDiaChiActivity.class));
            }
        });*/
        autodiachi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SaveData();
            }
        });
        firebaseFirestore.collection("DiaChi").whereEqualTo("userId", auth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> dc = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                DiaChi diaChi = new DiaChi(doc.getString("id"),doc.getString("userId"),doc.getString("hoTen"),doc.getString("sdt"),doc.getString("diaChi"));
                                dc.add(diaChi.getDiaChi());
                            }
                            itemdiachi = dc.toArray(new String[0]);
                            diachiadapter = new ArrayAdapter<>(getApplicationContext(), R.layout.listitem_thutugia, itemdiachi);
                            autodiachi.setAdapter(diachiadapter);

                        }
                    }
                });

        //gridItem.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnDay.setOnClickListener(this);
        btnYes.setOnClickListener(this);
        themdiachi.setOnClickListener(this);
        loadData();
        setview();
    }

    private void locTonTai(GioHang hang){
        for (GioHang gioHang : itemDis){
            if(gioHang.getBanh_id().equals(hang.getBanh_id())){
                gioHang.setSoLuong(gioHang.getSoLuong() + hang.getSoLuong());
                gioHang.setTongGia(gioHang.getTongGia() + hang.getTongGia());
                return;
            }
        }
        itemDis.add(hang);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnDay) {
            now = Calendar.getInstance();//co can +7 hay ko?
            DatePickerDialog day = new DatePickerDialog(this, (datePicker, y, m, d)
                    -> txtDay.setText("Ngày " + d + " tháng " + (m + 1) + " năm " + y),
                    now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            datePicker = day.getDatePicker();
            day.show();
        }
        if (view.getId() == R.id.btnTime) {
            TimePickerDialog time = new TimePickerDialog(this, (timePicker, h, m)
                    -> txtTime.setText(h + " : " + m), 10, 10, true);
            time.show();
        }
        if (view.getId() == R.id.btnBack) {
            finish();
        }
        if (view.getId() == R.id.btnYes) {
            day = txtDay.getText().toString();
            time = txtTime.getText().toString();
            address = autodiachi.getText().toString();
            if (day.equals("") || time.equals("") || address.equals("")) {
                Toast.makeText(getBaseContext(), "Vui lòng nhập đầy đủ thông tin đặt hàng", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder alConfirm = new AlertDialog.Builder(this);
                alConfirm.setTitle("Xác nhận đặt bánh");
                alConfirm.setMessage("Nhận bánh lúc " + time + ", " + day + " tại " + address);
                alConfirm.setIcon(R.drawable.logo_app);
                alConfirm.setNeutralButton("Xem lại thông tin", (dialogInterface, i) -> dialogInterface.cancel());
                alConfirm.setNegativeButton("Hủy đặt", (dialogInterface, i) -> finish());
                alConfirm.setPositiveButton("Xác nhận đặt bánh", (dialogInterface, i) -> saveBill());
                alConfirm.show();
            }
        }
        if (view.getId() == R.id.themdiachi) {
            Intent intent = new Intent(getApplicationContext(), ThemDiaChiActivity.class);
            intent.putExtra("location", "Thanhtoan");
            startActivity(intent);
        }

    }

    private void saveBill() {
        //database = FirebaseDatabase.getInstance();
        if(now.get(Calendar.DAY_OF_MONTH) > datePicker.getDayOfMonth() && now.get(Calendar.MONTH) >= datePicker.getMonth() && now.get(Calendar.YEAR) >= datePicker.getYear() ){
            Toast.makeText(getApplicationContext(),"Ngày đặt hàng không hợp lệ!",Toast.LENGTH_SHORT).show();
            return;
        }
        String customerId = auth.getInstance().getCurrentUser().getUid();

        DocumentReference myRef = FirebaseFirestore.getInstance().collection("HoaDon").document();
        String key = myRef.getId();
        String timestamp = String.valueOf(Calendar.getInstance().getTimeInMillis());
        Bill bill = new Bill(customerId, timestamp, day, time, address, items);
        bill.setId(key);
        myRef.set(bill).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Toast.makeText(getApplicationContext(),"Đặt hàng Thành công!",Toast.LENGTH_SHORT).show();
                    for (GioHang gioHang : items) {
                        updateCart(gioHang.getId());
                    }

                    Intent in = new Intent(getBaseContext(), DonHangActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bill", bill);
                    in.putExtras(bundle);
                    startActivity(in);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Đặt hàng That bai!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void updateCart(String uId) {
        DocumentReference myRef = firebaseFirestore.collection("GioHang").document(uId);
        myRef.update("thanhToan",true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //String ts = "";
            }
        });
        //gioRef.removeValue();
    }

    public void SaveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PRES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Address, autodiachi.getText().toString());
        editor.putString(Day, txtDay.getText().toString());
        editor.putString(Time, txtTime.getText().toString());
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PRES, MODE_PRIVATE);
        address = sharedPreferences.getString(Address, "");
        time = sharedPreferences.getString(Time, "");
        day = sharedPreferences.getString(Day, "");
    }

    public void setview() {
        autodiachi.setText(address);
        txtDay.setText(day);
        txtTime.setText(time);
    }
}