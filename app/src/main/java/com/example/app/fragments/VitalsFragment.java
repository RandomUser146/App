package com.example.app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app.R;
import com.example.app.adapter.DashboardAdapter;
import com.example.app.adapter.VitalsAdapter;
import com.example.app.databinding.FragmentVitalsBinding;
import com.example.app.util.Constants;
import com.example.app.util.ListItem;
import com.example.app.view_models.VitalsViewModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VitalsFragment extends Fragment {

    private VitalsViewModel viewModel;
    private FragmentVitalsBinding binding;
    private RecyclerView recyclerView;
    private VitalsAdapter adapter;
    private FirebaseFunctions mFunctions;
    private final String TAG = getClass().getSimpleName();

    public VitalsFragment() {
        // Required empty public constructor
    }

    ArrayList<ListItem> getCloudData(){
        ArrayList<ListItem> dummy = new ArrayList<>();
        dummy.add(new ListItem());
        dummy.add(new ListItem());
        dummy.add(new ListItem());
        dummy.add(new ListItem());
        dummy.add(new ListItem());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rainRef = database.getReference(Constants.id+":Rain-detected:list"+"/"+Constants.id+":Rain-detected:list");

        rainRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ListItem item = new ListItem();
                ArrayList<Double> value = (ArrayList<Double>)dataSnapshot.getValue();
                assert value != null;
                int i = 0;
                ArrayList<String> x = new ArrayList<String>();
                ArrayList<String> y = new ArrayList<String>();
                for(int index = 0; index < value.size(); index++){
                    x.add(String.valueOf((index)));
                    y.add(String.valueOf(value.get(index)));
                }
                item.setItemName("Rain fall").setX(x).setY(y);
                dummy.set(0,item);
                Log.d(TAG, "Value is: " + value);
                try{
                    recyclerView.getAdapter().notifyDataSetChanged();
                }catch (Exception e){


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        DatabaseReference tempRef = database.getReference(Constants.id+":Temperature:list"+"/"+Constants.id+":Temperature:list");

        tempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ListItem item = new ListItem();
                ArrayList<Double> value = (ArrayList<Double>)dataSnapshot.getValue();
                assert value != null;
                int i = 0;
                ArrayList<String> x = new ArrayList<String>();
                ArrayList<String> y = new ArrayList<String>();
                for(int index = 0; index < value.size(); index++){
                    x.add(String.valueOf((index)));
                    y.add(String.valueOf(value.get(index)));
                }
                item.setItemName("Temperature").setX(x).setY(y);
                dummy.set(1,item);
                Log.d(TAG, "Value is: " + value);
                try{
                    recyclerView.getAdapter().notifyDataSetChanged();
                }catch (Exception e){


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        DatabaseReference soilMRef = database.getReference(Constants.id+":Soil-Moisture:list"+"/"+Constants.id+":Soil-Moisture:list");

        soilMRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ListItem item = new ListItem();
                ArrayList<Double> value = (ArrayList<Double>)dataSnapshot.getValue();
                assert value != null;
                int i = 0;
                ArrayList<String> x = new ArrayList<String>();
                ArrayList<String> y = new ArrayList<String>();
                for(int index = 0; index < value.size(); index++){
                    x.add(String.valueOf((index)));
                    y.add(String.valueOf(value.get(index)));
                }
                item.setItemName("Soil Moisture").setX(x).setY(y);
                dummy.set(2,item);
                Log.d(TAG, "Value is: " + value);
                try{
                    recyclerView.getAdapter().notifyDataSetChanged();
                }catch (Exception e){


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        DatabaseReference humidityRef = database.getReference(Constants.id+":Relative-humidity:list"+"/"+Constants.id+":Relative-humidity:list");

        humidityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ListItem item = new ListItem();
                ArrayList<Double> value = (ArrayList<Double>)dataSnapshot.getValue();
                assert value != null;
                int i = 0;
                ArrayList<String> x = new ArrayList<String>();
                ArrayList<String> y = new ArrayList<String>();
                for(int index = 0; index < value.size(); index++){
                    x.add(String.valueOf((index)));
                    y.add(String.valueOf(value.get(index)));
                }
                item.setItemName("Relative-Humidity").setX(x).setY(y);
                dummy.set(3,item);
                Log.d(TAG, "Value is: " + value);
                try{
                    recyclerView.getAdapter().notifyDataSetChanged();
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        DatabaseReference pHRef = database.getReference(Constants.id+":pH-level:list"+"/"+Constants.id+":pH-level:list");

        pHRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ListItem item = new ListItem();
                ArrayList<Double> value = (ArrayList<Double>)dataSnapshot.getValue();
                assert value != null;
                int i = 0;
                ArrayList<String> x = new ArrayList<String>();
                ArrayList<String> y = new ArrayList<String>();
                for(int index = 0; index < value.size(); index++){
                    x.add(String.valueOf((index)));
                    y.add(String.valueOf(value.get(index)));
                }
                item.setItemName("Soil pH Level").setX(x).setY(y);
                dummy.set(4,item);
                Log.d(TAG, "Value is: " + value);
                try{
                    recyclerView.getAdapter().notifyDataSetChanged();
                }catch (Exception e){


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return dummy;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(viewModel == null) {
            viewModel = new VitalsViewModel();
        }
        binding = FragmentVitalsBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        //TODO
        
        recyclerView = binding.recyclerview;
        adapter = new VitalsAdapter(getCloudData());
        recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        recyclerView.setAdapter(adapter);
        mFunctions = FirebaseFunctions.getInstance();
        String inputMessage = "hello";
        addMessage(inputMessage)
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                            }
                        }
                    }
                });
        return binding.getRoot();
    }

    private Task<String> addMessage(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("addMessage")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        return (String) task.getResult().getData();
                    }
                });
    }

}