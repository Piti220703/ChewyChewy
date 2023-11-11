package com.example.chewychewy.Adapter;

import static com.example.chewychewy.LayHinhAnh.loadImageFromUrl;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chewychewy.Models.Banh;
import com.example.chewychewy.Models.GioHang;
import com.example.chewychewy.R;
import com.example.chewychewy.fragment.GioHangFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangViewHolder> {

    private int selectedPosition = -1;
    FirebaseFirestore firebaseFirestore;
    private final LayoutInflater mInflater;
    private Context context;
    RecyclerView recyclerView;

    TextView textView;

    private List<GioHang> mItemList = new ArrayList<>();

    public List<GioHang> getmWordList() {
        return mItemList;
    }
    public GioHangAdapter(Context context, List<GioHang> itemlist) {
        mInflater = LayoutInflater.from(context);
        this.mItemList = itemlist;
        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public GioHangAdapter(Context context, List<GioHang> itemlist , RecyclerView recyclerView , TextView textView) {
        mInflater = LayoutInflater.from(context);
        this.mItemList = itemlist;
        this.context = context;
        this.recyclerView = recyclerView;
        firebaseFirestore = FirebaseFirestore.getInstance();
        this.textView = textView;

    }


    @NonNull
    @Override
    public GioHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.layout_itemgiohang, parent, false);
        return new GioHangViewHolder(mItemView);
    }

    private String convertpricetostring(int a){
        NumberFormat format = new DecimalFormat("#,###");
        return format.format(a);
    }
    @Override
    public void onBindViewHolder(@NonNull GioHangViewHolder holder, int position) {
        int index = position;
        GioHang gioHang = mItemList.get(position);
        holder.tonggia.setText(String.valueOf(mItemList.get(position).getTongGia()));
        holder.soLuong.setText(String.valueOf(mItemList.get(position).getSoLuong()));
        String banhId = mItemList.get(position).getBanh_id();
        firebaseFirestore.collection("Banh").whereEqualTo("id", banhId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                holder.title.setText(doc.getString("ten"));
                                holder.donGia.setText(doc.get("gia").toString() + " đ");
                                String moTa = doc.getString("mota");
                                if (moTa.length() > 25) {
                                    holder.word.setText(moTa.substring(0, 25) + "...");
                                } else {
                                    holder.word.setText(moTa);
                                }
                                //banh = new Banh(doc.getString("id"), doc.getString("ten"), doc.getString("danhmuc"), doc.getString("hinhanh"), Float.parseFloat(doc.get("gia").toString()), doc.getString("mota"));
                                Picasso.get().load(doc.getString("hinhanh")).into(holder.iv_photo);
                            }
                        }
                    }
                });
        holder.tvXoabo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference myRef = FirebaseFirestore.getInstance().collection("GioHang").document(gioHang.getId());
                myRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //fragment.createItemList();
                        mItemList.remove(index);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        GioHangAdapter adapter = new GioHangAdapter(context,mItemList);
                        recyclerView.setAdapter(adapter);
                        int tong = 0;
                        for (GioHang hang : mItemList){
                            tong += hang.getTongGia();
                        }
                        textView.setText(String.valueOf(tong) + " Đ");
                    }
                });
            }
        });
    }
    public int getSelectedPosition() {
        return selectedPosition;
    }
    @Override
    public int getItemCount() {
        return mItemList.size();
    }

}


