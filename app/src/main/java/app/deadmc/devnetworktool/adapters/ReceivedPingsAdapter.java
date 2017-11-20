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
import app.deadmc.devnetworktool.modules.PingStructure;

/**
 * Created by Feren on 02.10.2016.
 */
public class ReceivedPingsAdapter extends RecyclerView.Adapter<ReceivedPingsAdapter.ViewHolder> {
    private ArrayList<PingStructure> pingArrayList;
    private Context context;

    public ReceivedPingsAdapter(Context context,ArrayList<PingStructure> pingArrayList) {
        this.pingArrayList = pingArrayList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewRaw;
        public ViewHolder(View view) {
            super(view);

            textViewRaw = (TextView)view.findViewById(R.id.textView);
        }
    }

    public void onLongClickItem(PingStructure pingStructure, int position) {

    }

    public void onClickItem(PingStructure pingStructure, int position) {

    }

    @Override
    public ReceivedPingsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ping, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReceivedPingsAdapter.ViewHolder viewHolder, int position) {
        final int currentPosition = position;
        viewHolder.textViewRaw.setText(pingArrayList.get(position).stringForRecyclerView(context));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem(pingArrayList.get(currentPosition), currentPosition);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("onLongClick", "clicked");
                onLongClickItem(pingArrayList.get(currentPosition), currentPosition);
                return true;
            }
        });
    }



    @Override
    public int getItemCount() {
        return pingArrayList.size();
    }

    public void addItem(PingStructure pingStructure) {
        pingArrayList.add(pingStructure);
        notifyItemInserted(pingArrayList.size());
    }

    public void removeItem(int position) {
        pingArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, pingArrayList.size());
    }

    public void editItem(int position) {
        notifyItemChanged(position);
    }

}
