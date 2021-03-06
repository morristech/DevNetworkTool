package app.deadmc.devnetworktool.ui.presentation.fragments.ping

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.ui.adapters.SimpleStringAdapter
import app.deadmc.devnetworktool.ui.presentation.views.PingView
import app.deadmc.devnetworktool.data.models.PingStats
import app.deadmc.devnetworktool.data.models.PingStructure
import app.deadmc.devnetworktool.data.models.SimpleString
import app.deadmc.devnetworktool.ui.presentation.presenters.BasePresenter
import app.deadmc.devnetworktool.ui.presentation.presenters.PingPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_pager_recyclerview.view.*
import java.io.Serializable
import java.util.*

class PingStatsPageFragment : PingBaseFragment(), PingView {

    @InjectPresenter
    lateinit var pingPresenter: PingPresenter
    private var pingStats: PingStats = PingStats()
    private lateinit var simpleStringAdapter: SimpleStringAdapter
    private var simpleStringArrayList: ArrayList<SimpleString> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager

    companion object {
        fun getInstance(serializable: Serializable): PingStatsPageFragment {
            val fragment = PingStatsPageFragment()
            val args = Bundle()
            args.putSerializable("current_url", serializable)
            fragment.arguments = args
            return fragment
        }
    }

    override fun addPingStructure(pingStructure: PingStructure) {
        pingStats.ipAddress = pingStructure.ipAddress
        pingStats.ttl = pingStructure.ttl
        pingStats.addPing(pingStructure.ping)
        if (initCompleted)
            setPingStats(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentUrl = this.arguments?.getString("current_url") ?:""
        pingPresenter.currentUrl = currentUrl
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater.inflate(R.layout.fragment_ping_stats, container, false)
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
        simpleStringAdapter = SimpleStringAdapter(activity!!, simpleStringArrayList)
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

