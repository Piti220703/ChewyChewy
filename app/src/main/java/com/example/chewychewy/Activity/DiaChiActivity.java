package com.example.chewychewy.Activity;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.chewychewy.Adapter.BanhListAdapter;
import com.example.chewychewy.Adapter.ListDiaChiAdapter;
import com.example.chewychewy.Models.Banh;
import com.example.chewychewy.Models.DiaChi;
import com.example.chewychewy.R;
import com.example.chewychewy.fragment.TrangChuFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DiaChiActivity extends AppCompatActivity {

    Button themdiachi;

    ImageButton btnBack;
    String location1 = "taikhoan";
    FirebaseAuth auth;

    FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firebaseFirestore;
    ListView listdiachi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_chi);
        themdiachi = findViewById(R.id.themdiachi);
        btnBack = findViewById(R.id.btnBack);
        firebaseFirestore = FirebaseFirestore.getInstance();
        listdiachi = findViewById(R.id.listdiachi);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        if(intent.getStringExtra("location") != null){
            location1 = intent.getStringExtra("location");
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(location1.equals("Thanhtoan")){
                    Intent intent = new Intent(DiaChiActivity.this, ThanhToanActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(DiaChiActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        themdiachi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(getBaseContext(),ThemDiaChiActivity.class));
            }
        });
        firebaseFirestore.collection("DiaChi").whereEqualTo("userId",auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<DiaChi> lsdiachi = new ArrayList<>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        DiaChi diaChi = new DiaChi(doc.getId(),auth.getCurrentUser().getUid(),doc.getString("hoTen"),doc.getString("sdt"),doc.getString("diaChi"));
                        lsdiachi.add(diaChi);
                    }
                    ListDiaChiAdapter diaChiAdapter = new ListDiaChiAdapter(DiaChiActivity.this,lsdiachi);
                    listdiachi.setAdapter(diaChiAdapter);
                }
            }
        });
    }

}