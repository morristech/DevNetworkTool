package app.deadmc.devnetworktool.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.adapters.JsonInputsAdapter;
import app.deadmc.devnetworktool.adapters.ReceivedMessagesAdapter;
import app.deadmc.devnetworktool.clients.BaseAbstractClient;
import app.deadmc.devnetworktool.helpers.DateTimeHelper;
import app.deadmc.devnetworktool.modules.JsonInput;
import app.deadmc.devnetworktool.modules.MessageHistory;
import app.deadmc.devnetworktool.modules.ReceivedMessage;
import app.deadmc.devnetworktool.system.SimpleDividerItemDecoration;
import app.deadmc.devnetworktool.system.SimpleItemTouchHelperCallback;

/**
 * Created by Feren on 15.06.2016.
 */
public class WorkingConnectionFragment2 extends ParentFragment {

    private Button buttonSend;
    private Button buttonAdd;
    private EditText messageEditText;
    private RecyclerView recyclerViewMessages;
    private RecyclerView recyclerViewInputs;
    private SlidingUpPanelLayout slidingLayout;
    private RelativeLayout errorRelativeLayout;
    private RelativeLayout loadingRelativeLayout;

    private String ipAddress;
    private BaseAbstractClient abstractClient;
    private ArrayList<JsonInput> jsonInputArrayList;
    private ArrayList<ReceivedMessage> receivedMessageArrayList;
    private JsonInputsAdapter jsonInputsAdapter;
    private ReceivedMessagesAdapter receivedMessagesAdapter;
    private int port;
    private boolean isThreadCreated = false;
    private boolean errorResulted = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_working_connection, container, false);

        Log.e("WCF", "onCreateView");

        restoreState(savedInstanceState);
        initElements();
        /*
        if (!isThreadCreated) {
            mainActivity2.startConnectionClient(typeOfFragment, ipAddress, port);
            mainActivity2.doBindService();
        } else {
            mainActivity2.doBindService();
        }
        */

        if (abstractClient != null) {
            successfulCallback();
        }

        return myFragmentView;
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Log.e("savedInstanceState", "is not null");
            ipAddress = savedInstanceState.getString("ipAddress");
            port = savedInstanceState.getInt("port");
            typeOfFragment = savedInstanceState.getString("typeOfFragment");
            isThreadCreated = savedInstanceState.getBoolean("isThreadCreated");
        }
    }

    public void initConnection(BaseAbstractClient baseAbstractClient) {
        this.abstractClient = baseAbstractClient;
    }

    @Override
    public void onResume() {

        super.onResume();

        showCloseConnectionAlert();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("ipAddress", ipAddress);
        outState.putInt("port", port);
        outState.putBoolean("isThreadCreated", isThreadCreated);
    }

    @Override
    public void initElements() {
        super.initElements();
        buttonSend = (Button) myFragmentView.findViewById(R.id.buttonSend);
        buttonAdd = (Button) myFragmentView.findViewById(R.id.buttonAdd);
        messageEditText = (EditText) myFragmentView.findViewById(R.id.messageEditText);
        recyclerViewMessages = (RecyclerView) myFragmentView.findViewById(R.id.recyclerViewMessages);
        recyclerViewInputs = (RecyclerView) myFragmentView.findViewById(R.id.recyclerViewInputs);
        slidingLayout = (SlidingUpPanelLayout) myFragmentView.findViewById(R.id.slidingLayout);
        errorRelativeLayout = (RelativeLayout) myFragmentView.findViewById(R.id.errorRelativeLayout);
        loadingRelativeLayout = (RelativeLayout) myFragmentView.findViewById(R.id.loadingRelativeLayout);

        //slidingLayout.addPanelSlideListener(onSlideListener());

        initRecyclerViewInputs();
        initRecyclerViewMessages();


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonInputArrayList.add(new JsonInput("", ""));
                jsonInputsAdapter.notifyItemInserted(jsonInputArrayList.size() - 1);
            }
        });

        slidingLayout.setVisibility(View.GONE);
        errorRelativeLayout.setVisibility(View.GONE);
        loadingRelativeLayout.setVisibility(View.VISIBLE);
    }


    private void initRecyclerViewInputs() {
        jsonInputArrayList = JsonInput.createJsonInputsList(1);
        jsonInputsAdapter = new JsonInputsAdapter(getContext(), jsonInputArrayList, messageEditText);
        recyclerViewInputs.setAdapter(jsonInputsAdapter);
        recyclerViewInputs.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(jsonInputsAdapter, true);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewInputs);
    }

    private void initRecyclerViewMessages() {
        receivedMessageArrayList = ReceivedMessage.createReceivedMessageList(0);
        receivedMessagesAdapter = new ReceivedMessagesAdapter(getContext(), receivedMessageArrayList, this);
        recyclerViewMessages.setAdapter(receivedMessagesAdapter);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(receivedMessagesAdapter, false);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewMessages);
        recyclerViewMessages.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        fillReceivedMessageList();
    }

    private void fillReceivedMessageList() {
        Select messageHistoryQuery = Select.from(MessageHistory.class)
                .where(Condition.prop("type").eq(typeOfFragment),
                        Condition.prop("ip_address").eq(ipAddress),
                        Condition.prop("port").eq(String.valueOf(port)));
        ArrayList<MessageHistory> arrayListMessageHistory = new ArrayList<>(messageHistoryQuery.list());
        for (MessageHistory messageHistory : arrayListMessageHistory) {
            receivedMessageArrayList.add(new ReceivedMessage(messageHistory.getMessage(),
                    DateTimeHelper.getTimeFromTimestamp(messageHistory.getTimeAdded()), messageHistory.getId(), messageHistory.isFromServer()));
            receivedMessagesAdapter.notifyItemInserted(receivedMessageArrayList.size() - 1);
        }
    }


    private void sendMessage() {
        if (abstractClient == null)
            return;
        String message = messageEditText.getText().toString();
        if (message.length() > 0)
            abstractClient.sendMessage(message);
        else
            Toast.makeText(getContext(), R.string.string_is_empty, Toast.LENGTH_SHORT).show();
        Log.e("workConnection", message + " " + typeOfFragment);
    }


    public void addLineToAdapter(String line, long id, final boolean fromServer) {
        final String message = line;
        final long finalId = id;

        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    receivedMessageArrayList.add(new ReceivedMessage(message, DateTimeHelper.getCurrentTime(), finalId, fromServer));
                    receivedMessagesAdapter.notifyItemInserted(receivedMessageArrayList.size() - 1);
                }
            });
        } catch (NullPointerException e) {
        }

    }

    public void errorCallback() {
        Log.e("error", "callback");
        errorResulted = true;
        slidingLayout.setVisibility(View.GONE);
        loadingRelativeLayout.setVisibility(View.GONE);
        errorRelativeLayout.setVisibility(View.VISIBLE);
    }

    public void successfulCallback() {
        Log.e("success", "callback");
        errorResulted = false;
        slidingLayout.setVisibility(View.VISIBLE);
        loadingRelativeLayout.setVisibility(View.GONE);
        errorRelativeLayout.setVisibility(View.GONE);
    }

    private void showCloseConnectionAlert() {
        Log.e("ConnectionAlert", "errorResulted = " + errorResulted);
        if (errorResulted)
            return;

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.AppTheme_Dialog_Alert);
                    alertDialogBuilder.setMessage(R.string.alert_close_connection);
                    alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mainActivity2.stopService();
                            getActivity().onBackPressed();
                        }
                    });

                    alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    alertDialog.show();

                    return true;

                } else {
                    Log.e("showCloseConnection", "not one of these actions");
                }

                return false;
            }
        });
    }

    /*
     ------------------------- Listeners ----------------------------------------
    */


}


