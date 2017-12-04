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
import app.deadmc.devnetworktool.interfaces.RestLoadHistoryView
import app.deadmc.devnetworktool.interfaces.RestView
import app.deadmc.devnetworktool.modules.ResponseDev
import app.deadmc.devnetworktool.modules.RestRequestHistory
import app.deadmc.devnetworktool.presenters.RestLoadHistoryPresenter
import app.deadmc.devnetworktool.presenters.RestPresenter
import app.deadmc.devnetworktool.system.ItemTouchCallback
import app.deadmc.devnetworktool.system.SimpleDividerItemDecoration
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_rest_history.view.*

class RestHistoryFragment : BaseFragment(),RestView {

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
        myFragmentView.recyclerViewHistory.addItemDecoration(SimpleDividerItemDecoration(activity))
        restRequestHistoryAdapter.notifyDataSetChanged()
        initSwipe()

    }

    private fun initSwipe() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchCallback(activity) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                restRequestHistoryAdapter.removeItem(position)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
        })
        itemTouchHelper.attachToRecyclerView(myFragmentView.recyclerViewHistory)
    }

    override fun setResponse(responseDev: ResponseDev) {

    }



}
