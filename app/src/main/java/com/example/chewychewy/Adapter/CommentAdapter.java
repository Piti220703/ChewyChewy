package com.example.chewychewy.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chewychewy.Models.Banh;
import com.example.chewychewy.Models.Comment;
import com.example.chewychewy.Models.User;
import com.example.chewychewy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context context;
    private List<Comment> mdatacomment;
    private FirebaseFirestore firebaseFirestore;

    public CommentAdapter(Context context, List<Comment> mdatacomment) {
        this.context = context;
        this.mdatacomment = mdatacomment;
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_comment, parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.tv_content.setText(mdatacomment.get(position).getContent());
        holder.tv_date.setText(mdatacomment.get(position).getTime());
        holder.rb_comment.setRating(mdatacomment.get(position).getRating());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore.collection("NguoiDung").whereEqualTo("id",user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                holder.tv_name.setText(doc.getString("hoTen"));
                                //Picasso.get().load(doc.getString("imagea")).into(holder.img_user);
                            }
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mdatacomment.size();
    }

    public  class CommentViewHolder extends  RecyclerView.ViewHolder{
        ImageView img_user;
        TextView tv_name, tv_content, tv_date;
        RatingBar rb_comment;

        public CommentViewHolder(View view){
            super(view);
            img_user = view.findViewById(R.id.img_user);
            tv_content = view.findViewById(R.id.comment_content);
            tv_name = view.findViewById(R.id.comment_username);
            tv_date = view.findViewById(R.id.comment_date);
            rb_comment = view.findViewById(R.id.comment_ratting);
        }
    }
    private  String timeStamp(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy",calendar).toString();
        return date;
    }
    private void SetUser(String id_user, TextView tv_name,ImageView img_user){
        FirebaseDatabase.getInstance().getReference("Users").child(id_user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User tam = snapshot.getValue(User.class);
                String ten = tam.getHoTen();
                String img = tam.getImagea();
                Picasso.get().load(img).into(img_user);
                tv_name.setText(ten);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
