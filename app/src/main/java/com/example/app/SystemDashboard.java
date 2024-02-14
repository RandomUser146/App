package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import java.util.ArrayList;
import java.util.List;
public class SystemDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_dashboard);

//        AnyChartView anyChartView = findViewById(R.id.temChart);
//        anyChartView.setProgressBar(findViewById(R.id.tprogress_bar));
//
//        Cartesian cartesian = AnyChart.line();
//
//        cartesian.animation(true);
//
//        cartesian.padding(10d, 20d, 5d, 20d);
//
//        cartesian.crosshair().enabled(true);
//        cartesian.crosshair()
//                .yLabel(true)
//                .yStroke((Stroke) null, null, null, (String) null, (String) null);
//
//        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
//
//        cartesian.title("Temperature and Humidity");
//
//        cartesian.yAxis(0).title("");
//        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
//
//        List<DataEntry> seriesData = new ArrayList<>();
//        seriesData.add(new CustomDataEntry("29/1/24:09:00", 25.0, 86));
//        seriesData.add(new CustomDataEntry("29/1/24:10:00", 27.3, 85));
//        seriesData.add(new CustomDataEntry("29/1/24:11:00", 28.4, 88));
//        seriesData.add(new CustomDataEntry("29/1/24:12:00", 30.5, 89));
//        seriesData.add(new CustomDataEntry("29/1/24:13:00", 32.6, 89));
//        seriesData.add(new CustomDataEntry("29/1/24:14:00", 32.0, 88));
//        seriesData.add(new CustomDataEntry("29/1/24:15:00", 31.3, 90));
//        seriesData.add(new CustomDataEntry("29/1/24:16:00", 30.4, 99));
//        seriesData.add(new CustomDataEntry("29/1/24:17:00", 30.6, 99));
//        seriesData.add(new CustomDataEntry("29/1/24:18:00", 31.77, 97));
//        seriesData.add(new CustomDataEntry("29/1/24:19:00", 29.67, 96));
//        seriesData.add(new CustomDataEntry("29/1/24:20:00", 30.34, 77));
//        seriesData.add(new CustomDataEntry("29/1/24:21:00", 28.57, 77));
//        seriesData.add(new CustomDataEntry("29/1/24:22:00", 27.345, 78));
//        seriesData.add(new CustomDataEntry("29/1/24:23:00", 26.345, 76));
//
//        Set set = Set.instantiate();
//        set.data(seriesData);
//        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
//        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
//
//        Line series1 = cartesian.line(series1Mapping);
//        series1.name("Temperature");
//        series1.hovered().markers().enabled(true);
//        series1.hovered().markers()
//                .type(MarkerType.CIRCLE)
//                .size(4d);
//        series1.tooltip()
//                .position("right")
//                .anchor(Anchor.LEFT_CENTER)
//                .offsetX(5d)
//                .offsetY(5d);
//
//        Line series2 = cartesian.line(series2Mapping);
//        series2.name("Humidity");
//        series2.hovered().markers().enabled(true);
//        series2.hovered().markers()
//                .type(MarkerType.CIRCLE)
//                .size(4d);
//        series2.tooltip()
//                .position("right")
//                .anchor(Anchor.LEFT_CENTER)
//                .offsetX(5d)
//                .offsetY(5d);
//
//        cartesian.legend().enabled(true);
//        cartesian.legend().fontSize(13d);
//        cartesian.legend().padding(0d, 0d, 10d, 0d);
//
//        anyChartView.setChart(cartesian);
//        cartesian.draw(true);

        AnyChartView anyChartView2 = findViewById(R.id.rainChart);
        anyChartView2.setProgressBar(findViewById(R.id.rprogress_bar));

        Cartesian cartesian2 = AnyChart.line();

        cartesian2.animation(true);

        cartesian2.padding(10d, 20d, 5d, 20d);

        cartesian2.crosshair().enabled(true);
        cartesian2.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian2.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian2.title("Rain and Predicted Precipitation");

        cartesian2.yAxis(0).title("in mm");
        cartesian2.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData2 = new ArrayList<>();
        seriesData2.add(new CustomDataEntry("29/1/24:09:00", 0.250, 0.86));
        seriesData2.add(new CustomDataEntry("29/1/24:10:00", 0.3, 0.85));
        seriesData2.add(new CustomDataEntry("29/1/24:11:00", 0.4, 0.88));
        seriesData2.add(new CustomDataEntry("29/1/24:12:00", 0.5, 0.89));
        seriesData2.add(new CustomDataEntry("29/1/24:13:00", 2.6, 2.89));
        seriesData2.add(new CustomDataEntry("29/1/24:14:00", 0.0, 0.2));
        seriesData2.add(new CustomDataEntry("29/1/24:15:00", 1.3, 0.90));
        seriesData2.add(new CustomDataEntry("29/1/24:16:00", 0.4, 0.99));
        seriesData2.add(new CustomDataEntry("29/1/24:17:00", 0.6, 0.99));
        seriesData2.add(new CustomDataEntry("29/1/24:18:00", 1.77, 0.97));
        seriesData2.add(new CustomDataEntry("29/1/24:19:00", 0.67, 0.96));
        seriesData2.add(new CustomDataEntry("29/1/24:20:00", 0.34, 0.77));
        seriesData2.add(new CustomDataEntry("29/1/24:21:00", 0.57, 0.77));
        seriesData2.add(new CustomDataEntry("29/1/24:22:00", 0.345, 0.78));
        seriesData2.add(new CustomDataEntry("29/1/24:23:00", 0.345, 0.76));

        Set set2 = Set.instantiate();
        set2.data(seriesData2);
        Mapping series1Mapping2 = set2.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping2 = set2.mapAs("{ x: 'x', value: 'value2' }");

        Line series12 = cartesian2.line(series1Mapping2);
        series12.name("Actual Rain");
        series12.hovered().markers().enabled(true);
        series12.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series12.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series22 = cartesian2.line(series2Mapping2);
        series22.name("Predicted rain");
        series22.hovered().markers().enabled(true);
        series22.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series22.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian2.legend().enabled(true);
        cartesian2.legend().fontSize(13d);
        cartesian2.legend().padding(0d, 0d, 10d, 0d);

        anyChartView2.setChart(cartesian2);
        cartesian2.draw(true);
    }



    private static class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }

}