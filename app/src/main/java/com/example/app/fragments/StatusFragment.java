package com.example.app.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app.R;
import com.example.app.databinding.FragmentStatusBinding;
import com.example.app.util.Constants;
import com.example.app.view_models.StatusViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StatusFragment extends Fragment {

    StatusViewModel viewModel;
    FragmentStatusBinding binding;
    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStatusBinding.inflate(inflater,container,false);
        if(viewModel == null)
            viewModel = new StatusViewModel();
        binding.setViewModel(viewModel);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tRef = database.getReference(Constants.id + ":time" + "/");
        tRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long value = (Long) snapshot.getValue();
                if(value == null)
                    return;
                java.util.Date time = new java.util.Date((long) value * 1000);
                new Handler().post(()->{
                    final String s = "Last contacted time : " + String.valueOf(time.toString());
                    binding.time.setText(s);
                    binding.status.setText("Status : Active");
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.delete.setOnClickListener(v->{
            SharedPreferences sh = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            String mail = sh.getString("mail", "");
            mail = mail.replace("@", "at");
            mail = mail.replace(".","");
            mail = mail+"_systems";

            Log.e("Delete", mail);
            FirebaseDatabase.getInstance().getReference("/"+mail).addListenerForSingleValueEvent(new ValueEventListener(){

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        snapshot.getRef().removeValue();
                    }else{
                        Log.e("Delete","no snapshot");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Delete","cancelled");
                }
            });
        });

        binding.name.setText("SID :"+Constants.id);
        return binding.getRoot();
    }
}