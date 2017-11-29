package app.deadmc.devnetworktool.fragments.ping

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.adapters.SimpleStringAdapter
import app.deadmc.devnetworktool.interfaces.PingView
import app.deadmc.devnetworktool.modules.PingStats
import app.deadmc.devnetworktool.modules.PingStructure
import app.deadmc.devnetworktool.modules.SimpleString
import app.deadmc.devnetworktool.presenters.PingPresenter
import app.deadmc.devnetworktool.system.SimpleDividerItemDecoration
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import kotlinx.android.synthetic.main.fragment_pager_recyclerview.view.*

class PingStatsPageFragment : BasePingFragment(),PingView {

    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var pingPresenter:PingPresenter
    private var pingStats: PingStats = PingStats()
    private lateinit var simpleStringAdapter: SimpleStringAdapter
    private var simpleStringArrayList: ArrayList<SimpleString> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun addPingStructure(pingStructure: PingStructure) {
        pingStats.ipAddress = pingStructure.ipAddress
        pingStats.ttl = pingStructure.ttl
        pingStats.addPing(pingStructure.ping)
        if (initCompleted)
            setPingStats(false)
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
        myFragmentView.recyclerView.addItemDecoration(SimpleDividerItemDecoration(activity))
    }

    private fun initPingList() {
        simpleStringAdapter = SimpleStringAdapter(activity, simpleStringArrayList)
        myFragmentView.recyclerView.adapter = simpleStringAdapter
        setPingStats(true)
    }

    private fun setPingStats(refreshAll: Boolean) {
        if (refreshAll) {
            for (pingStructure in pingPresenter.pingStructureArrayList) {
                pingStats.ipAddress = pingStructure.ipAddress
                pingStats.ttl = pingStructure.ttl
                pingStats.addPing(pingStructure.ping)
            }
        }
        simpleStringArrayList.clear()
        simpleStringArrayList.add(SimpleString(getString(R.string.general), true))
        simpleStringArrayList.add(SimpleString(getString(R.string.url) + " : " + pingPresenter.currentUrl))
        simpleStringArrayList.add(SimpleString(getString(R.string.ip_address) + " : " + pingStats.ipAddress))
        simpleStringArrayList.add(SimpleString(getString(R.string.ttl) + " : " + pingStats.ttl))

        simpleStringArrayList.add(SimpleString(getString(R.string.stats), true))
        simpleStringArrayList.add(SimpleString(getString(R.string.sended) + " : " + pingStats.sended))
        simpleStringArrayList.add(SimpleString(getString(R.string.successful) + " : " + pingStats.successful))
        simpleStringArrayList.add(SimpleString(getString(R.string.failed) + " : " + pingStats.failed))

        simpleStringArrayList.add(SimpleString(getString(R.string.ping), true))
        simpleStringArrayList.add(SimpleString(getString(R.string.max) + " : " + pingStats.max + " " + getString(R.string.ms)))
        simpleStringArrayList.add(SimpleString(getString(R.string.min) + " : " + pingStats.min + " " + getString(R.string.ms)))
        simpleStringArrayList.add(SimpleString(getString(R.string.average) + " : " + pingStats.average + " " + getString(R.string.ms)))

        simpleStringAdapter.notifyDataSetChanged()
    }

}

