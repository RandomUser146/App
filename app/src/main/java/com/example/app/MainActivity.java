package com.example.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.example.app.adapter.DashboardAdapter;
import com.example.app.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    TabLayout tabLayout;
    ActivityMainBinding binding;
    DashboardAdapter adapter;

    final String[] tabItems = {"KPIs", "Tasks","Pending Tasks", "Status"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        adapter = new DashboardAdapter(this);
        tabLayout = binding.tab;
        viewPager = binding.viewPager;
        binding.viewPager.setAdapter(adapter);
        viewPager.setSaveEnabled(false);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabItems[position]));
        tabLayoutMediator.attach();
        setContentView(binding.getRoot());
        binding.topAppBar.setNavigationOnClickListener(v -> binding.d.openDrawer(GravityCompat.START));
        binding.drawerMain.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.logOut1){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                startActivity(intent);
                finish();
            }
            binding.d.closeDrawer(GravityCompat.START);
            return true;
        });

        binding.newTask.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NewTaskActivity.class);
            startActivity(intent);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }
}