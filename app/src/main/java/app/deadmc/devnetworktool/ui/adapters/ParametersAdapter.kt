package app.deadmc.devnetworktool.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.util.ArrayList

import app.deadmc.devnetworktool.R

class ParametersAdapter(internal var context: Context, internal var arrayList: ArrayList<Spanned>) : RecyclerView.Adapter<ParametersAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView

        init {
            text = view.findViewById(R.id.textView)
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentPosition = position
        viewHolder.text.text = arrayList[position]
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_parameter, viewGroup, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

}
