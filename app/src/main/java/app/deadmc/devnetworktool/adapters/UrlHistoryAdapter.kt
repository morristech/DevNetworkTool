package app.deadmc.devnetworktool.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.adapters.ConnectionHistoryAdapter
import app.deadmc.devnetworktool.modules.ConnectionHistory
import kotlinx.android.synthetic.main.item_connection_swipe.view.*
import kotlinx.android.synthetic.main.item_edit_delete.view.*
import kotlinx.android.synthetic.main.item_url_history.view.*

abstract class UrlHistoryAdapter(private val connectionArrayList: ArrayList<ConnectionHistory>) : ConnectionHistoryAdapter(connectionArrayList) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ConnectionHistoryAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_url_history_swipe, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ConnectionHistoryAdapter.ViewHolder, position: Int) {
        viewHolder.itemView.urlTextView.text = connectionArrayList[position].ipAddress
        viewHolder.itemView.layoutUrlHistory.setOnClickListener {
            Log.e("click","position "+position)
            onClickItem(connectionArrayList[position], position)
        }
        viewBinderHelper.bind(viewHolder.itemView.swipeRevealLayout, connectionArrayList[position].id.toString())
        viewHolder.itemView.layoutEditDelete.layoutDelete.setOnClickListener {removeItem(position)}
        viewHolder.itemView.layoutEditDelete.layoutEdit.setOnClickListener {editItemCallback(connectionArrayList[position],position)}
        //viewHolder.itemView.layoutEditDelete.layoutParams.height = viewHolder.itemView.layoutConnectionHistory.measuredHeight
    }
}