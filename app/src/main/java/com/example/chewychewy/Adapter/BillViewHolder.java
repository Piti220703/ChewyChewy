package com.example.chewychewy.Adapter;

import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chewychewy.R;

public class BillViewHolder extends RecyclerView.ViewHolder {
    TextView tv_timeDat, tv_timeNhan, tv_address, tv_total;

    GridView gridItem;
    public BillViewHolder( View itemView) {
        super(itemView);
        tv_timeDat = itemView.findViewById(R.id.tv_timeDat);
        tv_address = itemView.findViewById(R.id.tv_address);
        tv_timeNhan = itemView.findViewById(R.id.tv_timeNhan);
        tv_total = itemView.findViewById(R.id.tv_total);
        gridItem = itemView.findViewById(R.id.gridItem);

    }

}
