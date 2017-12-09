package app.deadmc.devnetworktool.fragments.rest

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout

import java.util.ArrayList

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.activities.FullViewActivity
import app.deadmc.devnetworktool.adapters.ParametersAdapter
import app.deadmc.devnetworktool.constants.REST
import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.helpers.FileFormatHelper
import app.deadmc.devnetworktool.helpers.StringHelper
import app.deadmc.devnetworktool.models.ResponseDev
import app.deadmc.devnetworktool.views.CollapseLinearLayout

import app.deadmc.devnetworktool.helpers.ImageHelpers.rotateImage
import app.deadmc.devnetworktool.interfaces.views.RestView
import app.deadmc.devnetworktool.models.RestRequestHistory
import app.deadmc.devnetworktool.presenters.RestPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import kotlinx.android.synthetic.main.fragment_rest_response.*
import kotlinx.android.synthetic.main.fragment_rest_response.view.*

class ResponseRestFragment : BaseFragment(), RestView {

    @InjectPresenter(type = PresenterType.GLOBAL, tag = REST)
    lateinit var restPresenter: RestPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater!!.inflate(R.layout.fragment_rest_response, container, false)
        initElements()
        return myFragmentView
    }

    fun initElements() {
        initLayout(myFragmentView.collapseLinearLayoutStats, myFragmentView.statsTitleLayout, myFragmentView.imageViewArrowStats)
        initLayout(myFragmentView.collapseLinearLayoutHeaders, myFragmentView.headerTitleLayout, myFragmentView.imageViewArrowHeader)
        initLayout(myFragmentView.collapseLinearLayoutRequest, myFragmentView.requestTitleLayout, myFragmentView.imageViewArrowRequest)
        initRecyclerViews(myFragmentView.headersRecyclerView, null)
        initRecyclerViews(myFragmentView.statsRecyclerView, null)
        hideButton()
    }

    fun hideButton() {
        myFragmentView.watchButton.visibility = View.GONE
    }

    fun initButton(responseDev: ResponseDev) {
        myFragmentView.watchButton.visibility = View.VISIBLE

        val type = FileFormatHelper.getTypeOfString(responseDev.body)
        when (type) {
            FileFormatHelper.JSON -> myFragmentView.watchButton.text = getString(R.string.open_as) + " " + getString(R.string.json)
            FileFormatHelper.XML -> myFragmentView.watchButton.text = getString(R.string.open_as) + " " + getString(R.string.xml)
            FileFormatHelper.HTML -> myFragmentView.watchButton.text = getString(R.string.open_as) + " " + getString(R.string.html)
            FileFormatHelper.UNDEFINED -> myFragmentView.watchButton.visibility = View.GONE
        }


        myFragmentView.watchButton.setOnClickListener({
            val intent = Intent(context, FullViewActivity::class.java)
            startActivity(intent)
        })
    }

    fun initLayout(collapseLinearLayout: CollapseLinearLayout, titleLayout: LinearLayout, imageView: ImageView?) {
        collapseLinearLayout.collapse()

        titleLayout.setOnClickListener {
            if (collapseLinearLayout.isCollapsed) {
                collapseLinearLayout.expand()
            } else {
                collapseLinearLayout.collapse()
            }
            rotateImage(imageView, collapseLinearLayout.isCollapsed, 300)
        }
        rotateImage(imageView, collapseLinearLayout.isCollapsed, 0)
    }


    override fun setResponse(responseDev: ResponseDev) {
        Log.e("Response", "setResponse")
        initHeadersRecyclerView(responseDev.headers)
        initStatsRecyclerView(responseDev.code, responseDev.delay)
        bodyTextView!!.text = responseDev.body
        initButton(responseDev)

    }

    private fun initRecyclerViews(recyclerView: RecyclerView?, adapter: RecyclerView.Adapter<*>?) {
        val layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }


    private fun initStatsRecyclerView(code: Int, responseTime: Int) {
        val arrayList = ArrayList<Spanned>()
        arrayList.add(StringHelper.fromHtml(getString(R.string.response_code) + " " + code))
        arrayList.add(StringHelper.fromHtml(getString(R.string.response_time) + " " + responseTime + " " + getString(R.string.ms)))
        val parametersAdapter = ParametersAdapter(context, arrayList)
        initRecyclerViews(statsRecyclerView, parametersAdapter)
    }

    private fun initHeadersRecyclerView(headers: String) {
        val arrayList = ArrayList<Spanned>()
        val stringArray = headers.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (head in stringArray) {
            val index = head.indexOf(":")
            val key = head.substring(0, index)
            val value = head.substring(index, head.length)
            arrayList.add(StringHelper.fromHtml("<b>$key</b>$value"))
        }

        val parametersAdapter = ParametersAdapter(context, arrayList)
        initRecyclerViews(headersRecyclerView, parametersAdapter)
    }

    override fun loadRequestHistory(restRequestHistory: RestRequestHistory) {
    }

    override fun hideProgress() {
    }

    override fun showProgress() {
    }
}
