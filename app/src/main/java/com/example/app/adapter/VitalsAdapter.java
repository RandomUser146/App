package com.example.app.adapter;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.util.ListItem;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VitalsAdapter extends RecyclerView.Adapter<VitalsAdapter.ViewHolder>{
    public ArrayList<ListItem> myValues;
    public VitalsAdapter (ArrayList<ListItem> myValues){
        this.myValues= myValues;
    }

    @NonNull
    @Override
    public VitalsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.graph_card,parent,false);
        return new ViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull VitalsAdapter.ViewHolder holder, int position) {
        holder.bind(myValues.get(position));
    }

    @Override
    public int getItemCount() {
        return myValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        private final LineChart chart;
        public ViewHolder(View itemView) {
            super(itemView);

            tv = itemView.findViewById(R.id.t);
            chart = itemView.findViewById(R.id.chart1);
            chart.setViewPortOffsets(0, 0, 0, 0);
            chart.setBackgroundColor(Color.BLACK);
            chart.setGridBackgroundColor(Color.BLACK);
            //Color.rgb(150,213,166);
            chart.setDrawGridBackground(true);

            // no description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // enable scaling and dragging
            chart.setDragEnabled(false);
            chart.setScaleEnabled(false);

            // if disabled, scaling can be done on x- and y-axis separately
            chart.setPinchZoom(false);

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

            chart.getLegend().setEnabled(false);

            chart.animateXY(1000, 1000);

            // don't forget to refresh the drawing
            chart.invalidate();
        }

        public void bind(ListItem item){
            setData(item.getX(),item.getY());
            tv.setText(item.getItemName());
        }

        private void setData(ArrayList<String> valX,ArrayList<String> valY) {

            ArrayList<Entry> values = new ArrayList<>();
            float x,y;

            for (int i = 0;i<valX.size();i++) {
                x = Float.parseFloat(valX.get(i));
                y = Float.parseFloat(valY.get(i));
                values.add(new Entry(x,y));
                Log.e("Val",x+ " "+ y);
            }

            LineDataSet set1;

            if (chart.getData() != null &&
                    chart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
                set1.setValues(values);
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            } else {
                // create a dataset and give it a type
                set1 = new LineDataSet(values, "DataSet 1");

                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                set1.setCubicIntensity(0.2f);
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
                set1.setFillFormatter(new IFillFormatter() {
                    @Override
                    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                        return chart.getAxisLeft().getAxisMinimum();
                    }
                });

                // create a data object with the data sets
                set1.setColor(Color.BLACK);
                ColorDrawable colorDrawable = new ColorDrawable(Color.rgb(150,213,166));
                set1.setFillDrawable(colorDrawable);
                set1.setDrawFilled(true);
                LineData data = new LineData(set1);
                data.setValueTextSize(9f);
                data.setDrawValues(false);

                // set data
                chart.setData(data);
            }
        }
    }
}
