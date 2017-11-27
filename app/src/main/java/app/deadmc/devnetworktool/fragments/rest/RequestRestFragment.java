package app.deadmc.devnetworktool.fragments.rest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.adapters.KeyValueAdapter;
import app.deadmc.devnetworktool.fragments.ParentFragment;
import app.deadmc.devnetworktool.helpers.AllHeaders;
import app.deadmc.devnetworktool.modules.KeyValueModel;
import app.deadmc.devnetworktool.modules.RestRequestHistory;
import app.deadmc.devnetworktool.system.ItemTouchCallback;
import app.deadmc.devnetworktool.system.SimpleDividerItemDecoration;

/**
 * Created by adanilov on 14.03.2017.
 */

public class RequestRestFragment extends ParentFragment {

    private Spinner methodSpinner;
    private Button sendButton;
    private Button addHeaderButton;
    private EditText urlEditText;
    private RestFragment restFragment;
    private String currentUrl;
    private String currentMethod = "GET";
    private RecyclerView headersRecyclerView;
    private KeyValueAdapter keyValueAdapterHeaders;
    private ArrayList<KeyValueModel> headersArrayList;
    private RecyclerView requestRecyclerView;
    private KeyValueAdapter keyValueAdapterRequest;
    private ArrayList<KeyValueModel> requestArrayList;

