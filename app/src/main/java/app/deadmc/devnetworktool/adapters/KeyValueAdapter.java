package app.deadmc.devnetworktool.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.modules.KeyValueModel;

/**
 * Created by adanilov on 15.03.2017.
 */

public class KeyValueAdapter extends
        RecyclerView.Adapter<KeyValueAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private List<KeyValueModel> keyValueList;
    private Context context;
    private Fragment fragment;
    // Pass in the contact array into the constructor
    public KeyValueAdapter(Context context, List<KeyValueModel> keyValueList) {
        this.keyValueList = keyValueList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView keyTextView;
        public TextView valueTextView;
        public ViewHolder(View itemView) {
            super(itemView);

            keyTextView = (TextView) itemView.findViewById(R.id.keyTextView);
            valueTextView = (TextView) itemView.findViewById(R.id.valueTextView);
        }
    }


    @Override
    public KeyValueAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_key_value, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(KeyValueAdapter.ViewHolder viewHolder, final int position) {
        final KeyValueModel keyValueModel = keyValueList.get(position);
        final int currentPosition = position;
        viewHolder.keyTextView.setText(keyValueModel.getKey());
        viewHolder.valueTextView.setText(keyValueModel.getValue());

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongPress(keyValueModel,currentPosition);
                return true;
            }
        });


    }

    public void onLongPress(KeyValueModel keyValueModel, int position) {

    }

    @Override
    public void onItemDismiss(int position) {
        //keyValueList.get(position)
        keyValueList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(KeyValueModel keyValueModel) {
        keyValueList.add(keyValueModel);
        notifyItemInserted(keyValueList.size());
    }

    public void removeItem(int position) {
        keyValueList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(keyValueList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(keyValueList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public int getItemCount() {
        return keyValueList.size();
    }

    private Context getContext() {
        return context;
    }


}
