package com.example.chewychewy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chewychewy.Models.Comment;
import com.example.chewychewy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class CommentActivity extends AppCompatActivity {
    RatingBar ratting_user_comment;
    EditText user_comment;
    Button btn_danhgia;
    ImageView img_banh;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Float ratting_bar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_comment);

        ratting_user_comment = findViewById(R.id.ratting_comment);
        user_comment = findViewById(R.id.edittext_comment);
        btn_danhgia = findViewById(R.id.btn_danhgia);
        img_banh = findViewById(R.id.img_comment_anh);

        firebaseDatabase =FirebaseDatabase.getInstance();
        firebaseAuth =FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        String id_user = user.getUid();//hay getProvidedId??????????????

        Intent intent = getIntent();
        String id_banh = intent.getStringExtra("id_banh");
        String url_banh = intent.getStringExtra("img_banh");
        Picasso.get().load(url_banh).into(img_banh);

        ratting_user_comment.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                ratting_user_comment.setRating(rating);
                ratting_bar = rating;
            }
        });
        btn_danhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_comment.length() != 0 || ratting_bar != 0){
                    btn_danhgia.setVisibility(View.INVISIBLE);
                    DocumentReference myRef = FirebaseFirestore.getInstance().collection("BinhLuan").document();
                    String comment = user_comment.getText().toString();
                    Comment cmt = new Comment(id_user, id_banh,comment,ratting_bar);
                    myRef.set(cmt).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isComplete()){
                                ShowMessage("Đánh giá thành công");
                                user_comment.setText("");
                                btn_danhgia.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(getBaseContext(),BanhActivity.class);
                                intent.putExtra("id", id_banh);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Đánh giá That bai!",Toast.LENGTH_SHORT).show();
                            }
                            ratting_user_comment.setRating(0);
                            user_comment.setText("");
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(),"Bạn chưa đánh giá",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private  void ShowMessage(String mess){
        Toast.makeText(this,mess, Toast.LENGTH_SHORT).show();
    }}
