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
import app.deadmc.devnetworktool.modules.ConnectionHistory;


/**
 * Created by Feren on 02.10.2016.
 */
public class UrlHistoryAdapter extends RecyclerView.Adapter<UrlHistoryAdapter.ViewHolder> {
    private ArrayList<ConnectionHistory> urlArrayList;
    private Context context;
    private String typeOfConnection;

    public UrlHistoryAdapter(Context context, ArrayList<ConnectionHistory> urlArrayList, String typeOfConnection) {
        this.urlArrayList = urlArrayList;
        this.context = context;
        this.typeOfConnection = typeOfConnection;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ipAddressTextView;

        public ViewHolder(View view) {
            super(view);
            ipAddressTextView = (TextView) view.findViewById(R.id.ipAddressTextView);
        }
    }

    public void onLongClickItem(ConnectionHistory ConnectionHistory, int position) {

    }

    public void onClickItem(ConnectionHistory ConnectionHistory, int position) {

    }

    @Override
    public UrlHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_url_history, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UrlHistoryAdapter.ViewHolder viewHolder, int position) {
        final int currentPosition = position;
        viewHolder.ipAddressTextView.setText(urlArrayList.get(position).getIpAddress());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem(urlArrayList.get(currentPosition), currentPosition);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("onLongClick", "clicked");
                onLongClickItem(urlArrayList.get(currentPosition), currentPosition);
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return urlArrayList.size();
    }

    public void addItem(ConnectionHistory ConnectionHistory) {
        urlArrayList.add(ConnectionHistory);
        notifyItemInserted(urlArrayList.size());
    }

    public void removeItem(int position) {
        ConnectionHistory connectionHistory = urlArrayList.get(position);
        connectionHistory.delete();
        urlArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, urlArrayList.size());
    }

    public void editItem(int position) {
        notifyItemChanged(position);
    }

}