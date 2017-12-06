package app.deadmc.devnetworktool.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.helpers.DateTimeHelper;
import app.deadmc.devnetworktool.models.RestRequestHistory;

/**
 * Created by adanilov on 23.03.2017.
 */

public class RestRequestHistoryAdapter extends RecyclerView.Adapter<RestRequestHistoryAdapter.ViewHolder> {

    ArrayList<RestRequestHistory> arrayList;
    Context context;

    public RestRequestHistoryAdapter(Context context, ArrayList<RestRequestHistory> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView urlTextView;
        private TextView methodTextView;
        private TextView dateTextView;
        public ViewHolder(View view) {
            super(view);
            urlTextView = (TextView)view.findViewById(R.id.urlTextView);
            methodTextView = (TextView)view.findViewById(R.id.methodTextView);
            dateTextView = (TextView)view.findViewById(R.id.dateTextView);
        }
    }

    @Override
    public void onBindViewHolder(RestRequestHistoryAdapter.ViewHolder viewHolder, int position) {
        final int currentPosition = position;
        viewHolder.urlTextView.setText(arrayList.get(position).getUrl());
        viewHolder.methodTextView.setText(arrayList.get(position).getMethod());
        viewHolder.dateTextView.setText(DateTimeHelper.getDateAndTime(arrayList.get(position).getTimeLastUsage()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem(arrayList.get(currentPosition), currentPosition);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("onLongClick", "clicked");
                onLongClickItem(arrayList.get(currentPosition), currentPosition);
                return true;
            }
        });
    }

    @Override
    public RestRequestHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rest_request_history, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addItem(RestRequestHistory restRequestHistory) {
        arrayList.add(restRequestHistory);
        notifyItemInserted(arrayList.size());
    }

    public void removeItem(int position) {
        RestRequestHistory restRequestHistory = arrayList.get(position);
        restRequestHistory.delete();
        arrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, arrayList.size());
    }

    public void editItem(int position) {
        notifyItemChanged(position);
    }


    public void onLongClickItem(RestRequestHistory restRequestHistory, int position) {

    }

    public void onClickItem(RestRequestHistory restRequestHistory, int position) {

    }




}
