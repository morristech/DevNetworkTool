package app.deadmc.devnetworktool.fragments.ping

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.adapters.ReceivedPingsAdapter
import app.deadmc.devnetworktool.interfaces.views.PingView
import app.deadmc.devnetworktool.models.PingStructure
import app.deadmc.devnetworktool.presenters.PingPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import kotlinx.android.synthetic.main.fragment_pager_recyclerview.view.*

/**
 * Created by Feren on 12.01.2017.
 */
class PingRawPageFragment : BasePingFragment(), PingView {


    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var pingPresenter: PingPresenter
    lateinit var receivedMessagesAdapter: ReceivedPingsAdapter
    private var linearLayoutManager: LinearLayoutManager? = null

    override fun addPingStructure(pingStructure: PingStructure) {
        if (!initCompleted)
            return
        receivedMessagesAdapter.notifyDataSetChanged()
        if (linearLayoutManager!!.findLastVisibleItemPosition() >= pingPresenter.pingStructureArrayList.size - 3)
            myFragmentView.recyclerView.smoothScrollToPosition(pingPresenter.pingStructureArrayList.size - 1)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater!!.inflate(R.layout.fragment_pager_recyclerview, container, false)
        initElements()
        return myFragmentView
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
        receivedMessagesAdapter = ReceivedPingsAdapter(activity, pingPresenter.pingStructureArrayList)
        myFragmentView.recyclerView.adapter = receivedMessagesAdapter
        try {
            if (pingPresenter.currentPosition != 0 && pingPresenter.pingStructureArrayList.size > pingPresenter.currentPosition)
                myFragmentView.recyclerView.smoothScrollToPosition(pingPresenter.currentPosition)
        } catch (e: IllegalArgumentException) {
            //Log.e("error",Log.getStackTraceString(e));
        }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        pingPresenter.currentPosition = linearLayoutManager!!.findLastVisibleItemPosition()
    }


}
