package app.deadmc.devnetworktool.ui.presentation.fragments.rest

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.ui.adapters.RestRequestHistoryAdapter
import app.deadmc.devnetworktool.ui.presentation.fragments.BaseFragment
import app.deadmc.devnetworktool.ui.presentation.views.RestHistoryView
import app.deadmc.devnetworktool.data.models.RestRequestHistory
import app.deadmc.devnetworktool.ui.presentation.presenters.BasePresenter
import app.deadmc.devnetworktool.ui.presentation.presenters.RestHistoryPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_rest_history.view.*
import kotlinx.android.synthetic.main.layout_empty_list.view.*

class RestHistoryFragment : BaseFragment(), RestHistoryView {

    @InjectPresenter
    lateinit var restHistoryPresenter: RestHistoryPresenter
    private var restRequestHistoryArrayList: ArrayList<RestRequestHistory> = ArrayList()
    private lateinit var restRequestHistoryAdapter: RestRequestHistoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater.inflate(R.layout.fragment_rest_history, container, false)
        initElements()
        return myFragmentView
    }

    override fun getPresenter(): BasePresenter<*> {
        return restHistoryPresenter
    }

    fun initElements() {
        myFragmentView.recyclerViewHistory.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        myFragmentView.recyclerViewHistory.layoutManager = layoutManager
        initRecyclerView()
        restHistoryPresenter.fillRecyclerView()
    }

    fun initRecyclerView() {
        restRequestHistoryAdapter = object : RestRequestHistoryAdapter(context!!, ArrayList(restRequestHistoryArrayList)) {
            override fun onDeleteItem(element: RestRequestHistory) {
                restHistoryPresenter.deleteItem(element)
                if (restRequestHistoryAdapter.itemCount == 0)
                    showEmpty()
            }

            override fun onClickItem(restRequestHistory: RestRequestHistory, position: Int) {
                restHistoryPresenter.loadRestHistory(restRequestHistory)
            }
        }

        myFragmentView.recyclerViewHistory.adapter = restRequestHistoryAdapter
    }

    override fun fillRecyclerView(list: List<RestRequestHistory>) {
        restRequestHistoryArrayList = ArrayList(list)
        if (restRequestHistoryArrayList.isEmpty()) {
            showEmpty()
        } else {
            showView()
            restRequestHistoryAdapter.addAll(restRequestHistoryArrayList)
            restRequestHistoryAdapter.notifyDataSetChanged()
        }
    }

    fun showEmpty() {
        myFragmentView.recyclerViewHistory.visibility = View.GONE
        myFragmentView.emptyLayout.visibility = View.VISIBLE
    }

    fun showView() {
        myFragmentView.recyclerViewHistory.visibility = View.VISIBLE
        myFragmentView.emptyLayout.visibility = View.GONE
    }

    override fun addItem(restRequestHistory: RestRequestHistory) {
        activity?.runOnUiThread {
            showView()
            restRequestHistoryAdapter.addItem(restRequestHistory)
        }
    }
}
