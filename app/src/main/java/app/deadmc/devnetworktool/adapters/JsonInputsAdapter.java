package app.deadmc.devnetworktool.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.modules.JsonInput;

/**
 * Created by Feren on 24.09.2016.
 */
public class JsonInputsAdapter extends
        RecyclerView.Adapter<JsonInputsAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private List<JsonInput> jsonInputsList;
    private Context context;
    private EditText fullJsonEditText;
    // Pass in the contact array into the constructor
    public JsonInputsAdapter(Context context, List<JsonInput> jsonInputsList, EditText fullJsonEditText) {
        this.jsonInputsList = jsonInputsList;
        this.context = context;
        this.fullJsonEditText = fullJsonEditText;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText editTextKey;
        public EditText editTextValue;
        public ViewHolder(View itemView) {
            super(itemView);
            editTextKey = (EditText) itemView.findViewById(R.id.editTextKey);
            editTextValue = (EditText) itemView.findViewById(R.id.editTextValue);
        }


    }

    private void setUpdatedJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            for (JsonInput jsonInput : jsonInputsList) {
                if (!jsonInput.getKey().isEmpty())
                    jsonObject.put(jsonInput.getKey(), jsonInput.getValue());
            }
        } catch (JSONException e) {}
        fullJsonEditText.setText(jsonObject.toString());
    }

    @Override
    public JsonInputsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_recycler_view_inputs, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(JsonInputsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final JsonInput jsonInput = jsonInputsList.get(position);

        viewHolder.editTextKey.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                jsonInput.setKey(s.toString());
                if (!s.equals("")) {
                    setUpdatedJson();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        viewHolder.editTextValue.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                jsonInput.setValue(s.toString());
                if (!s.equals("")) {
                    setUpdatedJson();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onItemDismiss(int position) {
        jsonInputsList.remove(position);
        notifyItemRemoved(position);
        setUpdatedJson();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(jsonInputsList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(jsonInputsList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return jsonInputsList.size();
    }

    private Context getContext() {
        return context;
    }
}