package app.deadmc.devnetworktool.fragments.ping;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.adapters.SimpleStringAdapter;
import app.deadmc.devnetworktool.modules.PingStats;
import app.deadmc.devnetworktool.modules.PingStructure;
import app.deadmc.devnetworktool.modules.SimpleString;
import app.deadmc.devnetworktool.system.SimpleDividerItemDecoration;

/**
 * Created by Feren on 14.01.2017.
 */
public class PingStatsPageFragment extends BasePingFragment {

    private String currentUrl;
    private RecyclerView recyclerView;
    private PingStats pingStats;
    private SimpleStringAdapter simpleStringAdapter;
    private ArrayList<SimpleString> simpleStringArrayList;
    private LinearLayoutManager linearLayoutManager;

    public PingStatsPageFragment() {
        // Required empty public constructor
        Log.e("PingStats","default constructor called");
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    @Override
    public void addPingStructure(PingStructure pingStructure, boolean canUpdate){
        Log.e("pingStats","add ping structure "+canUpdate);
        if (pingStats == null) {
            Log.e("pingStats","pingStats is null");
            return;
        }
        pingStats.setIpAddress(pingStructure.getIpAddress());
        pingStats.setTtl(pingStructure.getTtl());
        pingStats.addPing(pingStructure.getPing());
        if (canUpdate)
            setPingStats(false);
        //simpleStringAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshFragment(ArrayList<PingStructure> arrayList) {
        try {
            setPingStructureArrayList(arrayList);
            Log.e("refreshStats", "count of elements " + pingStructureArrayList.size());
            setPingStats(true);
        } catch (NullPointerException e) {}
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setMyFragmentView(inflater.inflate(R.layout.fragment_pager_recyclerview, container, false));
        restoreState(savedInstanceState);
        initElements();
        // Inflate the layout for this fragment
        return getMyFragmentView();
    }

    private void initElements() {
        initRecyclerView();
        initPingList();
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) getMyFragmentView().findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
    }

    private void initPingList() {

        simpleStringArrayList = new ArrayList<>();
        simpleStringAdapter = new SimpleStringAdapter(getActivity(),simpleStringArrayList);
        recyclerView.setAdapter(simpleStringAdapter);
        setPingStats(true);
    }

    private void setPingStats(boolean refreshAll) {
        Log.e("setPingStats","called");
        if (pingStructureArrayList == null)
            return;
        if (pingStats == null || refreshAll) {
            pingStats = new PingStats();
            for (PingStructure pingStructure: pingStructureArrayList) {
                pingStats.setIpAddress(pingStructure.getIpAddress());
                pingStats.setTtl(pingStructure.getTtl());
                pingStats.addPing(pingStructure.getPing());
            }
        }
        simpleStringArrayList.clear();
        simpleStringArrayList.add(new SimpleString(getString(R.string.general),true));
        simpleStringArrayList.add(new SimpleString(getString(R.string.url)+" : "+currentUrl));
        simpleStringArrayList.add(new SimpleString(getString(R.string.ip_address)+" : "+pingStats.getIpAddress()));
        simpleStringArrayList.add(new SimpleString(getString(R.string.ttl)+" : "+pingStats.getTtl()));

        simpleStringArrayList.add(new SimpleString(getString(R.string.stats),true));
        simpleStringArrayList.add(new SimpleString(getString(R.string.sended)+" : "+pingStats.getSended()));
        simpleStringArrayList.add(new SimpleString(getString(R.string.successful)+" : "+pingStats.getSuccessful()));
        simpleStringArrayList.add(new SimpleString(getString(R.string.failed)+" : "+pingStats.getFailed()));

        simpleStringArrayList.add(new SimpleString(getString(R.string.ping),true));
        simpleStringArrayList.add(new SimpleString(getString(R.string.max)+" : "+pingStats.getMax()+" "+getString(R.string.ms)));
        simpleStringArrayList.add(new SimpleString(getString(R.string.min)+" : "+pingStats.getMin()+" "+getString(R.string.ms)));
        simpleStringArrayList.add(new SimpleString(getString(R.string.average)+" : "+pingStats.getAverage()+" "+getString(R.string.ms)));


        //
        if (simpleStringAdapter != null)
            simpleStringAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("onSaveInstanceState", "save");
        outState.putString("currentUrl", currentUrl);
        outState.putString("pingStructureList", new Gson().toJson(pingStructureArrayList));
        outState.putString("pingStats",new Gson().toJson(pingStats));
    }

    private void restoreState(Bundle bundle) {
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
        pingStructureArrayList = gson.fromJson(serializedString,type);

        type = new TypeToken<PingStats>(){}.getType();
        serializedString = bundle.getString("pingStats");
        pingStats = gson.fromJson(serializedString, type);

        Log.e("PingStats","pingStats "+pingStats.getIpAddress());
    }


}

