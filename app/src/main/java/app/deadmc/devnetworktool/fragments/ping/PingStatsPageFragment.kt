package app.deadmc.devnetworktool.fragments.ping

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type
import java.util.ArrayList

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.adapters.SimpleStringAdapter
import app.deadmc.devnetworktool.modules.PingStats
import app.deadmc.devnetworktool.modules.PingStructure
import app.deadmc.devnetworktool.modules.SimpleString
import app.deadmc.devnetworktool.presenters.PingPagePresenter
import app.deadmc.devnetworktool.presenters.PingPresenter
import app.deadmc.devnetworktool.system.SimpleDividerItemDecoration
import com.arellomobile.mvp.presenter.InjectPresenter

/**
 * Created by Feren on 14.01.2017.
 */
class PingStatsPageFragment : BasePingFragment() {

    @InjectPresenter
    lateinit var pingPagePresenter:PingPagePresenter
    private var currentUrl: String? = null
    private var recyclerView: RecyclerView? = null
    private var pingStats: PingStats? = null
    private var simpleStringAdapter: SimpleStringAdapter? = null
    private var simpleStringArrayList: ArrayList<SimpleString>? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    override fun getPresenter(): PingPagePresenter {
        return pingPagePresenter
    }

    override fun getCommonPresenter(): PingPresenter {
        return PingPresenter()
    }

    init {
        // Required empty public constructor
        Log.e("PingStats", "default constructor called")
    }

    fun setCurrentUrl(currentUrl: String) {
        this.currentUrl = currentUrl
    }

    override fun addPingStructure(pingStructure: PingStructure, canUpdate: Boolean) {
        Log.e("pingStats", "add ping structure " + canUpdate)
        if (pingStats == null) {
            Log.e("pingStats", "pingStats is null")
            return
        }
        pingStats!!.ipAddress = pingStructure.ipAddress
        pingStats!!.ttl = pingStructure.ttl
        pingStats!!.addPing(pingStructure.ping)
        if (canUpdate)
            setPingStats(false)
        //simpleStringAdapter.notifyDataSetChanged();
    }

    override fun refreshFragment(arrayList: ArrayList<PingStructure>) {
        try {
            getCommonPresenter().pingStructureArrayList = arrayList
            Log.e("refreshStats", "count of elements " + getCommonPresenter().pingStructureArrayList.size)
            setPingStats(true)
        } catch (e: NullPointerException) {
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater!!.inflate(R.layout.fragment_pager_recyclerview, container, false)
        restoreState(savedInstanceState)
        initElements()
        getCommonPresenter().pingPagePresenterList.add(pingPagePresenter)
        // Inflate the layout for this fragment
        return myFragmentView
    }

    private fun initElements() {
        initRecyclerView()
        initPingList()
    }

    private fun initRecyclerView() {
        recyclerView = myFragmentView.findViewById<View>(R.id.recyclerView) as RecyclerView
        linearLayoutManager = LinearLayoutManager(activity)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.addItemDecoration(SimpleDividerItemDecoration(activity))
    }

    private fun initPingList() {

        simpleStringArrayList = ArrayList()
        simpleStringAdapter = SimpleStringAdapter(activity, simpleStringArrayList)
        recyclerView!!.adapter = simpleStringAdapter
        setPingStats(true)
    }

    private fun setPingStats(refreshAll: Boolean) {
        Log.e("setPingStats", "called")
        if (getCommonPresenter().pingStructureArrayList == null)
            return
        if (pingStats == null || refreshAll) {
            pingStats = PingStats()
            for (pingStructure in getCommonPresenter().pingStructureArrayList) {
                pingStats!!.ipAddress = pingStructure.ipAddress
                pingStats!!.ttl = pingStructure.ttl
                pingStats!!.addPing(pingStructure.ping)
            }
        }
        simpleStringArrayList!!.clear()
        simpleStringArrayList!!.add(SimpleString(getString(R.string.general), true))
        simpleStringArrayList!!.add(SimpleString(getString(R.string.url) + " : " + currentUrl))
        simpleStringArrayList!!.add(SimpleString(getString(R.string.ip_address) + " : " + pingStats!!.ipAddress))
        simpleStringArrayList!!.add(SimpleString(getString(R.string.ttl) + " : " + pingStats!!.ttl))

        simpleStringArrayList!!.add(SimpleString(getString(R.string.stats), true))
        simpleStringArrayList!!.add(SimpleString(getString(R.string.sended) + " : " + pingStats!!.sended))
        simpleStringArrayList!!.add(SimpleString(getString(R.string.successful) + " : " + pingStats!!.successful))
        simpleStringArrayList!!.add(SimpleString(getString(R.string.failed) + " : " + pingStats!!.failed))

        simpleStringArrayList!!.add(SimpleString(getString(R.string.ping), true))
        simpleStringArrayList!!.add(SimpleString(getString(R.string.max) + " : " + pingStats!!.max + " " + getString(R.string.ms)))
        simpleStringArrayList!!.add(SimpleString(getString(R.string.min) + " : " + pingStats!!.min + " " + getString(R.string.ms)))
        simpleStringArrayList!!.add(SimpleString(getString(R.string.average) + " : " + pingStats!!.average + " " + getString(R.string.ms)))


        //
        if (simpleStringAdapter != null)
            simpleStringAdapter!!.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.e("onSaveInstanceState", "save")
        outState!!.putString("currentUrl", currentUrl)
        //outState.putString("pingStructureList", new Gson().toJson(getPingStructureArrayList()));
        //outState.putString("pingStats",new Gson().toJson(pingStats));
    }

    private fun restoreState(bundle: Bundle?) {
        /*
        Log.e("restoreState", "restore");
        if (bundle==null)
            return;
        if (!bundle.containsKey("pingStructureList"))
            return;
        if (!bundle.containsKey("currentUrl"))
            return;
        if (!bundle.containsKey("pingStats"))
            return;
        Log.e("restoreState", "restore is ok");
        currentUrl = bundle.getString("currentUrl");
        Type type = new TypeToken<ArrayList<PingStructure>>(){}.getType();
        String serializedString = bundle.getString("pingStructureList");
        Gson gson = new Gson();
        setPingStructureArrayList(gson.fromJson(serializedString, type));

        type = new TypeToken<PingStats>(){}.getType();
        serializedString = bundle.getString("pingStats");
        pingStats = gson.fromJson(serializedString, type);

        Log.e("PingStats","pingStats "+pingStats.getIpAddress());
        */
    }


}

