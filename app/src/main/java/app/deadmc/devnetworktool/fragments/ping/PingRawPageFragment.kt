package app.deadmc.devnetworktool.fragments.ping

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.adapters.ReceivedPingsAdapter
import app.deadmc.devnetworktool.helpers.safe
import app.deadmc.devnetworktool.interfaces.views.PingView
import app.deadmc.devnetworktool.models.PingStructure
import app.deadmc.devnetworktool.presenters.BasePresenter
import app.deadmc.devnetworktool.presenters.PingPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_pager_chart.view.*
import kotlinx.android.synthetic.main.fragment_pager_recyclerview.view.*
import kotlinx.android.synthetic.main.layout_empty_list.view.*

class PingRawPageFragment : PingBaseFragment(), PingView {

    @InjectPresenter
    lateinit var pingPresenter: PingPresenter
    lateinit var receivedMessagesAdapter: ReceivedPingsAdapter
    private var linearLayoutManager: LinearLayoutManager? = null

    override fun addPingStructure(pingStructure: PingStructure) {
        showView()
        if (!initCompleted)
            return
        receivedMessagesAdapter.addItem(pingStructure)
        if (linearLayoutManager!!.findLastVisibleItemPosition() >= pingPresenter.pingStructureArrayList.size - 3)
            myFragmentView.recyclerView.smoothScrollToPosition(pingPresenter.pingStructureArrayList.size - 1)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater.inflate(R.layout.fragment_pager_recyclerview, container, false)
        initElements()
        return myFragmentView
    }

    override fun getPresenter(): BasePresenter<*> {
        return pingPresenter
    }


    private fun initElements() {
        initRecyclerView()
        initPingList()
        initCompleted = true
    }

    private fun initRecyclerView() {
        linearLayoutManager = LinearLayoutManager(activity)
        myFragmentView.recyclerView.layoutManager = linearLayoutManager

    }

    private fun initPingList() {
        receivedMessagesAdapter = ReceivedPingsAdapter(activity!!, pingPresenter.pingStructureArrayList)
        myFragmentView.recyclerView.adapter = receivedMessagesAdapter
        safe {
            if (pingPresenter.currentPosition != 0 && pingPresenter.pingStructureArrayList.size > pingPresenter.currentPosition)
                myFragmentView.recyclerView.smoothScrollToPosition(pingPresenter.currentPosition)
        }

        if (pingPresenter.pingStructureArrayList.isEmpty())
            showEmpty()
        else
            showView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        pingPresenter.currentPosition = linearLayoutManager!!.findLastVisibleItemPosition()
    }

    private fun showEmpty() {
        myFragmentView.recyclerView.visibility = View.GONE
        myFragmentView.emptyLayout.visibility = View.VISIBLE
    }

    private fun showView() {
        if (myFragmentView.recyclerView.visibility == View.VISIBLE)
            return
        myFragmentView.emptyLayout.visibility = View.GONE
        myFragmentView.recyclerView.visibility = View.VISIBLE
    }


}
