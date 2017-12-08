package app.deadmc.devnetworktool.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.deadmc.devnetworktool.R;

public class ParametersAdapter extends RecyclerView.Adapter<ParametersAdapter.ViewHolder> {

    ArrayList<Spanned> arrayList;
    Context context;

    public ParametersAdapter(Context context, ArrayList<Spanned> arrayList) {
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
    public void onBindViewHolder(ParametersAdapter.ViewHolder viewHolder, int position) {
        final int currentPosition = position;
        viewHolder.text.setText(arrayList.get(position));
    }

    @Override
    public ParametersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_parameter, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
