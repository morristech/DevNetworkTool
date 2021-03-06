package app.deadmc.devnetworktool.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.ArrayList
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.data.models.PingStructure
import kotlinx.android.synthetic.main.item_ping.view.*

class ReceivedPingsAdapter(private val context: Context, private val pingArrayList: ArrayList<PingStructure>) : RecyclerView.Adapter<ReceivedPingsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_ping, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView.text = pingArrayList[position].stringForRecyclerView(context)
    }

    override fun getItemCount(): Int {
        return pingArrayList.size
    }

    fun addItem(pingStructure: PingStructure) {
        pingArrayList.add(pingStructure)
        notifyItemChanged(pingArrayList.size-1)
    }

}
