package com.example.chewychewy.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.chewychewy.Adapter.BillListAdapter;
import com.example.chewychewy.Models.Bill;
import com.example.chewychewy.Models.GioHang;
import com.example.chewychewy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LichSuDonHangActivity extends AppCompatActivity {
    RecyclerView reBill;
    BillListAdapter mBillList;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_don_hang);
        database = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        reBill = findViewById(R.id.reBill);
        reBill.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        firebaseFirestore.collection("HoaDon").whereEqualTo("customerId",auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Bill> b = new ArrayList<>();
                if(task.isComplete()){
                    for(DocumentSnapshot doc : task.getResult()){
                        List<GioHang> items = new ArrayList<>();
//                        List<GioHang> itemsss = new ArrayList<>();
//                        if(doc.get("items") != null){
//                            items = (List<GioHang>) doc.get("items");
//                        }
                        //String id,String banh_id,int soLuong , boolean thanhToan , String timestamp , float tongGia
                        Bill bill = new Bill(doc.getString("customerId"),doc.getString("timestamp"),doc.getString("day"),doc.getString("time"),doc.getString("address"),items);
                        bill.setId(doc.getString("id"));
                        bill.setTotalPrice(Integer.parseInt(doc.get("totalPrice").toString()));
                        b.add(bill);
                    }
                    mBillList = new BillListAdapter(getApplicationContext(),b);
                    reBill.setAdapter(mBillList);
                }
            }
        });

    }
}