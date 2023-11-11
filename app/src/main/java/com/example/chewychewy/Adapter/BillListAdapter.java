package com.example.chewychewy.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chewychewy.Activity.DonHangActivity;
import com.example.chewychewy.Models.Bill;
import com.example.chewychewy.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class BillListAdapter extends RecyclerView.Adapter<BillViewHolder> {

    private final LayoutInflater mInflater;
    private Context context;


    private List<Bill> mBillList = new ArrayList<>();

    public List<Bill> getmWordList() {
        return mBillList;
    }
    public BillListAdapter(Context context, List<Bill> billList) {
        mInflater = LayoutInflater.from(context);
        this.mBillList = billList;
        this.context = context;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.bill_disp_tpl, parent, false);
        return new BillViewHolder(mItemView);
    }

    private String convertpricetostring(int a){
        NumberFormat f = new DecimalFormat("#,###");
        return f.format(a);
    }
    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
//        Bill mCurrent = mHoaList.get(position);
//        holder..setText(mCurrent.getTitle_photo());
//        loadImageFromUrl(mCurrent.getSource_photo(),holder.img);

        Bill bill = mBillList.get(position);
        holder.tv_timeDat.setText(bill.getTimestamp());
        holder.tv_total.setText(convertpricetostring(bill.getTotalPrice()) + "Đ");
        holder.tv_timeNhan.setText(bill.getTime() + " "+bill.getDay());
        holder.tv_address.setText(bill.getAddress());
//        ItemListAdapter mAdapter = new ItemListAdapter(context, new ArrayList<>(), true);
//        holder.gridItem.setAdapter(mAdapter);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Create an intent to move to the other activity and pass the item information
//                Intent in = new Intent(context, DonHangActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("bill", bill);
//                in.putExtras(bundle);
//                startActivity(context,in, null);
//            }
//        });
    }
    @Override
    public int getItemCount() {
        return mBillList.size();
    }
}
