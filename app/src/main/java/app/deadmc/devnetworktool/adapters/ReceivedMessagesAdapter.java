package app.deadmc.devnetworktool.adapters;

/**
 * Created by Feren on 25.09.2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.activities.FullViewActivity;
import app.deadmc.devnetworktool.fragments.FormattedJsonFragment;
import app.deadmc.devnetworktool.modules.ReceivedMessage;

/**
 * Created by Feren on 24.09.2016.
 */
public class ReceivedMessagesAdapter extends
        RecyclerView.Adapter<ReceivedMessagesAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private List<ReceivedMessage> receivedMessageList;
    private Context context;
    private Fragment fragment;
    // Pass in the contact array into the constructor
    public ReceivedMessagesAdapter(Context context, List<ReceivedMessage> receivedMessageList, Fragment fragment) {
        this.receivedMessageList = receivedMessageList;
        this.context = context;
        this.fragment = fragment;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTextView;
        public TextView timeTextView;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);

            textTextView = (TextView) itemView.findViewById(R.id.textViewText);
            timeTextView = (TextView) itemView.findViewById(R.id.textViewTime);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }


    @Override
    public ReceivedMessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_received_message, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReceivedMessagesAdapter.ViewHolder viewHolder, final int position) {
        final ReceivedMessage receivedMessage = receivedMessageList.get(position);
        viewHolder.textTextView.setText(receivedMessage.getText());
        viewHolder.timeTextView.setText(receivedMessage.getTime());
        viewHolder.linearLayout.setBackgroundColor(receivedMessage.isFromServer() ? ContextCompat.getColor(context,R.color.lightGrey2) : ContextCompat.getColor(context,R.color.white));
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("long press", "position = " + position);
                Bundle bundle = new Bundle();
                bundle.putString("text",receivedMessage.getText());
                Intent intent = new Intent(getContext(), FullViewActivity.class);
                intent.putExtras(bundle);
                ((Activity) getContext()).startActivity(intent);
                return true;
            }
        });


    }

    @Override
    public void onItemDismiss(int position) {
        //receivedMessageList.get(position)
        receivedMessageList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(receivedMessageList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(receivedMessageList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public int getItemCount() {
        return receivedMessageList.size();
    }

    private Context getContext() {
        return context;
    }
}