    //for dialog
    private AlertDialog.Builder alertDialogBuilder;
    private View alertView;
    private EditText editTextKey;
    private EditText editTextValue;
    private Spinner keySpinner;
    private Spinner valueSpinner;
    private ArrayList<String> keyParamsList;
    private ArrayList<String> valueParamsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_rest_request, container, false);
        restoreState(savedInstanceState);
        initElements();
        return myFragmentView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentUrl", currentUrl);
        outState.putString("currentMethod", currentMethod);
        outState.putString("headersArrayList", new Gson().toJson(headersArrayList));
        outState.putString("requestArrayList", new Gson().toJson(requestArrayList));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void restoreState(Bundle bundle) {
        if (bundle == null)
            return;
        if (!bundle.containsKey("currentMethod"))
            return;
        if (!bundle.containsKey("currentUrl"))
            return;
        Log.e("restoreState", "restore is ok");
        currentUrl = bundle.getString("currentUrl");
        currentMethod = bundle.getString("currentMethod");
        String headersArrayListString = bundle.getString("headersArrayList");
        String requestArrayListString = bundle.getString("requestArrayList");
        Type type = new TypeToken<ArrayList<KeyValueModel>>() {
        }.getType();

        Gson gson = new Gson();
        headersArrayList = gson.fromJson(headersArrayListString, type);
        requestArrayList = gson.fromJson(requestArrayListString, type);

    }


    @Override
    public void initElements() {
        //super.initElements();
        initButtons();
        initEditText();
        initSpinner();
        initHeadersRecyclerView();
        initRequestRecyclerView();
        initSwipe();
        initOtherActions();
    }

    public void setRestFragment(RestFragment restFragment) {
        this.restFragment = restFragment;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public void setRequestHistory(RestRequestHistory restRequestHistory) {
        currentUrl = restRequestHistory.getUrl();
        currentMethod = restRequestHistory.getMethod();
        headersArrayList = restRequestHistory.getHeaders();
        requestArrayList = restRequestHistory.getRequests();
        initElements();
    }

    private void initOtherActions() {
        setTitle();
    }


    private void initButtons() {
        sendButton = (Button) myFragmentView.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    restFragment.sendRequest(urlEditText.getText().toString(), currentMethod, collectHeaders(), collectRequests());
                    RestRequestHistory restRequestHistory = new RestRequestHistory(urlEditText.getText().toString(), currentMethod, headersArrayList, requestArrayList);
                    restRequestHistory.save();
                } catch (Exception e) {
                }
            }
        });

        addHeaderButton = (Button) myFragmentView.findViewById(R.id.addHeaderButton);
        addHeaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialogForHeader();
            }
        });

        addHeaderButton = (Button) myFragmentView.findViewById(R.id.addRequestButton);
        addHeaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialogForRequest();
            }
        });
    }

    private void initEditText() {
        urlEditText = (EditText) myFragmentView.findViewById(R.id.urlEditText);
        if (!currentUrl.contains("http")) {
            currentUrl = "http://" + currentUrl;
        }
        urlEditText.setText(currentUrl);
    }

    private void initSpinner() {
        methodSpinner = (Spinner) myFragmentView.findViewById(R.id.materialSpinner);
        methodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentMethod = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void initHeadersRecyclerView() {
        headersRecyclerView = (RecyclerView) myFragmentView.findViewById(R.id.headersRecyclerView);
        initRecyclerView(headersRecyclerView);
        if (headersArrayList == null)
            headersArrayList = new ArrayList<>();
        keyValueAdapterHeaders = new KeyValueAdapter(getContext(), headersArrayList) {
            @Override
            public void onLongPress(KeyValueModel keyValueModel, int position) {
                initDialogForEditHeader(keyValueModel, position);
            }
        };

        headersRecyclerView.setAdapter(keyValueAdapterHeaders);

    }

    private void initRequestRecyclerView() {
        requestRecyclerView = (RecyclerView) myFragmentView.findViewById(R.id.requestRecyclerView);
        initRecyclerView(requestRecyclerView);
        if (requestArrayList == null)
            requestArrayList = new ArrayList<>();
        keyValueAdapterRequest = new KeyValueAdapter(getContext(), requestArrayList) {
            @Override
            public void onLongPress(KeyValueModel keyValueModel, int position) {
                initDialogForEditRequest(keyValueModel, position);
            }
        };
        requestRecyclerView.setAdapter(keyValueAdapterRequest);
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        //recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void initSwipe() {
        ItemTouchHelper itemTouchHelperHeader = new ItemTouchHelper(new ItemTouchCallback(getActivity()) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                keyValueAdapterHeaders.removeItem(position);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
        });
        itemTouchHelperHeader.attachToRecyclerView(headersRecyclerView);

        ItemTouchHelper itemTouchHelperRequest = new ItemTouchHelper(new ItemTouchCallback(getActivity()) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                keyValueAdapterRequest.removeItem(position);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
        });
        itemTouchHelperRequest.attachToRecyclerView(requestRecyclerView);
    }

    private HashMap<String, String> collectHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        String value;
        for (KeyValueModel keyValueModel : headersArrayList) {
            if (headers.containsKey(keyValueModel.getKey())) {
                value = headers.get(keyValueModel.getKey()) + "," + keyValueModel.getValue();
            } else {
                value = keyValueModel.getValue();
            }
            headers.put(keyValueModel.getKey(), value);
        }
        return headers;
    }

    private HashMap<String, String> collectRequests() {
        HashMap<String, String> requests = new HashMap<>();
        for (KeyValueModel keyValueModel : requestArrayList) {
            requests.put(keyValueModel.getKey(), keyValueModel.getValue());
        }

        return requests;
    }

    ///////////////////////////////////////////////////Dialog Part

    /**
     * Dialog where you can add new header
     */
    private void initDialogForHeader() {
        initDialogVariablesHeader();
        alertDialogBuilder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                KeyValueModel keyValueModel = new KeyValueModel();
                keyValueModel.setKey(editTextKey.getText().toString());
                keyValueModel.setValue(editTextValue.getText().toString());
                keyValueAdapterHeaders.addItem(keyValueModel);
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();

    }

    private void initDialogForEditHeader(KeyValueModel keyValueModel, int position) {
        final KeyValueModel keyValueModelFinal = keyValueModel;
        final int positionFinal = position;

        initDialogVariablesHeader();
        fillDialogVariables(keyValueModel, true);
        alertDialogBuilder.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                keyValueModelFinal.setKey(editTextKey.getText().toString());
                keyValueModelFinal.setValue(editTextValue.getText().toString());
                keyValueAdapterHeaders.notifyItemChanged(positionFinal);
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }


    /**
     * Dialog where you can add new request
     */
    private void initDialogForRequest() {
        initDialogVariablesRequest();

        alertDialogBuilder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                KeyValueModel keyValueModel = new KeyValueModel();
                keyValueModel.setKey(editTextKey.getText().toString());
                keyValueModel.setValue(editTextValue.getText().toString());
                keyValueAdapterRequest.addItem(keyValueModel);
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    private void initDialogForEditRequest(KeyValueModel keyValueModel, int position) {
        final KeyValueModel keyValueModelFinal = keyValueModel;
        final int positionFinal = position;

        initDialogVariablesRequest();
        fillDialogVariables(keyValueModel, false);
        alertDialogBuilder.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                keyValueModelFinal.setKey(editTextKey.getText().toString());
                keyValueModelFinal.setValue(editTextValue.getText().toString());
                keyValueAdapterRequest.notifyItemChanged(positionFinal);
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();

    }


    /**
     * Init all views in dialog
     */
    private void initDialogVariablesHeader() {
        alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.AppTheme_Dialog_Alert);
        alertView = getActivity().getLayoutInflater().inflate(R.layout.add_key_value_layout_header, null);
        alertDialogBuilder.setView(alertView);
        editTextKey = (EditText) alertView.findViewById(R.id.editTextKey);
        editTextValue = (EditText) alertView.findViewById(R.id.editTextValue);
        keySpinner = (Spinner) alertView.findViewById(R.id.materialSpinnerHeaderKey);
        valueSpinner = (Spinner) alertView.findViewById(R.id.materialSpinnerHeaderValue);
        setDialogSpinnerKey();
    }

    /**
     * Init all views in dialog
     */
    private void initDialogVariablesRequest() {
        alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.AppTheme_Dialog_Alert);
        alertView = getActivity().getLayoutInflater().inflate(R.layout.add_key_value_layout_request, null);
        alertDialogBuilder.setView(alertView);
        editTextKey = (EditText) alertView.findViewById(R.id.editTextKey);
        editTextValue = (EditText) alertView.findViewById(R.id.editTextValue);
    }

    private void fillDialogVariables(KeyValueModel keyValueModel, boolean hasSpinners) {
        editTextKey.setText(keyValueModel.getKey());
        editTextValue.setText(keyValueModel.getValue());
        if (hasSpinners) {
            int keyIndex = keyParamsList.indexOf(keyValueModel.getKey());
            if (keyIndex > 0) {
                keySpinner.setSelection(keyIndex);
                valueSpinner.setVisibility(View.VISIBLE);
                setDialogSpinnerValue(keyValueModel.getKey());
                int valueIndex = valueParamsList.indexOf(keyValueModel.getValue());
                if (valueIndex > 0) {
                    valueSpinner.setSelection(valueIndex);
                } else {
                    valueSpinner.setSelection(0);
                }

            } else {
                keySpinner.setSelection(0);
                valueSpinner.setVisibility(View.GONE);
            }
        }
    }

    private void setDialogSpinnerKey() {
        keyParamsList = new ArrayList<String>();
        keyParamsList.add(getString(R.string.custom_header));
        keyParamsList.addAll(AllHeaders.getHeadersHashmap().keySet());
        ArrayAdapter<String> keySpinnerAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, keyParamsList);
        keySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        keySpinner.setVisibility(View.VISIBLE);
        keySpinner.setAdapter(keySpinnerAdapter);
        keySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals(getString(R.string.custom_header))) {
                    editTextKey.setText("");
                    valueSpinner.setVisibility(View.GONE);
                } else {
                    editTextKey.setText(item);
                    setDialogSpinnerValue(item);
                }
                editTextValue.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setDialogSpinnerValue(String key) {
        valueParamsList = new ArrayList<String>();
        valueParamsList.add(getString(R.string.custom_value));
        valueParamsList.addAll(AllHeaders.getHeadersHashmap().get(key));
        if (valueParamsList.size() == 1) {
            valueSpinner.setVisibility(View.GONE);
            return;
        }
        ArrayAdapter<String> valueSpinnerAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, valueParamsList);
        valueSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valueSpinner.setVisibility(View.VISIBLE);
        valueSpinner.setAdapter(valueSpinnerAdapter);
        valueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals(getString(R.string.custom_value))) {
                    editTextValue.setText("");
                } else {
                    editTextValue.setText(item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
