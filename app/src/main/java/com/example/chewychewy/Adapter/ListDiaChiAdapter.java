package com.example.chewychewy.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chewychewy.Activity.ChinhSuaDiaChiActivity;
import com.example.chewychewy.Activity.DiaChiActivity;
import com.example.chewychewy.Activity.ThanhToanActivity;
import com.example.chewychewy.Models.DiaChi;
import com.example.chewychewy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ListDiaChiAdapter extends BaseAdapter {


    TextView Hoten,DiaChi,SDT;
    Button ChinhSua, XoaBo;
    private List<DiaChi> lsdiachi;
    ListView listView;
    Context context;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;

    public ListDiaChiAdapter(Context context, List<DiaChi> lsdiachi){
        this.lsdiachi = lsdiachi;
        this.context = context;
    }

//    public ListDiaChiAdapter(Context context, List<DiaChi> lsdiachi,ListView listView){
//        this.lsdiachi = lsdiachi;
//        this.context = context;
//        this.listView = listView;
//    }
    @Override
    public int getCount() {
        return lsdiachi.size();
    }

    @Override
    public Object getItem(int position) {
        return lsdiachi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_listview_listdiachi, null);
        }



        Hoten = view.findViewById(R.id.HoTen);
        SDT = view.findViewById(R.id.SDT);
        DiaChi = view.findViewById(R.id.DiaChi);
        ChinhSua = view.findViewById(R.id.chinhsua);
        XoaBo = view.findViewById(R.id.xoabo);
        firebaseDatabase = FirebaseDatabase.getInstance();
        Hoten.setText(lsdiachi.get(position).getHoTen());
        SDT.setText(lsdiachi.get(position).getSDT());
        DiaChi.setText(lsdiachi.get(position).getDiaChi());
        auth = FirebaseAuth.getInstance();


        ChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChinhSuaDiaChiActivity.class);
                intent.putExtra("Id",lsdiachi.get(position).getId());
                intent.putExtra("Hoten",lsdiachi.get(position).getHoTen());
                intent.putExtra("SDT",lsdiachi.get(position).getSDT());
                intent.putExtra("DiaChi",lsdiachi.get(position).getDiaChi());
                //startActivity(context,intent,null);
                startActivity(context,intent,null);
            }
        });

        XoaBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference myRef = FirebaseFirestore.getInstance().collection("DiaChi").document(lsdiachi.get(position).getId());
                //
                myRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            Toast.makeText(context, "Xóa Địa Chỉ Thành Công!", Toast.LENGTH_SHORT).show();
                            startActivity(context,new Intent(context, DiaChiActivity.class),null);
                        }
                    }
                });
            }
        });
        return view;
    }
}
