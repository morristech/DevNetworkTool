package app.deadmc.devnetworktool.fragments.rest

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.adapters.RestRequestHistoryAdapter
import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.models.RestRequestHistory
import com.orm.SugarRecord
import kotlinx.android.synthetic.main.fragment_rest_history.view.*
import java.util.*

class RestHistoryFragment : BaseFragment() {

    private var restRequestHistoryArrayList: List<RestRequestHistory>? = null
    private lateinit var restRequestHistoryAdapter: RestRequestHistoryAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater!!.inflate(R.layout.fragment_rest_history, container, false)
        initElements()
        return myFragmentView
    }

    fun initElements() {
        myFragmentView.recyclerViewHistory.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        myFragmentView.recyclerViewHistory.layoutManager = layoutManager
        restRequestHistoryArrayList = SugarRecord.listAll(RestRequestHistory::class.java)
        restRequestHistoryAdapter = object : RestRequestHistoryAdapter(context, ArrayList(restRequestHistoryArrayList)) {
            override fun onDeleteItem(element: RestRequestHistory) {
                element.delete()
            }

            override fun onClickItem(restRequestHistory: RestRequestHistory, position: Int) {
                //restPresenter.loadRestHistory(restRequestHistory)
            }
        }

        myFragmentView.recyclerViewHistory.adapter = restRequestHistoryAdapter
        restRequestHistoryAdapter.notifyDataSetChanged()
    }
}
