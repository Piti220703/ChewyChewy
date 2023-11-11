package com.example.chewychewy.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chewychewy.Adapter.BanhListAdapter;
import com.example.chewychewy.Adapter.CommentAdapter;
import com.example.chewychewy.Models.Banh;
import com.example.chewychewy.Models.Comment;
import com.example.chewychewy.Models.GioHang;
import com.example.chewychewy.Models.User;
import com.example.chewychewy.R;
import com.example.chewychewy.fragment.TrangChuFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BanhActivity extends AppCompatActivity {

    ImageView img_anh, minus, plus, btn_trove, btn_addcomment;
    TextView txt_ten, txt_gia, txt_mota, txt_comment, txt_soluong, txt_ratting;
    Button themgiohang;
    RecyclerView RVcomment;
    RatingBar ratingBar_comment;
    List<Comment> lscmt;
    List<Float> allrating = new ArrayList<Float>();
    CommentAdapter commentAdapter;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    User users;
    Integer soluong;
    String url_banh;
    Float averageRating = 0f;
    Banh banh;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_chitietsp);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        btn_trove = findViewById(R.id.btn_trove);
        img_anh = findViewById(R.id.image_anh);
        txt_ten = findViewById(R.id.txt_tensanpham);
        txt_gia = findViewById(R.id.txt_giá);
        txt_mota = findViewById(R.id.txt_motasp);
        txt_comment = findViewById(R.id.edittext_comment);
        RVcomment = findViewById(R.id.rv_comment);
        minus = findViewById(R.id.minus);
        plus = findViewById(R.id.plus);
        txt_soluong = findViewById(R.id.txt_soluong);
        themgiohang = findViewById(R.id.btn_themgiohang);
        ratingBar_comment = findViewById(R.id.ratting);
        txt_ratting = findViewById(R.id.txt_tongratting);
        btn_addcomment = findViewById(R.id.btn_add_comment);

        Intent intent = getIntent();
        String id_banh = intent.getStringExtra("id");
        RVcomment.addItemDecoration(new MyItemDecoration(10));
        btn_trove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(BanhActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String i = txt_soluong.getText().toString();
                int sl = Integer.parseInt(i);
                if (sl > 0) {
                    soluong = sl - 1;
                    txt_soluong.setText(soluong.toString());
                } else {
                    Toast.makeText(BanhActivity.this, "Không thể thêm sản phẩm", Toast.LENGTH_SHORT).show();
                }

            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String i = txt_soluong.getText().toString();
                int sl = Integer.parseInt(i);
                soluong = sl + 1;
                txt_soluong.setText(soluong.toString());
            }
        });
        themgiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customerId = firebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference myRef = firebaseFirestore.collection("GioHang").document();
                GioHang item = new GioHang(banh, Integer.valueOf(txt_soluong.getText().toString()), customerId);
                item.setId(myRef.getId());
                //System.out.println("customerId:  " + customerId);
                myRef.set(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Toast.makeText(getApplicationContext(), "Thêm Thành Công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Thêm That bai!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        firebaseFirestore.collection("Banh").whereEqualTo("id", id_banh)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                banh = new Banh(doc.getString("id"), doc.getString("ten"), doc.getString("danhmuc"), doc.getString("hinhanh"), Float.parseFloat(doc.get("gia").toString()), doc.getString("mota"));
                                txt_ten.setText(banh.getTen());
                                txt_mota.setText(banh.getMota());
                                Float gia = banh.getGia();
                                txt_gia.setText(gia.toString() + "Đ");
                                Picasso.get().load(banh.getImage_Banh()).into(img_anh);
                            }
                        }
                    }
                });
        btn_addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(BanhActivity.this, CommentActivity.class);
                intent1.putExtra("id_banh", banh.getId());
                intent1.putExtra("id_user", user.getUid());
                intent1.putExtra("img_banh", banh.getImage_Banh());
                startActivity(intent1);
            }
        });
        //doSubmit(id_banh);
        //ItemUser();
        iniRvcomment(id_banh);

    }

    public class MyItemDecoration extends RecyclerView.ItemDecoration {
        private int spacing;

        public MyItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = spacing;
            outRect.right = spacing;
            outRect.bottom = spacing;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = spacing;
            } else {
                outRect.top = 5;
            }
        }
    }

    private void iniRvcomment(String id_banh) {
        RVcomment.setLayoutManager(new LinearLayoutManager(this));
        firebaseFirestore.collection("BinhLuan").whereEqualTo("id_banh", id_banh).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                float hangdanhgiaSum = 0;
                int sldanhgia = 0;
                if (task.isSuccessful()) {
                    lscmt = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Comment cmt = new Comment(doc.getString("id_user"), doc.getString("id_banh"), doc.getString("content"), Float.parseFloat(doc.get("rating").toString()), doc.getString("time"));
                        hangdanhgiaSum += Float.parseFloat(doc.get("rating").toString());
                        sldanhgia++;
                        lscmt.add(cmt);
                    }
                    lscmt.sort(new Comparator<Comment>() {
                        @Override
                        public int compare(Comment o1, Comment o2) {
                            return o2.getTime().compareTo(o1.getTime());
                        }
                    });
                    float hangdanhgiaFl = 0;
                    if (sldanhgia != 0) hangdanhgiaFl = (float) hangdanhgiaSum / sldanhgia;
                    hangdanhgiaFl = (float) Math.floor(hangdanhgiaFl * 10) / 10;
                    txt_ratting.setText((new DecimalFormat("0.0").format(hangdanhgiaFl)) + "(" + sldanhgia + ")");
                    float rating;
                    int socuoi = (int) ((hangdanhgiaFl * 10) % 10);
                    if (socuoi > 5) rating = ((hangdanhgiaFl * 10) - (socuoi - 5)) / 10;
                    else if (socuoi < 5) rating = ((hangdanhgiaFl * 10) - socuoi) / 10;
                    else rating = hangdanhgiaFl;
                    ratingBar_comment.setRating(rating);
                    commentAdapter = new CommentAdapter(getApplicationContext(), lscmt);
                    RVcomment.setAdapter(commentAdapter);

                }
            }
        });
    }

    private void ItemUser() {
        firebaseDatabase.getReference("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User tam = snapshot.getValue(User.class);
                String ten = tam.getHoTen();
                String urlimage = tam.getImagea();
                users = new User(user.getUid(), urlimage, ten);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void doSubmit(String id) {
        DatabaseReference ratingReference = firebaseDatabase.getReference("Comment").child(id);
        ratingReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float sum = 0f;
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment cmt = snapshot.getValue(Comment.class);
                    Float tam = new Float(cmt.getRating());
                    float rating = tam.floatValue();
                    sum += rating;
                    count++;
                    allrating.add(rating);

                }
                if (count > 0) {
                    firebaseFirestore.collection("Banh").document(id.trim()).update("soLuongDanhGia", count);
                    averageRating = sum / count;
                    txt_ratting.setText(String.valueOf(new DecimalFormat("0.0").format(averageRating)));
                    ratingBar_comment.setRating(averageRating);
                    Map<String, Object> hoadg = new HashMap<>();
                    hoadg.put("hangDanhGia", averageRating);
                    firebaseFirestore.collection("Banh").document(id.trim()).update("hangDanhGia", averageRating);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}