package com.example.app.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.util.Constants;
import com.example.app.util.TaskObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class PendingTasksAdapter extends RecyclerView.Adapter<PendingTasksAdapter.MyViewHolder> {

    public ArrayList<TaskObject> myValues;

    public PendingTasksAdapter(ArrayList<TaskObject> myValues) {
        this.myValues = myValues;
    }

    @NonNull
    @Override
    public PendingTasksAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        return new PendingTasksAdapter.MyViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingTasksAdapter.MyViewHolder holder, int position) {
        holder.bind(myValues.get(position));
    }

    @Override
    public int getItemCount() {
        return myValues.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tName, tAmount, tTime, tStatus;
        private MaterialButton cancelButton;
        private long scheduledTime = -100;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tName = itemView.findViewById(R.id.TaskName);
            tAmount = itemView.findViewById(R.id.Amount);
            tTime = itemView.findViewById(R.id.taskTime);
            tStatus = itemView.findViewById(R.id.status);
            cancelButton = itemView.findViewById(R.id.cancel_button);
            cancelButton.setOnClickListener(v -> {
                Toast.makeText(cancelButton.getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference(Constants.id + ":newTasks" + "/");
                DatabaseReference cancelledTasks = FirebaseDatabase.getInstance().getReference(Constants.id + ":cancelledTasks" + "/");
                taskRef.get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        ArrayList<TaskObject> values = new ArrayList<>();
                        ArrayList<TaskObject> removedValues = new ArrayList<>();
                        ArrayList<Object> value = (ArrayList<Object>) task.getResult().getValue();
                        if(value == null) return;
                        Log.e("firebase", "tasks :" + value);
                        for(int i = 0; i < value.size(); i++){
                            TaskObject taskItem = new TaskObject();
                            Map<String,Object> m = (Map<String, Object>) value.get(i);
                            Log.e("firebase",m.toString());
                            for (Map.Entry<String, Object> entry : m.entrySet()) {
                                if (Objects.equals(entry.getKey(), "amount")) {
                                    taskItem.amount = (int) Long.parseLong(String.valueOf(entry.getValue()));
                                } else if (Objects.equals(entry.getKey(), "scheduledTime")) {
                                    taskItem.scheduledTime = ((Long) entry.getValue());
                                    //Log.e("firebase",  "sc task sched:" + taskItem.scheduledTime);
                                } else if (Objects.equals(entry.getKey(), "task")) {
                                    taskItem.task = Integer.parseInt(String.valueOf(entry.getValue()));
                                } else if(Objects.equals(entry.getKey(), "taskid")){
                                    taskItem.taskid = Integer.parseInt(String.valueOf(entry.getValue()));
                                }else if(Objects.equals(entry.getKey(), "taskStatus")){
                                    taskItem.taskStatus = Integer.parseInt(String.valueOf(entry.getValue()));
                                }
                            }
                            if(scheduledTime != taskItem.scheduledTime){
                                values.add(taskItem);
                            }else{
                                Log.e("firebase duplicate", "sc :"+scheduledTime + " sc task :" + taskItem.scheduledTime);
                                removedValues.add(taskItem);
                            }
                        }
                        taskRef.setValue(values);
                        cancelledTasks.get().addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                ArrayList<Object> result = (ArrayList<Object>) task1.getResult().getValue();
                                if(result == null) {
                                    Log.e("firebase cancel", removedValues.toString());
                                    cancelledTasks.setValue(removedValues);
                                    return;
                                }
                                for(int i = 0; i < result.size(); i++){
                                    TaskObject taskItem = new TaskObject();
                                    Map<String,Object> m = (Map<String, Object>) result.get(i);
                                    for (Map.Entry<String, Object> entry : m.entrySet()) {
                                        if (Objects.equals(entry.getKey(), "amount")) {
                                            taskItem.amount = (int) Long.parseLong(String.valueOf(entry.getValue()));
                                        } else if (Objects.equals(entry.getKey(), "scheduledTime")) {
                                            taskItem.scheduledTime = ((Long) entry.getValue());
                                        } else if (Objects.equals(entry.getKey(), "task")) {
                                            taskItem.task = Integer.parseInt(String.valueOf(entry.getValue()));
                                        } else if(Objects.equals(entry.getKey(), "taskid")){
                                            taskItem.taskid = Integer.parseInt(String.valueOf(entry.getValue()));
                                        }else if(Objects.equals(entry.getKey(), "taskStatus")){
                                            taskItem.taskStatus = Integer.parseInt(String.valueOf(entry.getValue()));
                                        }
                                    }
                                    removedValues.add(taskItem);
                                }
                                Log.e("firebase cancel", removedValues.toString());
                                cancelledTasks.setValue(removedValues);
                            }
                        });
                    }
                });
            });
        }

        public void bind(TaskObject task) {
            tName.setText(task.task == 0 ? "Irrigation" : "Fertilization");
            final String amount = "Amount = " + task.amount + " Liters";
            tAmount.setText(amount);
            Instant instant = Instant.ofEpochMilli(task.scheduledTime * 1000);
            scheduledTime = task.scheduledTime;
            Log.e("timestampBind", "scheduledTime :"+scheduledTime);
            ZoneId zoneId = ZoneId.systemDefault(); // Use the system default time zone
            LocalDate localDate = instant.atZone(zoneId).toLocalDate();
            final String time = "Scheduled Date = " + localDate.toString();
            tTime.setText(time);
            final String status = "Status = " + Constants.taskStatus[task.taskStatus];
            tStatus.setText(status);
        }
    }
}
