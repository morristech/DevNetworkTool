package app.deadmc.devnetworktool.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.adapters.UrlHistoryAdapter;
import app.deadmc.devnetworktool.constants.DevConsts;
import app.deadmc.devnetworktool.fragments.ping.PingFragment;
import app.deadmc.devnetworktool.fragments.rest.RestFragment;
import app.deadmc.devnetworktool.modules.ConnectionHistory;
import app.deadmc.devnetworktool.system.ItemTouchCallback;
import app.deadmc.devnetworktool.system.SimpleDividerItemDecoration;

public class HistoryOfUrlsFragment extends ParentFragment {

    private RecyclerView recyclerViewHistory;
    private FloatingActionButton floatingActionButton;
    private EditText editTextIpAddress;
    private ArrayList<ConnectionHistory> arrayListConnectionHistory;
    private AlertDialog.Builder alertDialog;
    private View alertView;
    private UrlHistoryAdapter urlHistoryAdapter;

    public HistoryOfUrlsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_history_of_connections, container, false);
        initElements();
        return myFragmentView;
    }

    @Override
    public void initElements() {
        super.initElements();

        floatingActionButton = (FloatingActionButton) myFragmentView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialogForCreate();
            }
        });

        recyclerViewHistory = (RecyclerView) myFragmentView.findViewById(R.id.recyclerViewHistory);
        recyclerViewHistory.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewHistory.setLayoutManager(layoutManager);
        arrayListConnectionHistory = new ArrayList<>(ConnectionHistory.find(ConnectionHistory.class,"type = ?", typeOfFragment));
        urlHistoryAdapter = new UrlHistoryAdapter(getContext(), arrayListConnectionHistory, typeOfFragment) {
            @Override
            public void onLongClickItem(ConnectionHistory connectionHistory, int position) {
                super.onLongClickItem(connectionHistory, position);
                initDialogForEdit(connectionHistory, position);
            }

            @Override
            public void onClickItem(ConnectionHistory connectionHistory, int position) {
                super.onLongClickItem(connectionHistory, position);
                openFragment(connectionHistory);

            }
        };
        recyclerViewHistory.setAdapter(urlHistoryAdapter);
        recyclerViewHistory.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        urlHistoryAdapter.notifyDataSetChanged();
        initSwipe();

    }

    private void openFragment(ConnectionHistory connectionHistory) {
        switch (typeOfFragment) {
            case DevConsts.PING:
                PingFragment pingFragment = new PingFragment();
                pingFragment.setCurrentUrl(connectionHistory.getIpAddress());
                mainActivity2.openFragment(pingFragment, true);
                break;
            case DevConsts.REST:
                RestFragment restFragment = new RestFragment();
                restFragment.setCurrentUrl(connectionHistory.getIpAddress());
                mainActivity2.openFragment(restFragment, true);
                break;
        }

    }

    private void initSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchCallback(getActivity()) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                urlHistoryAdapter.removeItem(position);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewHistory);
    }

    ///////////////////////////////////////////////////Dialog Part

    /**
     * Dialog where you can create new connection
     */
    private void initDialogForCreate() {
        Log.e("initDialogForCreate", "clicked");
        initDialogVariables();
        alertDialog.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConnectionHistory connectionHistory = new ConnectionHistory();
                connectionHistory.setIpAddress(editTextIpAddress.getText().toString());
                connectionHistory.setType(typeOfFragment);
                connectionHistory.save();
                urlHistoryAdapter.addItem(connectionHistory);
                Log.e("item", "added");
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /**
     * Dialog where you can edit old connection
     */
    private void initDialogForEdit(ConnectionHistory connectionHistory, int position) {
        final ConnectionHistory connectionHistoryFinal = connectionHistory;
        final int positionFinal = position;
        initDialogVariables();
        editTextIpAddress.setText(connectionHistory.getIpAddress());

        alertDialog.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConnectionHistory connectionHistory = connectionHistoryFinal;
                connectionHistory.setIpAddress(editTextIpAddress.getText().toString());
                connectionHistory.save();
                urlHistoryAdapter.notifyItemChanged(positionFinal);
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /**
     * Init all views in dialog
     */
    private void initDialogVariables() {
        alertDialog = new AlertDialog.Builder(getContext(), R.style.AppTheme_Dialog_Alert);
        alertView = getLayoutInflater(null).inflate(R.layout.add_url_layout, null);
        alertDialog.setView(alertView);
        editTextIpAddress = (EditText) alertView.findViewById(R.id.editIpAddress);
    }


    /////////////////////////////////////////////////// end of Dialog part


}
