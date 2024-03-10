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
import com.example.app.adapter.PendingTasksAdapter;
import com.example.app.adapter.TaskAdapter;
import com.example.app.databinding.FragmentPendingTasksBinding;
import com.example.app.databinding.FragmentTasksBinding;
import com.example.app.util.Constants;
import com.example.app.util.TaskObject;
import com.example.app.view_models.TasksViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

public class PendingTasks extends Fragment {

    TasksViewModel viewModel;
    FragmentPendingTasksBinding binding;
    private RecyclerView recyclerView;
    private PendingTasksAdapter adapter;
    public final String TAG = "PendingTasksFragment";
    public PendingTasks() {
        // Required empty public constructor
    }

    private ArrayList<TaskObject> getTasksFromCloud() {
        ArrayList<TaskObject> values = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference taskRef = database.getReference(Constants.id + ":newTasks" + "/");
        taskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                values.clear();
                ArrayList<Object> value = (ArrayList<Object>) dataSnapshot.getValue();
                if(value == null) {
                    try {
                        recyclerView.getAdapter().notifyDataSetChanged();
                    } catch (Exception e) {


                    }
                    return;
                }
                String description = "";
                Log.e(TAG, "tasks :" + value);
                for(int i = 0; i < value.size(); i++){
                    TaskObject task = new TaskObject();
                    Map<String,Object> m = (Map<String, Object>) value.get(i);
                    for (Map.Entry<String, Object> entry : m.entrySet()) {
                        if (Objects.equals(entry.getKey(), "amount")) {
                            task.amount = (int) Long.parseLong(String.valueOf(entry.getValue()));
                        } else if (Objects.equals(entry.getKey(), "scheduledTime")) {
                            task.scheduledTime = ((Long) entry.getValue());
                            //Log.e("timestamps", String.valueOf(task.scheduledTime));
                        } else if (Objects.equals(entry.getKey(), "task")) {
                            task.task = Integer.parseInt(String.valueOf(entry.getValue()));
                        } else if(Objects.equals(entry.getKey(), "taskid")){
                            task.taskid = Integer.parseInt(String.valueOf(entry.getValue()));
                        }else if(Objects.equals(entry.getKey(), "taskStatus")){
                            task.taskStatus = Integer.parseInt(String.valueOf(entry.getValue()));
                        }
                    }
                    values.add(task);
                }
                //Log.d(TAG, "PendinValue is: " + values.toString());
                try {
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (Exception e) {


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        values.sort((o1, o2) -> {
//            if (o1.scheduledTime == o2.scheduledTime)
//                return 0;
//            return o1.scheduledTime < o2.scheduledTime ? -1 : 1;
//        });
        return values;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (viewModel == null)
            viewModel = new TasksViewModel();
        binding = FragmentPendingTasksBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        recyclerView = binding.recyclerview;
        adapter = new PendingTasksAdapter(getTasksFromCloud());
        recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }
}