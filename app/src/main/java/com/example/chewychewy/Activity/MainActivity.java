package com.example.chewychewy.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.chewychewy.R;
import com.example.chewychewy.databinding.ActivityMainBinding;
import com.example.chewychewy.fragment.DanhMucFragment;
import com.example.chewychewy.fragment.GioHangFragment;
import com.example.chewychewy.fragment.TaiKhoanFragment;
import com.example.chewychewy.fragment.TrangChuFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;

    public static Activity main;
    ActivityMainBinding binding;
        private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        main = this;
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.navmenu);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replace(new TrangChuFragment(MainActivity.this));
        binding.navmenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.trangchu: replace(new TrangChuFragment(MainActivity.this)); break;
                    case R.id.taikhoan:replace(new TaiKhoanFragment(MainActivity.this));break;
                    case R.id.giohang:replace(new GioHangFragment(MainActivity.this)); break;
                    case R.id.danhmuc:replace(new DanhMucFragment(MainActivity.this)); break;
                }
                return true;
            }
        });




    }
    private void replace(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.relativelayout,fragment);
        fragmentTransaction.commit();
    }


}
