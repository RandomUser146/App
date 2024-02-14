package com.example.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.util.Task;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder>{

    ArrayList<Task> myValues;

    public TaskAdapter(ArrayList<Task> myValues){
        this.myValues = myValues;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card,parent,false);
        return new TaskAdapter.MyViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(myValues.get(position));
    }

    @Override
    public int getItemCount() {
        return myValues.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tName,tDesc,tStatus;
        private MaterialButton cancelButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tName = itemView.findViewById(R.id.TaskName);
            tDesc = itemView.findViewById(R.id.desc);
            tStatus = itemView.findViewById(R.id.status);
            cancelButton = itemView.findViewById(R.id.cancel_button);

            cancelButton.setOnClickListener(v->{
                Toast.makeText(cancelButton.getContext(), "Cancel", Toast.LENGTH_SHORT).show();
            });
        }

        public void bind(Task task) {
            tName.setText(task.getTaskName());
            tDesc.setText(task.getTaskDescription());
            tStatus.setText(task.getTaskStatus());
        }
    }
}
