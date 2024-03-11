package com.example.app.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app.databinding.FragmentVitalsBinding;
import com.example.app.util.Constants;
import com.example.app.util.ListItem;
import com.example.app.view_models.VitalsViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class VitalsFragment extends Fragment {
    private VitalsViewModel viewModel;
    private FragmentVitalsBinding binding;
    private ArrayList<ListItem> dataset = new ArrayList<>();

    private MaterialTextView rain, pendingTasks, completedTasks, dispensedWater, dispensedFertilizer;
    private LineChart chart;
    private final String TAG = getClass().getSimpleName();

    public VitalsFragment() {
        // Required empty public constructor
    }

    private void drawChart() {
        chart.setViewPortOffsets(0, 0, 0, 0);
        chart.setBackgroundColor(Color.WHITE);
        chart.setGridBackgroundColor(Color.BLACK);
        //Color.rgb(150,213,166);
        chart.setDrawGridBackground(true);

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        chart.setDrawGridBackground(true);
        chart.setMaxHighlightDistance(300);

        XAxis x = chart.getXAxis();
        x.setEnabled(false);
        x.setLabelCount(6, false);
        x.setTextColor(Color.WHITE);
        x.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        x.setDrawGridLines(false);
        x.setAxisLineColor(Color.WHITE);

        YAxis y = chart.getAxisLeft();
        y.setLabelCount(4, false);
        y.setTextColor(Color.WHITE);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.WHITE);

        chart.getAxisRight().setEnabled(false);

        chart.getLegend().setEnabled(true);

        LegendEntry[] legend = new LegendEntry[4];
        legend[0] = new LegendEntry();
        legend[0].label = "pH";
        legend[0].formColor = ColorTemplate.VORDIPLOM_COLORS[0];
        legend[1] = new LegendEntry();
        legend[1].label = "Temperature";
        legend[1].formColor = ColorTemplate.VORDIPLOM_COLORS[1];
        legend[2] = new LegendEntry();
        legend[2].label = "Soil Moisture";
        legend[2].formColor = ColorTemplate.VORDIPLOM_COLORS[2];
        legend[3] = new LegendEntry();
        legend[3].label = "Humidity";
        legend[3].formColor = ColorTemplate.VORDIPLOM_COLORS[3];

        chart.getLegend().setCustom(new LegendEntry[]{
                legend[0],
                legend[1],
                legend[2],
                legend[3]
        });

        chart.animateXY(1000, 1000);

        // don't forget to refresh the drawing
        chart.invalidate();
    }

    private void setData(ListItem item, String label, int index) {
        if (label == null || label.length() == 0) {
            label = "Loading";
        }

        Log.e(TAG, "setData :" + label);
        ArrayList<Entry> values = new ArrayList<>();
        float x_, y_;
        for (int i = 0; i < item.getX().size(); i++) {
            x_ = Float.parseFloat(item.getX().get(i));
            y_ = Float.parseFloat(item.getY().get(i));
            values.add(new Entry(x_, y_));
            Log.e("Val", x_ + " " + y_);
        }
        LineDataSet set1;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(index);
            if (set1 != null) {
                set1.setLabel(label);
                set1.setValues(values);
                set1.setMode(LineDataSet.Mode.STEPPED);
                set1.setCubicIntensity(0.05f);
                set1.setDrawFilled(false);
                set1.setDrawCircles(false);
                set1.setLineWidth(1.8f);
                set1.setCircleRadius(4f);
                set1.setCircleColor(Color.RED);
                set1.setHighLightColor(Color.BLUE);
                set1.setColor(Color.BLACK);
                set1.setFillColor(Color.BLUE);
                set1.setFillAlpha(100);
                set1.setDrawHorizontalHighlightIndicator(false);
                set1.setFillFormatter((dataSet, dataProvider) -> chart.getAxisLeft().getAxisMinimum());
                set1.setColor(Color.BLACK);
                ColorDrawable colorDrawable = new ColorDrawable(Color.rgb(150, 213, 166));
                set1.setFillDrawable(colorDrawable);
                set1.setDrawFilled(true);
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            } else {
                set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
                set1.setLabel(label);
                set1.setValues(values);
                set1.setMode(LineDataSet.Mode.STEPPED);
                set1.setCubicIntensity(0.05f);
                set1.setDrawFilled(false);
                set1.setDrawCircles(false);
                set1.setLineWidth(1.8f);
                set1.setCircleRadius(4f);
                set1.setCircleColor(Color.RED);
                set1.setHighLightColor(Color.BLUE);
                set1.setColor(Color.BLACK);
                set1.setFillColor(Color.BLUE);
                set1.setFillAlpha(100);
                set1.setDrawHorizontalHighlightIndicator(false);
                set1.setFillFormatter((dataSet, dataProvider) -> chart.getAxisLeft().getAxisMinimum());
                set1.setColor(Color.BLACK);
                ColorDrawable colorDrawable = new ColorDrawable(Color.rgb(150, 213, 166));
                set1.setFillDrawable(colorDrawable);
                set1.setDrawFilled(true);
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            }
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, label);

            set1.setMode(LineDataSet.Mode.STEPPED);
            set1.setCubicIntensity(0.05f);
            set1.setDrawFilled(false);
            set1.setDrawCircles(false);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.RED);
            set1.setHighLightColor(Color.BLUE);
            set1.setColor(Color.BLACK);
            set1.setFillColor(Color.BLUE);
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter((dataSet, dataProvider) -> chart.getAxisLeft().getAxisMinimum());
            set1.setColor(Color.BLACK);
            ColorDrawable colorDrawable = new ColorDrawable(Color.rgb(150, 213, 166));
            set1.setFillDrawable(colorDrawable);
            set1.setDrawFilled(true);
            LineData data = new LineData(set1);
            data.setValueTextSize(9f);
            data.setDrawValues(false);
            chart.setData(data);
        }
    }

    private final int[] colors = new int[] {
            ColorTemplate.VORDIPLOM_COLORS[0],
            ColorTemplate.VORDIPLOM_COLORS[1],
            ColorTemplate.VORDIPLOM_COLORS[2],
            ColorTemplate.VORDIPLOM_COLORS[3]
    };

    private void setDataset() {

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        for (int z = dataset.size()-1; z >= 0; z--) {

            ArrayList<Entry> values = new ArrayList<>();

            Log.e(TAG, "setDataset :"+ dataset.get(z).getY());
            Log.e(TAG, "setDataset Name :"+ dataset.get(z).getItemName());

            for (int i = 0; i < dataset.get(z).getX().size(); i++) {
                values.add(new Entry(i, (float) Float.parseFloat(dataset.get(z).getY().get(i))));
                //values.add(new Entry(i, (float)Math.random()*100));
            }

            LineDataSet d = new LineDataSet(values, dataset.get(z).getItemName());
            d.setLineWidth(2.5f);
            d.setCircleRadius(4f);

            int color = colors[z];
            d.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            d.setCubicIntensity(0.05f);
            d.setDrawCircles(false);
            d.setDrawFilled(true);
            d.setHighLightColor(Color.WHITE);
            d.setColor(Color.BLACK);
            d.setFillColor(Color.BLUE);
            d.setFillAlpha(100);
            d.setDrawHorizontalHighlightIndicator(false);
            d.setColor(Color.BLACK);
            ColorDrawable colorDrawable = new ColorDrawable(color);//Color.rgb(150, 213, 166));
            d.setFillDrawable(colorDrawable);
            d.setDrawFilled(true);
            dataSets.add(d);
        }

        LineData data = new LineData(dataSets);
        chart.setData(data);
        chart.invalidate();
    }


    private void getCloudData() {
        if (dataset == null) {
            dataset = new ArrayList<>();
        }
        if (dataset.size() < 1) {
            dataset.add(new ListItem()); //ph
            dataset.add(new ListItem()); //Temp
            dataset.add(new ListItem()); //Soil Moist
            dataset.add(new ListItem()); // Humidity
        }

//        ListItem item = new ListItem();
//        int i = 0;
//        ArrayList<String> x = new ArrayList<String>();
//        ArrayList<String> y = new ArrayList<String>();
//        for (int index = 0; index < 24; index++) {
//            x.add(String.valueOf((index)));
//            y.add(String.valueOf(Math.random()*50));
//        }
//        item.setItemName("Soil Moisture").setX(x).setY(y);
//        dataset.set(2, item);
//        setDataset();
//        ListItem item2 = new ListItem();
//        i = 0;
//        ArrayList<String> xx = new ArrayList<String>();
//        ArrayList<String> yy = new ArrayList<String>();
//        for (int index = 0; index < 24; index++) {
//            xx.add(String.valueOf((index)));
//            yy.add(String.valueOf(Math.random()*7.5));
//        }
//        item2.setItemName("Soil pH Level").setX(xx).setY(yy);
//        dataset.set(0, item2);
//        setDataset();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tempRef = database.getReference(Constants.id + ":Temperature:list" + "/" + Constants.id + ":Temperature:list");
        tempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListItem item = new ListItem();
                ArrayList<Double> value = (ArrayList<Double>) dataSnapshot.getValue();
                if (value == null) return;
                int i = 0;
                ArrayList<String> x = new ArrayList<String>();
                ArrayList<String> y = new ArrayList<String>();
                for (int index = 0; index < value.size(); index++) {
                    x.add(String.valueOf((index)));
                    y.add(String.valueOf(value.get(index)));
                }
//                for (int index = 0; index < 24; index++) {
//                    x.add(String.valueOf((index)));
//                    y.add(String.valueOf(Math.random()*40));
//                }
                item.setItemName("Temperature").setX(x).setY(y);
                dataset.set(1, item);
                setDataset();
                Log.d(TAG, "Temp Value is: " + y.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        DatabaseReference humidityRef = database.getReference(Constants.id + ":Relative-humidity:list" + "/" + Constants.id + ":Relative-humidity:list");
        humidityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ListItem item = new ListItem();
                ArrayList<Double> value = (ArrayList<Double>) dataSnapshot.getValue();
                if (value == null) return;
                int i = 0;
                ArrayList<String> x = new ArrayList<String>();
                ArrayList<String> y = new ArrayList<String>();
                for (int index = 0; index < value.size(); index++) {
                    x.add(String.valueOf((index)));
                    y.add(String.valueOf(value.get(index)));
                }
//                for (int index = 0; index < 24; index++) {
//                    x.add(String.valueOf((index)));
//                    y.add(String.valueOf(Math.random()*90));
//                }
                item.setItemName("Relative-Humidity").setX(x).setY(y);
                dataset.set(3, item);
                setDataset();
                Log.d(TAG, "Relative Hum Value is: " + y.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        DatabaseReference soilMRef = database.getReference(Constants.id + ":Soil-Moisture:list" + "/" + Constants.id + ":Soil-Moisture:list");
        soilMRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ListItem item = new ListItem();
                ArrayList<Long> value = (ArrayList<Long>) dataSnapshot.getValue();
                if (value == null) return;
                int i = 0;
                ArrayList<String> x = new ArrayList<>();
                ArrayList<String> y = new ArrayList<>();
                for (int index = 0; index < value.size(); index++) {
                    x.add(String.valueOf((index)));
                    y.add(String.valueOf(value.get(index)/100));
                }
                item.setItemName("Soil Moisture").setX(x).setY(y);
                dataset.set(2, item);
                setDataset();
                Log.d(TAG, "SM Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        DatabaseReference pHRef = database.getReference(Constants.id + ":pH-level:list" + "/" + Constants.id + ":pH-level:list");
        pHRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ListItem item = new ListItem();
                ArrayList<Double> value = (ArrayList<Double>) dataSnapshot.getValue();
                if (value == null) return;
                int i = 0;
                ArrayList<String> x = new ArrayList<String>();
                ArrayList<String> y = new ArrayList<String>();
                for (int index = 0; index < value.size(); index++) {
                    x.add(String.valueOf((index)));
                    y.add(String.valueOf(value.get(index)));
                }
                item.setItemName("Soil pH Level").setX(x).setY(y);
                dataset.set(0, item);
                setDataset();
                Log.d(TAG, "pH Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        ////////////////////////////////////////////////////////////////////////////////////////////

        AtomicInteger pendingTasks = new AtomicInteger(0);
        AtomicInteger completedTasks = new AtomicInteger(0);
        AtomicInteger dispensedWater = new AtomicInteger(0);
        AtomicInteger dispensedFertilizer = new AtomicInteger(0);

        FirebaseDatabase.getInstance().getReference(Constants.id + ":newTasks" + "/").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Object> value = (ArrayList<Object>) task.getResult().getValue();
                if(value == null) return;
                pendingTasks.set(value.size());
                requireActivity().runOnUiThread(()->{
                    String message = "Pending tasks : "+pendingTasks.get();
                    binding.x1.setText(message);
                });
            }
        });
        FirebaseDatabase.getInstance().getReference(Constants.id + ":completedTasks" + "/").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Object> value = (ArrayList<Object>) task.getResult().getValue();
                if(value == null) return;
                completedTasks.set(value.size());
                requireActivity().runOnUiThread(()->{
                    String message = "Pending tasks : "+completedTasks.get();
                    binding.x1.setText(message);
                });
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (viewModel == null) {
            viewModel = new VitalsViewModel();
        }
        binding = FragmentVitalsBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        chart = binding.sgrpah;
        rain = binding.t2;
        pendingTasks = binding.x1;
        completedTasks = binding.x2;
        dispensedWater = binding.x3;
        dispensedFertilizer = binding.x4;
        drawChart();
        getCloudData();
        FirebaseDatabase.getInstance().getReference("/" + Constants.id + ":rain").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val = "";
                if (snapshot.exists()) {
                    val = String.valueOf((Double) snapshot.getValue(Double.class));
                } else {
                    val = "";
                }
                String finalVal = val;
                requireActivity().runOnUiThread(() -> {
                    final String t = finalVal + "mm";
                    binding.t2.setText(t);
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}