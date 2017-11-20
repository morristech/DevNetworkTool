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
import app.deadmc.devnetworktool.adapters.ReceivedPingsAdapter;
import app.deadmc.devnetworktool.modules.PingStructure;
import app.deadmc.devnetworktool.system.SimpleDividerItemDecoration;

/**
 * Created by Feren on 12.01.2017.
 */
public class PingRawPageFragment extends BasePingFragment {

    private RecyclerView recyclerView;
    private ReceivedPingsAdapter receivedMessagesAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisiblePosition = 0;

    public PingRawPageFragment() {
        // Required empty public constructor
        Log.e("PingRawPageFragment","new created");
    }


    @Override
    public void addPingStructure(PingStructure pingStructure, boolean canUpdate){
        //Log.e("addPingStructure","RAW");

        if (pingStructureArrayList == null) {
            Log.e("addPingStructure","pingStructureArrayList IS NULL");
            return;
        }
        pingStructureArrayList.add(pingStructure);
        if (canUpdate) {
            receivedMessagesAdapter.notifyDataSetChanged();
            if (linearLayoutManager.findLastVisibleItemPosition() >= pingStructureArrayList.size() - 3)
                recyclerView.smoothScrollToPosition(pingStructureArrayList.size() - 1);
            else {
                //Log.e("raw","linearLayoutManager.findLastVisibleItemPosition() = "+linearLayoutManager.findLastVisibleItemPosition());
                //Log.e("raw","pingStructureArrayList.size() = "+pingStructureArrayList.size());
            }
        }

    }

    @Override
    public void refreshFragment(ArrayList<PingStructure> arrayList){
        try {
            setPingStructureArrayList(arrayList);
            receivedMessagesAdapter = new ReceivedPingsAdapter(getActivity(), pingStructureArrayList);
            recyclerView.setAdapter(receivedMessagesAdapter);
            receivedMessagesAdapter.notifyDataSetChanged();
            if (linearLayoutManager.findLastVisibleItemPosition() == pingStructureArrayList.size() - 2)
                recyclerView.smoothScrollToPosition(pingStructureArrayList.size() - 1);
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
        Log.e("PingRawPageFragment", "initRecyclerView");
        recyclerView = (RecyclerView) getMyFragmentView().findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

    }

    private void initPingList() {
        if (pingStructureArrayList == null)
            return;
        //Log.e("PingRaw","pingStructureArrayList size = "+pingStructureArrayList.size());
        receivedMessagesAdapter = new ReceivedPingsAdapter(getActivity(), pingStructureArrayList);
        recyclerView.setAdapter(receivedMessagesAdapter);
        try {
            if (lastVisiblePosition != 0 && pingStructureArrayList.size() > lastVisiblePosition)
                recyclerView.smoothScrollToPosition(lastVisiblePosition);
        } catch (IllegalArgumentException e) {
            //Log.e("error",Log.getStackTraceString(e));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.e("onSaveInstanceState", "save");
        outState.putString("pingStructureList", new Gson().toJson(pingStructureArrayList));
        outState.putInt("scrollPosition", linearLayoutManager.findLastVisibleItemPosition());
    }

    private void restoreState(Bundle bundle) {
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
        pingStructureArrayList = gson.fromJson(serializedString,type);
    }


}
