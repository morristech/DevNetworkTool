package app.deadmc.devnetworktool.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.util.ArrayList

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.modules.ConnectionHistory
import app.deadmc.devnetworktool.views.ViewBinderHelper
import kotlinx.android.synthetic.main.item_connection_swipe.view.*
import kotlinx.android.synthetic.main.item_connection_history.view.*
import kotlinx.android.synthetic.main.item_edit_delete.view.*

abstract class ConnectionHistoryAdapter(private val connectionArrayList: ArrayList<ConnectionHistory>) : RecyclerView.Adapter<ConnectionHistoryAdapter.ViewHolder>() {

    protected val viewBinderHelper = ViewBinderHelper()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    abstract fun onClickItem(connectionHistory: ConnectionHistory, position: Int)

    abstract fun removeItemCallback(connectionHistory: ConnectionHistory)

    abstract fun editItemCallback(connectionHistory: ConnectionHistory,position: Int)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ConnectionHistoryAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_connection_swipe, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ConnectionHistoryAdapter.ViewHolder, position: Int) {
        viewHolder.itemView.nameTextView.text = connectionArrayList[position].name
        viewHolder.itemView.ipAddressAndPortTextView.text = connectionArrayList[position].ipAddress + " : " + connectionArrayList[position].port
        viewHolder.itemView.layoutConnectionHistory.setOnClickListener {
            Log.e("click","position "+position)
            onClickItem(connectionArrayList[position], position)
        }
        viewBinderHelper.bind(viewHolder.itemView.swipeRevealLayout, connectionArrayList[position].id.toString())
        viewHolder.itemView.layoutEditDelete.layoutDelete.setOnClickListener {removeItem(position)}
        viewHolder.itemView.layoutEditDelete.layoutEdit.setOnClickListener {editItemCallback(connectionArrayList[position],position)}
        //viewHolder.itemView.layoutEditDelete.layoutParams.height = viewHolder.itemView.layoutConnectionHistory.measuredHeight
    }


    override fun getItemCount(): Int {
        return connectionArrayList.size
    }

    fun addItem(connectionHistory: ConnectionHistory) {
        connectionArrayList.add(connectionHistory)
        notifyItemInserted(connectionArrayList.size)
    }

    fun removeItem(position: Int) {
        val connectionHistory = connectionArrayList[position]
        connectionArrayList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, connectionArrayList.size)
        removeItemCallback(connectionHistory)
    }

    fun editItem(position: Int) {
        notifyItemChanged(position)
    }


}
