package com.example.chewychewy.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chewychewy.Models.DiaChi;
import com.example.chewychewy.Models.User;
import com.example.chewychewy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChinhSuaDiaChiActivity extends AppCompatActivity {

    TextView txthoten,txtsdt,txtdiachi,label;

    Button btnsuadichi;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sua_dia_chi);
        txthoten = findViewById(R.id.HoTen);
        txtsdt = findViewById(R.id.SDT);
        txtdiachi = findViewById(R.id.DiaChi);
        firebaseDatabase = FirebaseDatabase.getInstance();
        btnsuadichi = findViewById(R.id.chinhsuadiachi);
        auth = FirebaseAuth.getInstance();
        label = findViewById(R.id.labelthemdiachi);
        FirebaseUser currentuser = auth.getCurrentUser();
        intent = getIntent();
        btnsuadichi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = intent.getStringExtra("Id");
                DocumentReference myRef = FirebaseFirestore.getInstance().collection("DiaChi").document(id);
                //
                myRef.update("diaChi",txtdiachi.getText().toString(),"hoTen",txthoten.getText().toString(),"sdt",txtsdt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            Toast.makeText(getBaseContext(),"Chỉnh Sửa Địa Chỉ Thành Công",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getBaseContext(),DiaChiActivity.class));
                        }
                    }
                });
            }
        });
        ChinhSuaDuLieu();
    }
    private void ChinhSuaDuLieu(){


        String hoten = intent.getStringExtra("Hoten");
        String SDT = intent.getStringExtra("SDT");
        String DiaChi = intent.getStringExtra("DiaChi");

        txthoten.setText(hoten);
        txtsdt.setText(SDT);
        txtdiachi.setText(DiaChi);
    }
}