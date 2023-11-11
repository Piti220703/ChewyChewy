package com.example.chewychewy.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chewychewy.Models.Banh;
import com.example.chewychewy.Models.Comment;
import com.example.chewychewy.Models.User;
import com.example.chewychewy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class HoSoNguoiDungActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView ImageCaNhan;

    EditText txtHoTen, txtSDT, txtNgaySinh;
    Button btnCatNhat;
    TextView email;
    String img;
    private StorageReference mSttorageRef;
    private FirebaseFirestore firebaseFirestore;
    private DatabaseReference mDatabaseRef;

    private FirebaseAuth auth;
    private Uri Image;
    private String docId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_so_nguoi_dung);
        ImageCaNhan = findViewById(R.id.anhhoso);
        txtHoTen = findViewById(R.id.HoTen);
        txtNgaySinh = findViewById(R.id.NgaySinh);
        txtSDT = findViewById(R.id.SDT);
        btnCatNhat = findViewById(R.id.catnhatthongtin);
        email = findViewById(R.id.email);
        firebaseFirestore = FirebaseFirestore.getInstance();
        email.setEnabled(false);
        mSttorageRef = FirebaseStorage.getInstance().getReference("uploadsCaNhan");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        //email.setText(auth.getCurrentUser().getEmail());
        String userId = auth.getCurrentUser().getUid();
        firebaseFirestore.collection("NguoiDung").whereEqualTo("id",userId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                email.setText(doc.getString("email"));
                                if(docId.isEmpty()) docId = doc.getId();
                                txtHoTen.setText(doc.getString("hoTen"));
                                txtSDT.setText(doc.getString("sdt"));
                                //img = doc.getString("imagea");
                                //Picasso.get().load(img).resize(150, 150).into(ImageCaNhan);
                                txtNgaySinh.setText(doc.getString("ngaySinh"));
                            }
                        }
                    }
                });
        ImageCaNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoAnh();
            }
        });

        btnCatNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadthongtin(docId);
                finish();
            }
        });

    }

    private void MoAnh() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Image = data.getData();
            Picasso.get().load(Image).resize(150, 150).into(ImageCaNhan);
        }
    }

    private String getFileEx(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadthongtin(String docId) {
        DocumentReference myRef = FirebaseFirestore.getInstance().collection("NguoiDung").document(docId);
        String text=auth.getCurrentUser().getUid();
        User user = new User(auth.getCurrentUser().getUid(), img,txtHoTen.getText().toString(),email.getText().toString(),txtSDT.getText().toString(),txtNgaySinh.getText().toString());
        myRef.update("hoTen",user.getHoTen(),"ngaySinh",user.getNgaySinh(),"sdt",user.getSDT()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Toast.makeText(HoSoNguoiDungActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        if (Image != null) {
//            StorageReference fileRef = mSttorageRef.child(currentuser.getUid() + "." +
//                    getFileEx(Image));
//            fileRef.putFile(Image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    mSttorageRef.child(auth.getCurrentUser().getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            User user = new User(auth.getCurrentUser().getUid(), uri.toString(), txtHoTen.getText().toString(), email.getText().toString(), txtSDT.getText().toString(),txtNgaySinh.getText().toString());
//                            mDatabaseRef.child(auth.getCurrentUser().getUid()).setValue(user);
//                            Toast.makeText(HoSoNguoiDungActivity.this, "Cật nhật thành công!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            });
//        } else {
//            mDatabaseRef.child(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                @Override
//                public void onSuccess(DataSnapshot dataSnapshot) {
//                    User us = dataSnapshot.getValue(User.class);
//                    User user = new User(auth.getCurrentUser().getUid(), us.getImagea(), txtHoTen.getText().toString(), email.getText().toString(), txtSDT.getText().toString(),
//                            txtNgaySinh.getText().toString());
//                    mDatabaseRef.child(auth.getCurrentUser().getUid()).setValue(user);
//                    Toast.makeText(HoSoNguoiDungActivity.this, "Cật nhật thành công!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
    }
}