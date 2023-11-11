package com.example.chewychewy.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chewychewy.R;

public class BanhViewHolder extends RecyclerView.ViewHolder {
    ImageView iv_photo;
    TextView tv_caption;

    TextView soluongdanhgiavahang;

    RatingBar hangdanhgiasao;

    TextView tv_giabanh;
    public BanhViewHolder(View itemView) {
        super(itemView);
        iv_photo = itemView.findViewById(R.id.imv_photo);
        tv_caption = itemView.findViewById(R.id.tv_title);
        tv_giabanh = itemView.findViewById(R.id.gia);
        soluongdanhgiavahang = itemView.findViewById(R.id.slvahangdg);
        hangdanhgiasao = itemView.findViewById(R.id.ratting);
    }

}
