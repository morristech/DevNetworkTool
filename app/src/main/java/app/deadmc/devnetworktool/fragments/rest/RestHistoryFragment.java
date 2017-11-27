package app.deadmc.devnetworktool.fragments.rest;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.adapters.RestRequestHistoryAdapter;
import app.deadmc.devnetworktool.fragments.ParentFragment;
import app.deadmc.devnetworktool.modules.RestRequestHistory;
import app.deadmc.devnetworktool.system.ItemTouchCallback;
import app.deadmc.devnetworktool.system.SimpleDividerItemDecoration;

/**
 * Created by adanilov on 23.03.2017.
 */

public class RestHistoryFragment extends ParentFragment {

    private RecyclerView recyclerViewHistory;
    private ArrayList<RestRequestHistory> restRequestHistoryArrayList;
    private RestRequestHistoryAdapter restRequestHistoryAdapter;
    private RestFragment restFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_rest_history, container, false);
        initElements();
        return myFragmentView;
    }

    public void setRestFragment(RestFragment restFragment) {
        this.restFragment = restFragment;
    }

    @Override
    public void initElements() {

        recyclerViewHistory = (RecyclerView) myFragmentView.findViewById(R.id.recyclerViewHistory);
        recyclerViewHistory.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewHistory.setLayoutManager(layoutManager);
        restRequestHistoryArrayList = new ArrayList<>(RestRequestHistory.listAll(RestRequestHistory.class));
        restRequestHistoryAdapter = new RestRequestHistoryAdapter(getContext(), restRequestHistoryArrayList) {
            @Override
            public void onLongClickItem(RestRequestHistory restRequestHistory, int position) {
                super.onLongClickItem(restRequestHistory, position);

            }

            @Override
            public void onClickItem(RestRequestHistory restRequestHistory, int position) {
                try {
                    //restFragment.loadRestHistory(restRequestHistory);
                } catch (Exception e) {}
            }
        };

        recyclerViewHistory.setAdapter(restRequestHistoryAdapter);
        recyclerViewHistory.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        restRequestHistoryAdapter.notifyDataSetChanged();
        initSwipe();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchCallback(getActivity()) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                restRequestHistoryAdapter.removeItem(position);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewHistory);
    }


}
