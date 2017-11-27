package app.deadmc.devnetworktool.fragments.ping

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type
import java.util.ArrayList

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.modules.PingStructure
import app.deadmc.devnetworktool.presenters.PingPagePresenter
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.presenter.InjectPresenter

/**
 * Created by Feren on 16.01.2017.
 */
class PingChartPageFragment : BasePingFragment() {

    @InjectPresenter
    lateinit var pingPagePresenter:PingPagePresenter
    private var lineChart: LineChart? = null
    private var lineDataSet: LineDataSet? = null


    override fun getPresenter(): PingPagePresenter {
        return pingPagePresenter
    }

    override fun addPingStructure(pingStructure: PingStructure, canUpdate: Boolean) {
        addDataToChart(pingStructure.ping, canUpdate)
    }


    override fun refreshFragment(pingStructureArrayList: ArrayList<PingStructure>) {
        /*
        try {
            setPingStructureArrayList(pingStructureArrayList);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
        } catch (NullPointerException e) {}
        */
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater!!.inflate(R.layout.fragment_pager_chart, container, false)
        initElements()
        pingPresenter?.pingPagePresenterList.add(pingPagePresenter)
        // Inflate the layout for this fragment
        return myFragmentView
    }


    private fun initElements() {
        initGraphic()
    }

    private fun initGraphic() {

        lineChart = myFragmentView.findViewById<View>(R.id.chart) as LineChart
        lineChart!!.setViewPortOffsets(0f, 0f, 0f, 0f)
        lineChart!!.setBackgroundColor(Color.rgb(238, 238, 238))

        // no description text
        lineChart!!.description.isEnabled = false

        // enable touch gestures
        lineChart!!.setTouchEnabled(true)

        // enable scaling and dragging
        lineChart!!.isDragEnabled = true
        lineChart!!.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart!!.setPinchZoom(false)

        lineChart!!.setDrawGridBackground(false)
        lineChart!!.maxHighlightDistance = 300f

        val x = lineChart!!.xAxis
        x.isEnabled = false

        val y = lineChart!!.axisLeft
        //y.setTypeface(mTfLight);
        y.setLabelCount(8, false)
        y.textColor = ContextCompat.getColor(activity, R.color.textColor)
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        y.setDrawGridLines(false)
        y.axisLineColor = ContextCompat.getColor(activity, R.color.textColor)

        lineChart!!.axisRight.isEnabled = false

        // add data
        setDataToChart()

        lineChart!!.legend.isEnabled = false

        lineChart!!.animateXY(2000, 2000)

        // dont forget to refresh the drawing
        lineChart!!.invalidate()
    }

    private fun setDataToChart() {
        val yVals = ArrayList<Entry>()

        if (lineChart!!.data != null && lineChart!!.data.dataSetCount > 0) {
            lineDataSet = lineChart!!.data.getDataSetByIndex(0) as LineDataSet
            lineDataSet!!.values = yVals
            lineChart!!.data.notifyDataChanged()
            lineChart!!.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            lineDataSet = LineDataSet(yVals, "DataSet 1")

            lineDataSet!!.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            lineDataSet!!.cubicIntensity = 0.2f
            lineDataSet!!.setDrawFilled(true)
            lineDataSet!!.setDrawCircles(false)
            lineDataSet!!.lineWidth = 1.0f
            lineDataSet!!.highlightLineWidth = 1.0f
            lineDataSet!!.highLightColor = ContextCompat.getColor(activity, R.color.textColor)
            lineDataSet!!.color = ContextCompat.getColor(activity, R.color.colorPrimaryDark)
            lineDataSet!!.fillColor = ContextCompat.getColor(activity, R.color.colorPrimary)
            lineDataSet!!.fillAlpha = 150
            lineDataSet!!.setDrawHorizontalHighlightIndicator(true)
            lineDataSet!!.fillFormatter = IFillFormatter { dataSet, dataProvider -> 0f }

            // create a data object with the datasets
            val data = LineData(lineDataSet!!)
            //data.setValueTypeface(mTfLight);
            data.setValueTextSize(9f)
            data.setDrawValues(false)

            // set data
            lineChart!!.data = data

            for (pingStructure in pingPresenter.pingStructureArrayList) {
                val i = lineDataSet!!.entryCount
                lineDataSet!!.addEntry(Entry(i.toFloat(), pingStructure.ping))
            }
            lineChart!!.data.notifyDataChanged()
            lineChart!!.notifyDataSetChanged()
            lineChart!!.invalidate()
        }
    }

    private fun addDataToChart(value: Float, canUpdate: Boolean) {
        val i = lineDataSet!!.entryCount
        lineDataSet!!.addEntry(Entry(i.toFloat(), value))
        if (canUpdate) {
            lineChart!!.data.notifyDataChanged()
            lineChart!!.notifyDataSetChanged()
            lineChart!!.invalidate()
        }
    }


}
