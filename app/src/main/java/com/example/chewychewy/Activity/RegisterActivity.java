package com.example.chewychewy.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chewychewy.Models.Banh;
import com.example.chewychewy.Models.Comment;
import com.example.chewychewy.Models.User;
import com.example.chewychewy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;

public class RegisterActivity extends AppCompatActivity {

    private EditText Email;
    private EditText matkhau;
    private Button register;
    private EditText xacnhanmatkhau;

    private FirebaseAuth auth;

    private FirebaseDatabase firebaseDatabase;

    private StorageReference storageReference;

    TextView quaylaiDN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Email = findViewById(R.id.Email);
        matkhau = findViewById(R.id.matkhau);
        register = findViewById(R.id.dangky);
        quaylaiDN = findViewById(R.id.quaylaitaikhoan);
        xacnhanmatkhau = findViewById(R.id.xacnhanmatkhau);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploadsCaNhan");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = Email.getText().toString();
                String txt_matkhau = matkhau.getText().toString();
                String txt_xacnhanmk = xacnhanmatkhau.getText().toString();
                if(TextUtils.isEmpty(txt_matkhau) || TextUtils.isEmpty(txt_email)){
                    Toast.makeText(RegisterActivity.this, "không được bỏ trống",Toast.LENGTH_SHORT).show();
                }
                else if(txt_matkhau.length() < 6){
                    Toast.makeText(RegisterActivity.this, "mật khấu phải hơn 6 kí tự",Toast.LENGTH_SHORT).show();
                }
                else if(!txt_matkhau.trim().equals(txt_xacnhanmk.trim())) {
                    Toast.makeText(RegisterActivity.this, "nhập sai mật khẩu xác nhận", Toast.LENGTH_SHORT).show();
                }
                else if(!txt_email.trim().contains("@gmail.com")) {
                    Toast.makeText(RegisterActivity.this, "tài khoản phải có định dạng gmail", Toast.LENGTH_SHORT).show();
                }
                else dangky(txt_email,txt_matkhau);
            }
        });
        quaylaiDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),LoginActivity.class));

            }
        });
    }
    private String getFileEx(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private void dangky(String email, String matkhau){
        try {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("NguoiDung").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isComplete()){
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Toast.makeText(getApplicationContext(),"Tài khoản đã tồn tại",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            });
            auth.createUserWithEmailAndPassword(email,matkhau).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User newus = new User();
                        newus.setPassword(matkhau);
                        newus.setEmail(email);
                        newus.setHoTen("Người dùng");
                        //newus.setImagea("https://firebasestorage.googleapis.com/v0/b/chewychewy-d3f60.appspot.com/o/user.png?alt=media&token=21b2509d-4731-48b8-81f3-fe4fe42cb4fc&_gl=1*hjy9bb*_ga*MTA3NTk3Mzk1Mi4xNjk5MTU1MDA1*_ga_CW55HF8NVT*MTY5OTI4NzU4MS45LjEuMTY5OTI5MzQ0Mi40Ny4wLjA.");
                        newus.setId(auth.getCurrentUser().getUid());
//                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//                        StorageReference islandRef = storageRef.child("image/user.png");
//
//                        final long ONE_MEGABYTE = 1024 * 1024;
//                        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                            @Override
//                            public void onSuccess(byte[] bytes) {
//                                byte[] bytes1 = bytes;
//                                // Data for "images/island.jpg" is returns, use this as needed
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                                // Handle any errors
//                            }
//                        });

                        DocumentReference myRef = FirebaseFirestore.getInstance().collection("NguoiDung").document();
                        myRef.set(newus).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isComplete()){

//                                    storageRef.child("user.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            String test = uri.toString();
//                                            // Got the download URL for 'users/me/profile.png'
//                                        }
//                                    }).addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception exception) {
//                                            // Handle any errors
//                                        }
//                                    });
                                    Toast.makeText(RegisterActivity.this, "đăng ký thành công", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                                }
                            }
                        });

                        //StorageReference storageReference1 = FirebaseStorage.getInstance().getReference("defaultiamge/ava.jpg");
                        //storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//
//                            }
                        //});
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception ex){
            System.out.println(ex);
        }

    }
}