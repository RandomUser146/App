package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.example.app.adapter.SystemsAdapter;
import com.example.app.databinding.ActivitySystemDashboardBinding;
import com.example.app.util.SystemItem;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class SystemDashboard extends AppCompatActivity {

    SystemsAdapter adapter;
    ActivitySystemDashboardBinding binding;

    private ArrayList<SystemItem> getSystems(){
        ArrayList<SystemItem> list = new ArrayList<SystemItem>();
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String mail = sh.getString("mail", "");
        mail = mail.replace("@", "at");
        mail = mail.replace(".","");
        Log.e("Result","mail = " + mail);
        FirebaseDatabase.getInstance().getReference("/"+mail+"_systems").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.e("Result ",snapshot.getValue().toString());
                    FirebaseDatabase.getInstance().getReference("/"+snapshot.getValue()+"_loc").addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            if(snapshot.exists()){
                                final String location = (String) snapshot2.getValue();
                                Log.e("Result val ",snapshot2.getValue().toString());
                                //list.add(new SystemItem(name, location, status));
                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                //myEdit.putString("name", name);
                                myEdit.commit();
                                list.add(new SystemItem((String) snapshot.getValue(), location, "Active"));
                                runOnUiThread(()->{
                                    adapter = new SystemsAdapter(list);
                                    binding.recyclerviewDashboard.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
                                    binding.recyclerviewDashboard.setAdapter(adapter);
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    Log.e("Result ","No data");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return list;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SystemsAdapter(getSystems());
        binding = ActivitySystemDashboardBinding.inflate(getLayoutInflater());
        binding.recyclerviewDashboard.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recyclerviewDashboard.setAdapter(adapter);
        binding.topAppBarDashboard.setNavigationOnClickListener(v -> binding.dDashboard.openDrawer(GravityCompat.START));
        binding.drawerMain2.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.logOut1){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getBaseContext(), AuthActivity.class);
                startActivity(intent);
                finish();
            }
            binding.dDashboard.closeDrawer(GravityCompat.START);
            return true;
        });
        binding.newDashboard.setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(),SystemSetup.class);
            startActivity(intent);
        });
        setContentView(binding.getRoot());
    }
}