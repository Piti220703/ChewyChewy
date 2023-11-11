package com.example.chewychewy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.chewychewy.Adapter.BanhListAdapter;
import com.example.chewychewy.Models.Banh;
import com.example.chewychewy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DanhMucBanhActivity extends AppCompatActivity {

    RecyclerView listdanhmuc;

    EditText timkiemdanhmuc;

    ImageButton btntimkiem;
    Button loc;
    AutoCompleteTextView autocomplethutugia;

    SwipeRefreshLayout swipeRefreshLayout;

    TextView tendanhmuc;

    Query.Direction thutu;
    ArrayAdapter<String> itemthutugiaAD;
    String[] itemthutugia = {"Giá Từ Cao Đến Thấp","Giá Từ Thấp Đến Cao"};

    FirebaseFirestore firebaseFirestore;


    private void refreshdata(String iddanhmuc){
        firebaseFirestore.collection("Banh").whereEqualTo("danhmucId",iddanhmuc).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<Banh> listBanhh = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        Banh newbanh = new Banh(doc.getString("id"),doc.getString("ten"),doc.getString("danhmucId"),doc.getString("hinhanh"),Float.parseFloat(doc.get("gia").toString()),doc.getString("mota"));
                        listBanhh.add(newbanh);
                    }
                    BanhListAdapter adapter = new BanhListAdapter(DanhMucBanhActivity.this,listBanhh);
                    listdanhmuc.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc_banh);
        autocomplethutugia = findViewById(R.id.thutugia);
        listdanhmuc = findViewById(R.id.listdanhmuc);
        firebaseFirestore = FirebaseFirestore.getInstance();
        tendanhmuc = findViewById(R.id.tendanhmuc);
        loc = findViewById(R.id.loc);
        btntimkiem = findViewById(R.id.btntimkiem);
        swipeRefreshLayout = findViewById(R.id.refreshdata);
        timkiemdanhmuc = findViewById(R.id.timkiembanhdanhmuc);
        itemthutugiaAD = new ArrayAdapter<>(getBaseContext(),R.layout.listitem_thutugia,itemthutugia);
        autocomplethutugia.setAdapter(itemthutugiaAD);

        Intent intent = getIntent();
        String iddm =  intent.getStringExtra("iddanhmuc");
        String tendm = intent.getStringExtra("tendanhmuc");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshdata(iddm);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        listdanhmuc.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        tendanhmuc.setText(tendm);
        firebaseFirestore.collection("Banh").whereEqualTo("danhmucId",iddm).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<Banh> listBanhh = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        Banh newbanh = new Banh(doc.getString("id"),doc.getString("ten"),doc.getString("danhmucId"),doc.getString("hinhanh"),Float.parseFloat(doc.get("gia").toString()),doc.getString("mota"));
                        listBanhh.add(newbanh);
                    }
                    BanhListAdapter adapter = new BanhListAdapter(DanhMucBanhActivity.this,listBanhh);
                    listdanhmuc.setAdapter(adapter);
                }
            }
        });

        btntimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tensp = timkiemdanhmuc.getText().toString();
                if(!tensp.isEmpty()){
                    firebaseFirestore.collection("Banh").whereEqualTo("danhmucId",iddm).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                List<Banh> listBanhh = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : task.getResult()){
                                    if(doc.getString("ten").toLowerCase().contains(tensp.toLowerCase())){
                                        Banh newbanh = new Banh(doc.getString("id"),doc.getString("ten"),doc.getString("danhmucId"),doc.getString("hinhanh"),Float.parseFloat(doc.get("gia").toString()),doc.getString("mota"));
                                        listBanhh.add(newbanh);
                                    }
                                }
                                if(listBanhh.isEmpty()){
                                    Toast.makeText(getBaseContext(),"Không tìm thấy sản phẩm",Toast.LENGTH_SHORT).show();
                                }
                                BanhListAdapter adapter = new BanhListAdapter(DanhMucBanhActivity.this,listBanhh);
                                listdanhmuc.setAdapter(adapter);
                            }
                        }
                    });
                }
                else {
                    firebaseFirestore.collection("Banh").whereEqualTo("danhmucId",iddm).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                List<Banh> listBanhh = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : task.getResult()){
                                    Banh newbanh = new Banh(doc.getString("id"),doc.getString("ten"),doc.getString("danhmucId"),doc.getString("hinhanh"),Float.parseFloat(doc.get("gia").toString()),doc.getString("mota"));
                                    listBanhh.add(newbanh);
                                }
                                if(listBanhh.isEmpty()){
                                    Toast.makeText(getBaseContext(),"Không tìm thấy sản phẩm",Toast.LENGTH_SHORT).show();
                                }
                                BanhListAdapter adapter = new BanhListAdapter(DanhMucBanhActivity.this,listBanhh);
                                listdanhmuc.setAdapter(adapter);
                            }
                        }
                    });
                }

            }
        });
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String querythutugia = autocomplethutugia.getText().toString();

                if (querythutugia.equals("") || querythutugia.equals("Thứ Tự Giá")) {
                    Toast.makeText(getBaseContext(), "Chưa chọn hoặc để trống điều kiện lọc", Toast.LENGTH_SHORT).show();
                } else {
                    if (querythutugia.trim().equals("Giá Từ Cao Đến Thấp")) {
                      thutu = Query.Direction.DESCENDING;
                      locdulieu(thutu,iddm);
                    } else {
                        thutu = Query.Direction.ASCENDING;
                        locdulieu(thutu,iddm);
                    }

                }
            }
            });
        }
        private void locdulieu(Query.Direction k,String iddm){
            firebaseFirestore.collection("Banh").whereEqualTo("danhmucId",iddm).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        List<Banh> listBanhh = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            Banh newbanh = new Banh(doc.getString("id"),doc.getString("ten"),doc.getString("danhmucId"),doc.getString("hinhanh"),Float.parseFloat(doc.get("gia").toString()),doc.getString("mota"));
                            listBanhh.add(newbanh);
                        }
                        listBanhh.sort(new Comparator<Banh>() {
                            @Override
                            public int compare(Banh o1, Banh o2) {
                                if(k == Query.Direction.DESCENDING) return (int) (o2.getGia() - o1.getGia());
                                else return (int) (o1.getGia() - o2.getGia());
                            }
                        });
                        BanhListAdapter adapter = new BanhListAdapter(DanhMucBanhActivity.this,listBanhh);
                        listdanhmuc.setAdapter(adapter);
                    }
                }
            });
        }

    }

