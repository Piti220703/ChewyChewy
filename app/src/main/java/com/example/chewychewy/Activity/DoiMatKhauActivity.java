package com.example.chewychewy.Activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chewychewy.Models.User;
import com.example.chewychewy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoiMatKhauActivity extends AppCompatActivity {

    EditText txtemail, txtmkcu, txtmkmoi, txtxnmkm;

    Button btndoi;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);
        txtemail = findViewById(R.id.email);

        txtmkmoi = findViewById(R.id.matkhaumoi);
        txtxnmkm = findViewById(R.id.matkhaumoixacnhan);
        auth = FirebaseAuth.getInstance();
        btndoi = findViewById(R.id.btndoimatkhau);
        txtmkcu = findViewById(R.id.matkhaucu);

        txtemail.setEnabled(false);
        txtemail.setText(auth.getCurrentUser().getEmail());
        btndoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (   txtxnmkm.getText().toString().length() == 0
                        || txtmkcu.getText().toString().length() == 0
                        || txtmkmoi.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Không Được Để Trống", Toast.LENGTH_SHORT).show();




                }
                else if(txtmkmoi.getText().toString().length() < 6){
                    Toast.makeText(getApplicationContext(), "Mật Khẩu Phải Dài Hơn hoặc bằng 6 kí tự", Toast.LENGTH_SHORT).show();
                }
                else if(txtmkmoi.getText().toString().equals(txtmkcu.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Mật Khẩu mới không được trùng với cũ", Toast.LENGTH_SHORT).show();
                }
                else if(txtxnmkm.getText().toString().equals(txtmkmoi.getText().toString()))
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), txtmkcu.getText().toString().trim());
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser us = auth.getCurrentUser();
                                try {
                                    us.updatePassword(txtmkmoi.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "Đổi Mật Khẩu Thành Công", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                        auth.signOut();
                                                        finish();
                                                        MainActivity.main.finish();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Đổi Mật Khẩu Thất Bại", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                            });
                                }
                                catch (Exception e){
                                    System.out.print(e.getMessage());
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Mật Khẩu Cũ Không Đúng",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else {
                    Toast.makeText(getApplicationContext(), "Mật khẩu mới xác nhận không khớp", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}