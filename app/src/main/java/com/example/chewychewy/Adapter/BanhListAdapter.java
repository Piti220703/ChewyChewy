package com.example.chewychewy.Adapter;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.chewychewy.LayHinhAnh.loadImageFromUrl;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chewychewy.Activity.BanhActivity;
import com.example.chewychewy.Models.Banh;
import com.example.chewychewy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class BanhListAdapter extends RecyclerView.Adapter<BanhViewHolder> {

    private final LayoutInflater mInflater;
    private Context context;
    private FirebaseStorage firebaseStorage;
    private List<Banh> mBanhList = new ArrayList<>();

    private FirebaseFirestore firebaseFirestore;

    public List<Banh> getmWordList() {
        return mBanhList;
    }
    public BanhListAdapter(Context context, List<Banh> banhlist) {
        mInflater = LayoutInflater.from(context);
        this.mBanhList = banhlist;
        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public BanhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.banh_disp_tpl, parent, false);
        return new BanhViewHolder(mItemView);
    }

    private String convertpricetostring(float a){
        NumberFormat numberFormat = new DecimalFormat("#,###");
        return numberFormat.format(a);
    }
    @Override
    public void onBindViewHolder(@NonNull BanhViewHolder holder, int position) {


        holder.tv_caption.setText(mBanhList.get(position).getTen());
        //String test = mBanhList.get(position).getImage_Banh();
        if(!mBanhList.get(position).getImage_Banh().isEmpty()){
            loadImageFromUrl(mBanhList.get(position).getImage_Banh(), holder.iv_photo);
        }

        holder.tv_giabanh.setText(convertpricetostring(mBanhList.get(position).getGia()) + "Đ");
        Banh banh = mBanhList.get(position);

        firebaseFirestore.collection("BinhLuan").whereEqualTo("id_banh",mBanhList.get(position).getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        float hangdanhgiaSum = 0;
                        int sldanhgia = 0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                hangdanhgiaSum += Float.parseFloat(doc.get("rating").toString());
                                sldanhgia++;
                            }
                            float hangdanhgiaFl = 0;
                            if(sldanhgia != 0) hangdanhgiaFl = (float) hangdanhgiaSum / sldanhgia;
                            hangdanhgiaFl = (float) Math.floor(hangdanhgiaFl * 10) / 10;
                            holder.soluongdanhgiavahang.setText((new DecimalFormat("0.0").format(hangdanhgiaFl)) + "(" + sldanhgia + ")");
                            float rating;
                            int socuoi = (int) ((hangdanhgiaFl * 10) % 10);
                            if(socuoi > 5 ) rating = ((hangdanhgiaFl * 10) - (socuoi - 5)) / 10;
                            else if(socuoi < 5) rating = ((hangdanhgiaFl * 10) - socuoi) / 10;
                            else rating = hangdanhgiaFl;
                            holder.hangdanhgiasao.setRating(rating);
                        }
                    }
                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to move to the other activity and pass the item information
                Intent intent = new Intent(context, BanhActivity.class);
                intent.putExtra("id", banh.getId());
                startActivity(context,intent,null);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mBanhList.size();
    }
}
