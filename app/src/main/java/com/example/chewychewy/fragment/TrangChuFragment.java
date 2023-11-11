package com.example.chewychewy.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.chewychewy.Adapter.BanhListAdapter;
import com.example.chewychewy.Models.Banh;
import com.example.chewychewy.Models.Slider;
import com.example.chewychewy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class TrangChuFragment extends Fragment {
    GridView gridView;

    RecyclerView recyclerView;
    RecyclerView recyclerViewngang;
    ImageSlider imgslider;

    SwipeRefreshLayout swipeRefreshLayout;
    private ArrayAdapter<String> itemthutugia;

    EditText timkiemtrangchu;

    ImageButton btntimkiem, btnreset;

    private Context context;
    private FirebaseFirestore firebaseFirestore;


    private String[] itemsthutu = {"Gía từ cao đến thấp", "Gía từ thấp đến cao"};
    AutoCompleteTextView autocomple;

    private String tensp = "";

    public TrangChuFragment(Context context) {
        this.context = context;

    }

    private void refreshdata() {
        firebaseFirestore.collection("Banh").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Banh> listBanh = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Banh banh = new Banh(doc.getString("id"), doc.getString("ten"), doc.getString("danhmuc"), doc.getString("hinhanh"), Float.parseFloat(doc.get("gia").toString()), doc.getString("mota"));
                        listBanh.add(banh);
                    }

                    BanhListAdapter adapterngang = new BanhListAdapter(context, listBanh);
                    recyclerViewngang.setAdapter(adapterngang);

                    BanhListAdapter adapter = new BanhListAdapter(context, listBanh);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgslider = view.findViewById(R.id.image_slider);
        gridView = view.findViewById(R.id.gridview);
        recyclerView = view.findViewById(R.id.recycleviewbanh);
        recyclerViewngang = view.findViewById(R.id.recycleviewbanhngang);
        timkiemtrangchu = view.findViewById(R.id.timkiemtrangchu);
        btntimkiem = view.findViewById(R.id.btntimkiem);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerViewngang.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getBaseContext(), 2));
        btnreset = view.findViewById(R.id.btnreset);
        swipeRefreshLayout = view.findViewById(R.id.pullToRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshdata();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Banh").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Banh> listBanhh = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Banh newbanh = new Banh(doc.getString("id"), doc.getString("ten"), doc.getString("danhmucId"), doc.getString("hinhanh"), Float.parseFloat(doc.get("gia").toString()), doc.getString("mota"));
                                listBanhh.add(newbanh);
                            }
                            BanhListAdapter adapterngang = new BanhListAdapter(context, listBanhh);
                            recyclerViewngang.setAdapter(adapterngang);

                            BanhListAdapter adapter = new BanhListAdapter(context, listBanhh);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
                tensp = "";
            }
        });

        btntimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tensp = timkiemtrangchu.getText().toString();
                firebaseFirestore.collection("Banh").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Banh> lsBanh = new ArrayList<>();

                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Banh newbanh = new Banh(doc.getString("id"), doc.getString("ten"), doc.getString("danhmuc"), doc.getString("hinhanh"), Float.parseFloat(doc.get("gia").toString()), doc.getString("mota"));
                                if (tensp.isEmpty()) lsBanh.add(newbanh);
                                else if (newbanh.getTen().toLowerCase().contains(tensp.toLowerCase())) {
                                    lsBanh.add(newbanh);
                                }
                            }
                            BanhListAdapter adapter = new BanhListAdapter(context, lsBanh);
                            recyclerView.setAdapter(adapter);
                        }
                    }

                });
            }
        });
        firebaseFirestore.collection("Sliders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {
                    List<SlideModel> im = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Slider slider = new Slider(doc.getId(), new SlideModel(doc.get("imageSlider").toString(), ScaleTypes.FIT));
                        im.add(slider.getImageSlider());
                    }

                    imgslider.setImageList(im, ScaleTypes.FIT);
                }
            }
        });

        autocomple = view.findViewById(R.id.thutugia);
        itemthutugia = new ArrayAdapter<>(context, R.layout.listitem_thutugia, itemsthutu);
        autocomple.setAdapter(itemthutugia);
        autocomple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Query.Direction direction;
                if (position == 0) {
                    direction = Query.Direction.DESCENDING;
                } else {
                    direction = Query.Direction.ASCENDING;
                }
                firebaseFirestore.collection("Banh").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Banh> listBanhh = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Banh newbanh = new Banh(doc.getString("id"), doc.getString("ten"), doc.getString("danhmuc"), doc.getString("hinhanh"), Float.parseFloat(doc.get("gia").toString()), doc.getString("mota"));
                                if (newbanh.getTen().toLowerCase().contains(tensp.toLowerCase())) {
                                    listBanhh.add(newbanh);
                                }
                            }
                            listBanhh.sort(new Comparator<Banh>() {
                                @Override
                                public int compare(Banh o1, Banh o2) {
                                    if (direction == Query.Direction.DESCENDING)
                                        return (int) (o2.getGia() - o1.getGia());
                                    else return (int) (o1.getGia() - o2.getGia());
                                }
                            });
                            BanhListAdapter adapter = new BanhListAdapter(context, listBanhh);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
            }
        });

        firebaseFirestore.collection("Banh").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Banh> listBanhh = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Banh newbanh = new Banh(doc.getString("id"), doc.getString("ten"), doc.getString("danhmuc"), doc.getString("hinhanh"), Float.parseFloat(doc.get("gia").toString()), doc.getString("mota"));
                        listBanhh.add(newbanh);
                    }
                    BanhListAdapter adapterngang = new BanhListAdapter(context, listBanhh);
                    recyclerViewngang.setAdapter(adapterngang);

                    BanhListAdapter adapter = new BanhListAdapter(context, listBanhh);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

    }


    public static TrangChuFragment newInstance() {
        TrangChuFragment fragment = new TrangChuFragment(newInstance().context);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_trang_chu, container, false);
    }

}