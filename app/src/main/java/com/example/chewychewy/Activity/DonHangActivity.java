package com.example.chewychewy.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.example.chewychewy.Adapter.GioHangAdapter;
import com.example.chewychewy.Adapter.ThanhToanAdapter;
import com.example.chewychewy.Models.Bill;
import com.example.chewychewy.Models.GioHang;
import com.example.chewychewy.R;

import java.util.ArrayList;
import java.util.List;

public class DonHangActivity extends AppCompatActivity {
    ThanhToanAdapter mAdapter;
    RecyclerView recyclerView;
    List<GioHang> listItem = new ArrayList<>();
    List<GioHang> listItemDis = new ArrayList<>();
    Button trove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang);
        Intent intent = getIntent();
        Bundle msg = intent.getExtras();
        Bill b;
        if (msg != null) {
            b = (Bill) intent.getSerializableExtra("bill");
            listItem = b.getItems();
        }
        recyclerView = findViewById(R.id.recycle_item);
        trove = findViewById(R.id.troVe);

        for (GioHang hang : listItem){
            locTonTai(hang);
        }
        mAdapter = new ThanhToanAdapter(getBaseContext(), listItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(mAdapter);
        trove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void locTonTai(GioHang hang){
        for (GioHang gioHang : listItemDis){
            if(gioHang.getBanh_id().equals(hang.getBanh_id())){
                gioHang.setSoLuong(gioHang.getSoLuong() + hang.getSoLuong());
                gioHang.setTongGia(gioHang.getTongGia() + hang.getTongGia());
                return;
            }
        }
        listItemDis.add(hang);
    }
}