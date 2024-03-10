package com.example.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.MainActivity;
import com.example.app.R;
import com.example.app.util.Constants;
import com.example.app.util.SystemItem;
import com.example.app.util.Task;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class SystemsAdapter extends RecyclerView.Adapter<SystemsAdapter.MyViewHolder>{

    ArrayList<SystemItem> myValues;

    public SystemsAdapter(ArrayList<SystemItem> myValues){
        this.myValues = myValues;
    }

    @NonNull
    @Override
    public SystemsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.system_card,parent,false);
        return new SystemsAdapter.MyViewHolder(inflater);
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

        private TextView name,loc,status;
        private MaterialButton select;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.system_name);
            loc = itemView.findViewById(R.id.system_loc);
            status = itemView.findViewById(R.id.system_active);
            select = itemView.findViewById(R.id.select);
        }

        public void bind(SystemItem item) {
            name.setText(item.getSystemName());
            loc.setText(item.getSystemLocation());
            status.setText(item.getSystemStatus());
            select.setOnClickListener(v->{
                Intent intent = new Intent(itemView.getContext(), MainActivity.class);
                intent.putExtra("name",item.getSystemName());
                Constants.id = item.getSystemName();
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
