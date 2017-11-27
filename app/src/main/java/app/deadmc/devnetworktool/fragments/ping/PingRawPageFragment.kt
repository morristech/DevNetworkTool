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
import app.deadmc.devnetworktool.adapters.ReceivedPingsAdapter
import app.deadmc.devnetworktool.modules.PingStructure
import app.deadmc.devnetworktool.presenters.PingPagePresenter
import app.deadmc.devnetworktool.presenters.PingPresenter
import app.deadmc.devnetworktool.system.SimpleDividerItemDecoration
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_pager_recyclerview.view.*

/**
 * Created by Feren on 12.01.2017.
 */
class PingRawPageFragment : BasePingFragment() {

    @InjectPresenter
    lateinit var pingPagePresenter:PingPagePresenter
    lateinit var receivedMessagesAdapter: ReceivedPingsAdapter
    private var linearLayoutManager: LinearLayoutManager? = null
    private val lastVisiblePosition = 0

    init {
        // Required empty public constructor
        Log.e("PingRawPageFragment", "new created")
    }

    override fun getPresenter(): PingPagePresenter {
        return pingPagePresenter
    }

    override fun getCommonPresenter(): PingPresenter {
        return PingPresenter()
    }

    override fun addPingStructure(pingStructure: PingStructure, canUpdate: Boolean) {
        //Log.e("addPingStructure","RAW");
        if (canUpdate) {
            receivedMessagesAdapter.notifyDataSetChanged()
            if (linearLayoutManager!!.findLastVisibleItemPosition() >= getCommonPresenter().pingStructureArrayList.size - 3)
                myFragmentView.recyclerView.smoothScrollToPosition(getCommonPresenter().pingStructureArrayList.size - 1)
            else {
                //Log.e("raw","linearLayoutManager.findLastVisibleItemPosition() = "+linearLayoutManager.findLastVisibleItemPosition());
                //Log.e("raw","pingStructureArrayList.size() = "+pingStructureArrayList.size());
            }
        }

    }

    override fun refreshFragment(arrayList: ArrayList<PingStructure>) {
        /*
        try {
            setPingStructureArrayList(arrayList);
            receivedMessagesAdapter = new ReceivedPingsAdapter(getActivity(), getPingStructureArrayList());
            recyclerView.setAdapter(receivedMessagesAdapter);
            receivedMessagesAdapter.notifyDataSetChanged();
            if (linearLayoutManager.findLastVisibleItemPosition() == getPingStructureArrayList().size() - 2)
                recyclerView.smoothScrollToPosition(getPingStructureArrayList().size() - 1);
        } catch (NullPointerException e) {}
        */
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
        linearLayoutManager = LinearLayoutManager(activity)
        myFragmentView.recyclerView.layoutManager = linearLayoutManager
        myFragmentView.recyclerView.addItemDecoration(SimpleDividerItemDecoration(activity))

    }

    private fun initPingList() {
        receivedMessagesAdapter = ReceivedPingsAdapter(activity, getCommonPresenter().pingStructureArrayList)
        myFragmentView.recyclerView.adapter = receivedMessagesAdapter
        try {
            if (lastVisiblePosition != 0 && getCommonPresenter().pingStructureArrayList.size > lastVisiblePosition)
                myFragmentView.recyclerView.smoothScrollToPosition(lastVisiblePosition)
        } catch (e: IllegalArgumentException) {
            //Log.e("error",Log.getStackTraceString(e));
        }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        //Log.e("onSaveInstanceState", "save");
        //outState.putString("pingStructureList", new Gson().toJson(getPingStructureArrayList()));
        outState!!.putInt("scrollPosition", linearLayoutManager!!.findLastVisibleItemPosition())
    }

    private fun restoreState(bundle: Bundle?) {
        /*
        //Log.e("restoreState","restore");
        if (bundle==null)
            return;
        if (!bundle.containsKey("pingStructureList"))
            return;
        if (bundle.containsKey("scrollPosition"))
            lastVisiblePosition = bundle.getInt("scrollPosition");
        Log.e("restoreState","restore is ok");
        Type type = new TypeToken<ArrayList<PingStructure>>(){}.getType();
        String serializedString = bundle.getString("pingStructureList");
        Gson gson = new Gson();
        setPingStructureArrayList((ArrayList<PingStructure>) gson.fromJson(serializedString, type));
        */
    }


}
