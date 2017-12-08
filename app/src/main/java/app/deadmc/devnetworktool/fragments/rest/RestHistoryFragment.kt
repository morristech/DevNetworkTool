package app.deadmc.devnetworktool.fragments.rest

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.adapters.RestRequestHistoryAdapter
import app.deadmc.devnetworktool.constants.DevConsts
import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.interfaces.views.RestView
import app.deadmc.devnetworktool.models.ResponseDev
import app.deadmc.devnetworktool.models.RestRequestHistory
import app.deadmc.devnetworktool.presenters.RestPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import kotlinx.android.synthetic.main.fragment_rest_history.view.*

class RestHistoryFragment : BaseFragment(), RestView {

    @InjectPresenter(type = PresenterType.GLOBAL, tag = DevConsts.REST)
    lateinit var restPresenter:RestPresenter

    private var restRequestHistoryArrayList: ArrayList<RestRequestHistory>? = null
    private lateinit var restRequestHistoryAdapter: RestRequestHistoryAdapter
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater!!.inflate(R.layout.fragment_rest_history, container, false)
        initElements()
        return myFragmentView
    }


    override fun loadRequestHistory(restRequestHistory: RestRequestHistory) {
        Log.e(TAG,"loadRequestHistory ")
    }

    fun initElements() {
        myFragmentView.recyclerViewHistory.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        myFragmentView.recyclerViewHistory.layoutManager = layoutManager
        restRequestHistoryArrayList = ArrayList(RestRequestHistory.listAll(RestRequestHistory::class.java))
        restRequestHistoryAdapter = object : RestRequestHistoryAdapter(context, restRequestHistoryArrayList) {
            override fun onLongClickItem(restRequestHistory: RestRequestHistory, position: Int) {
                super.onLongClickItem(restRequestHistory, position)
            }

            override fun onClickItem(restRequestHistory: RestRequestHistory, position: Int) {
                restPresenter.loadRestHistory(restRequestHistory)
            }
        }

        myFragmentView.recyclerViewHistory.adapter = restRequestHistoryAdapter
        restRequestHistoryAdapter.notifyDataSetChanged()
    }

    override fun setResponse(responseDev: ResponseDev) {

    }



}
