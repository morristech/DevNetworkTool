package app.deadmc.devnetworktool.fragments.ping;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.modules.PingStructure;

/**
 * Created by Feren on 16.01.2017.
 */
public class PingChartPageFragment extends BasePingFragment {

    private LineChart lineChart;
    private LineDataSet lineDataSet;

    public PingChartPageFragment() {

    }

    @Override
    public void addPingStructure(PingStructure pingStructure, boolean canUpdate){
        addDataToChart(pingStructure.getPing(),canUpdate);
    }

    @Override
    public void refreshFragment(ArrayList<PingStructure> pingStructureArrayList) {
        try {
            setPingStructureArrayList(pingStructureArrayList);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
        } catch (NullPointerException e) {}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setMyFragmentView(inflater.inflate(R.layout.fragment_pager_chart, container, false));
        restoreState(savedInstanceState);
        initElements();
        // Inflate the layout for this fragment
        return getMyFragmentView();
    }


    private void initElements() {
        initGraphic();
    }

    private void initGraphic() {

        lineChart = (LineChart) getMyFragmentView().findViewById(R.id.chart);
        lineChart.setViewPortOffsets(0, 0, 0, 0);
        lineChart.setBackgroundColor(Color.rgb(238, 238, 238));

        // no description text
        lineChart.getDescription().setEnabled(false);

        // enable touch gestures
        lineChart.setTouchEnabled(true);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);

        lineChart.setDrawGridBackground(false);
        lineChart.setMaxHighlightDistance(300);

        XAxis x = lineChart.getXAxis();
        x.setEnabled(false);

        YAxis y = lineChart.getAxisLeft();
        //y.setTypeface(mTfLight);
        y.setLabelCount(8, false);
        y.setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor));
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(ContextCompat.getColor(getActivity(),R.color.textColor));

        lineChart.getAxisRight().setEnabled(false);

        // add data
        setDataToChart();

        lineChart.getLegend().setEnabled(false);

        lineChart.animateXY(2000, 2000);

        // dont forget to refresh the drawing
        lineChart.invalidate();
    }

    private void setDataToChart() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(yVals);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            lineDataSet = new LineDataSet(yVals, "DataSet 1");

            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            lineDataSet.setCubicIntensity(0.2f);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setDrawCircles(false);
            lineDataSet.setLineWidth(1.0f);
            lineDataSet.setHighlightLineWidth(1.0f);
            lineDataSet.setHighLightColor(ContextCompat.getColor(getActivity(),R.color.textColor));
            lineDataSet.setColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
            lineDataSet.setFillColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            lineDataSet.setFillAlpha(150);
            lineDataSet.setDrawHorizontalHighlightIndicator(true);
            lineDataSet.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return 0;
                }
            });

            // create a data object with the datasets
            LineData data = new LineData(lineDataSet);
            //data.setValueTypeface(mTfLight);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            lineChart.setData(data);
            if (getPingStructureArrayList() == null)
                return;
            Log.e("PingChart", "pingStructureArrayList size = " + getPingStructureArrayList().size());

            for(PingStructure pingStructure: getPingStructureArrayList()) {
                int i = lineDataSet.getEntryCount();
                lineDataSet.addEntry(new Entry(i, pingStructure.getPing()));
            }
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
        }
    }

    private void addDataToChart(float value, boolean canUpdate) {
        int i = lineDataSet.getEntryCount();
        lineDataSet.addEntry(new Entry(i, value));
        if (canUpdate) {
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.e("onSaveInstanceState", "save");
        outState.putString("pingStructureList", new Gson().toJson(getPingStructureArrayList()));
    }

    private void restoreState(Bundle bundle) {
        //Log.e("restoreState","restore");
        if (bundle==null)
            return;
        if (!bundle.containsKey("pingStructureList"))
            return;
        //Log.e("restoreState", "restore is ok");
        Type type = new TypeToken<ArrayList<PingStructure>>(){}.getType();
        String serializedString = bundle.getString("pingStructureList");
        Gson gson = new Gson();
        setPingStructureArrayList((ArrayList<PingStructure>)gson.fromJson(serializedString, type));
    }

}
