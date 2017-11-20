package app.deadmc.devnetworktool.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.modules.SimpleString;

/**
 * Created by Feren on 30.11.2016.
 */
public class SimpleStringAdapter extends RecyclerView.Adapter<SimpleStringAdapter.ViewHolder> {

    ArrayList<SimpleString> arrayList;
    Context context;

    public SimpleStringAdapter(Context context, ArrayList<SimpleString> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView text;
        public ViewHolder(View view) {
            super(view);
            text = (TextView)view.findViewById(R.id.textView);
        }
    }

    @Override
    public void onBindViewHolder(SimpleStringAdapter.ViewHolder viewHolder, int position) {
        final int currentPosition = position;
        viewHolder.text.setText(arrayList.get(position).getText());
        if (arrayList.get(position).isBold())
            viewHolder.text.setTypeface(null, Typeface.BOLD);
        else
            viewHolder.text.setTypeface(null, Typeface.NORMAL);
    }

    @Override
    public SimpleStringAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ping, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
