package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.app.adapter.SystemsAdapter;
import com.example.app.databinding.ActivityNewTaskBinding;
import com.example.app.util.Constants;
import com.example.app.util.SystemItem;
import com.example.app.util.TaskObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class NewTaskActivity extends AppCompatActivity {

    ActivityNewTaskBinding binding;

    int taskTypeNumber = -1;
    long timestamp = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewTaskBinding.inflate(getLayoutInflater());
        final String[] type = {"Irrigation","Fertilization"};
        ArrayAdapter<String> taskType = new ArrayAdapter<>(getApplicationContext(),R.layout.item,type);
        binding.taskTypeField.setAdapter(taskType);
        ArrayAdapter<String> fertilzers = new ArrayAdapter<>(getApplicationContext(),R.layout.item,Constants.fertilizers);
        binding.fertilizerTypeField.setAdapter(fertilzers);
        binding.a.setOnClickListener(v -> {
            //String taskName = binding.taskNameField.getText().toString();
            String taskA = binding.amountField.getText().toString();
            if(taskA.equals("")){
                Toast.makeText(NewTaskActivity.this, "Please enter the amount", Toast.LENGTH_SHORT).show();
                return;
            }
            if(taskTypeNumber == -1){
                Toast.makeText(NewTaskActivity.this, "Please select the task type", Toast.LENGTH_SHORT).show();
                return;
            }
            if(timestamp == -1){
                Toast.makeText(NewTaskActivity.this, "Please enter the date", Toast.LENGTH_SHORT).show();
                return;
            }

            if(taskTypeNumber == 0){
                taskA = String.valueOf(Integer.parseInt(taskA) * 60);
            }
            int taskAmount = Integer.parseInt(taskA);
            TaskObject taskObject = new TaskObject((int) System.currentTimeMillis(),taskAmount,timestamp,taskTypeNumber);
            SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            String mail = sh.getString("mail", "");
            mail = mail.replace("@", "at");
            mail = mail.replace(".","");
            Log.e("Result","mail = " + mail);
            ArrayList<TaskObject> list = new ArrayList<>();
            list.add(taskObject);
            //FirebaseDatabase.getInstance().getReference("/"+ Constants.id +":newTasks").setValue(list);
            DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference(Constants.id + ":newTasks" + "/");
            taskRef.get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    ArrayList<Object> value = (ArrayList<Object>) task.getResult().getValue();
                    if(value == null){
                        taskRef.setValue(list);
                        return;
                    }
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
                        list.add(taskItem);
                    }
                    taskRef.setValue(list);
                }
            });
            finish();
        });
        binding.c.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),"Cancelling", Toast.LENGTH_SHORT).show();
            finish();
        });
        binding.time.setOnClickListener(v -> {
            showDatePickerDialog();
        });

        binding.taskTypeField.setOnItemClickListener(((parent, view, position, id) -> {
            taskTypeNumber = position;
            if(position == 0){
                binding.fertilizerType.setVisibility(View.GONE);
                binding.amountField.setHint("Enter the pumping duration in minutes");
            }else if(position == 1){
                binding.fertilizerType.setVisibility(View.VISIBLE);
                binding.amountField.setHint("Enter the Fertilizer in Kgs");
            }else{
                binding.fertilizerType.setVisibility(View.GONE);
                binding.amountField.setHint("Enter the pumping duration in minutes");
            }
        }));
        setContentView(binding.getRoot());
    }

    private void showDatePickerDialog(){
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();

        builder.setTitleText("Select start date");
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());

        final MaterialDatePicker materialDatePicker = builder.build();

        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                String date_ = materialDatePicker.getHeaderText();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
                try {
                    LocalDate date = LocalDate.parse(date_, formatter);
                    ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
                    timestamp = date.atStartOfDay(zoneId).toEpochSecond();
                    Log.e("Timestamp", String.valueOf(timestamp));
                }catch (Exception e){
                    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
                    LocalDate date = LocalDate.parse(date_, formatter2);
                    ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
                    timestamp = date.atStartOfDay(zoneId).toEpochSecond();
                    Log.e("Exception Timestamp", String.valueOf(timestamp));
                }
            }
        });
    }
}