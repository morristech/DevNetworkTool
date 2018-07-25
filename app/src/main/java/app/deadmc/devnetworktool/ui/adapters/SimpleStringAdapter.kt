package app.deadmc.devnetworktool.ui.adapters

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.data.models.SimpleString
import java.util.*

class SimpleStringAdapter(private var context: Context, private var arrayList: ArrayList<SimpleString>) : RecyclerView.Adapter<SimpleStringAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text = view.findViewById<TextView>(R.id.textView)!!
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentPosition = position
        viewHolder.text.text = arrayList[position].text
        if (arrayList[position].isBold)
            viewHolder.text.setTypeface(null, Typeface.BOLD)
        else
            viewHolder.text.setTypeface(null, Typeface.NORMAL)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_ping, viewGroup, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

}
