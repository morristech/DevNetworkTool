package app.deadmc.devnetworktool.fragments.ping

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.interfaces.views.PingView
import app.deadmc.devnetworktool.models.PingStructure
import app.deadmc.devnetworktool.presenters.BasePresenter
import app.deadmc.devnetworktool.presenters.PingPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import kotlinx.android.synthetic.main.fragment_pager_chart.view.*
import kotlinx.android.synthetic.main.layout_empty_list.view.*
import java.util.*


class PingChartPageFragment : PingBaseFragment(), PingView {

    @InjectPresenter
    lateinit var pingPresenter: PingPresenter
    private lateinit var lineDataSet: LineDataSet

    override fun addPingStructure(pingStructure: PingStructure) {
        if (initCompleted)
            addDataToChart(pingStructure.ping)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater!!.inflate(R.layout.fragment_pager_chart, container, false)
        initGraphic()
        initCompleted = true
        return myFragmentView
    }

    override fun getPresenter(): BasePresenter<*> {
        return pingPresenter
    }

    private fun initGraphic() {
        myFragmentView.chart.setViewPortOffsets(0f, 0f, 0f, 0f)
        myFragmentView.chart.setBackgroundColor(Color.rgb(238, 238, 238))
        // no description text
        myFragmentView.chart.description.isEnabled = false
        // enable touch gestures
        myFragmentView.chart.setTouchEnabled(true)
        // enable scaling and dragging
        myFragmentView.chart.isDragEnabled = true
        myFragmentView.chart.setScaleEnabled(true)
        // if disabled, scaling can be done on x- and y-axis separately
        myFragmentView.chart.setPinchZoom(false)
        myFragmentView.chart.setDrawGridBackground(false)
        myFragmentView.chart.maxHighlightDistance = 300f

        myFragmentView.chart.setBackgroundColor(ContextCompat.getColor(activity, R.color.white))

        val x = myFragmentView.chart.xAxis
        x.isEnabled = false
        val y = myFragmentView.chart.axisLeft
        //y.setTypeface(mTfLight);
        y.setLabelCount(8, false)
        y.axisMinimum = 0f
        y.textColor = ContextCompat.getColor(activity, R.color.textColor)
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        y.setDrawGridLines(false)
        y.axisLineColor = ContextCompat.getColor(activity, R.color.textColor)

        myFragmentView.chart.axisRight.isEnabled = false
        showEmpty()
        setDataToChart()
        myFragmentView.chart.legend.isEnabled = false
        myFragmentView.chart.animateXY(2000, 2000)
        myFragmentView.chart.invalidate()

    }

    private fun setDataToChart() {
        val yVals = ArrayList<Entry>()
        if (myFragmentView.chart.data != null && myFragmentView.chart.data.dataSetCount > 0) {
            showView()
            lineDataSet = myFragmentView.chart.data.getDataSetByIndex(0) as LineDataSet
            lineDataSet.values = yVals
            myFragmentView.chart.data.notifyDataChanged()
            myFragmentView.chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            lineDataSet = LineDataSet(yVals, "DataSet 1")

            lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            lineDataSet.cubicIntensity = 0.2f
            lineDataSet.setDrawFilled(true)
            lineDataSet.setDrawCircles(false)
            lineDataSet.lineWidth = 1.0f
            lineDataSet.highlightLineWidth = 1.0f
            lineDataSet.highLightColor = ContextCompat.getColor(activity, R.color.textColorLight)
            lineDataSet.color = ContextCompat.getColor(activity, R.color.colorPrimaryDark)
            lineDataSet.fillColor = ContextCompat.getColor(activity, R.color.colorPrimary)
            lineDataSet.fillAlpha = 150
            lineDataSet.setDrawHorizontalHighlightIndicator(true)
            lineDataSet.fillFormatter = IFillFormatter { dataSet, dataProvider -> 0f }

            // create a data object with the datasets
            val data = LineData(lineDataSet!!)
            //data.setValueTypeface(mTfLight);
            data.setValueTextSize(9f)
            data.setDrawValues(false)
            myFragmentView.chart.data = data

            for (pingStructure in pingPresenter.pingStructureArrayList) {
                val i = lineDataSet.entryCount
                lineDataSet.addEntry(Entry(i.toFloat(), pingStructure.ping))
            }
            myFragmentView.chart.data.notifyDataChanged()
            myFragmentView.chart.notifyDataSetChanged()
            myFragmentView.chart.invalidate()
        }
    }

    private fun addDataToChart(value: Float) {
        showView()
        val i = lineDataSet.entryCount
        lineDataSet.addEntry(Entry(i.toFloat(), value))
        myFragmentView.chart.data.notifyDataChanged()
        myFragmentView.chart.notifyDataSetChanged()
        myFragmentView.chart.invalidate()
    }

    private fun showEmpty() {
        myFragmentView.chart.visibility = View.GONE
        myFragmentView.emptyLayout.visibility = View.VISIBLE
    }

    private fun showView() {
        if (myFragmentView.chart.visibility == View.VISIBLE)
            return
        myFragmentView.emptyLayout.visibility = View.GONE
        myFragmentView.chart.visibility = View.VISIBLE
    }


}
