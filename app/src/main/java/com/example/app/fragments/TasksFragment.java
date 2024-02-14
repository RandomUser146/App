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
import com.example.app.adapter.TaskAdapter;
import com.example.app.adapter.VitalsAdapter;
import com.example.app.databinding.FragmentTasksBinding;
import com.example.app.databinding.FragmentVitalsBinding;
import com.example.app.util.Constants;
import com.example.app.util.ListItem;
import com.example.app.util.Task;
import com.example.app.view_models.TasksViewModel;
import com.example.app.view_models.VitalsViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TasksFragment extends Fragment {

    TasksViewModel viewModel;
    FragmentTasksBinding binding;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    public final String TAG = "TasksFragment";

    private ArrayList<Task> createDummy() {
        ArrayList<Task> values = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Task t = new Task("jihj", "hcijshif", "dhsfjhj", "dhsjfhdbfj", "sjkfjhk");
            values.add(t);
        }
        return values;
    }

    private ArrayList<Task> getTasksFromCloud() {
        ArrayList<Task> values = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference taskRef = database.getReference(Constants.id + ":taskQueue" + "/");
        taskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<Object> item = new ArrayList<>();
                ArrayList<Object> value = (ArrayList<Object>) dataSnapshot.getValue();
                assert value != null;
                Task task = new Task();
                String description = "";
                Log.e(TAG, "tasks :" + value);
                for(int i = 0; i < value.size(); i++){
                    Map<String,Object> m = (Map<String, Object>) value.get(i);
                    for (Map.Entry<String, Object> entry : m.entrySet()) {
                        if (Objects.equals(entry.getKey(), "amount")) {
                            String amount = String.valueOf((Long) entry.getValue());
                            description = description.concat("Amount to be dispensed : " + amount);
                            description = description.concat("\n");
                        } else if (Objects.equals(entry.getKey(), "scheduledTime")) {
                            long timeStamp = (Long) entry.getValue();
                            java.util.Date time = new java.util.Date((long) timeStamp * 1000);
                            task = task.setTaskDate(time.toString());
                        } else if (Objects.equals(entry.getKey(), "task")) {
                            if (entry.getValue() == "0") {
                                description = description.concat("Task type is : " + "Irrigation");
                                description = description.concat("\n");
                                task = task.setTaskName("Irrigation");
                            } else {
                                description = description.concat("Task type is : " + "Fertilization");
                                description = description.concat("\n");
                                task = task.setTaskName("Fertilization");
                            }
                        } else if (Objects.equals(entry.getKey(), "taskid")) {
                            String taskid = String.valueOf((Long) entry.getValue());
                            description = description.concat("Task id is : " + taskid);
                            description = description.concat("\n");
                        }
                        task = task.setTaskDescription(description).setTaskStatus("Running").setTaskStatus("Active");
                    }
                }

                values.add(task);
                Log.d(TAG, "Value is: " + values);
                try {
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (Exception e) {


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return values;
    }


    public TasksFragment() {
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
        if (viewModel == null)
            viewModel = new TasksViewModel();
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        recyclerView = binding.recyclerview;
        adapter = new TaskAdapter(getTasksFromCloud());
        recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }
}