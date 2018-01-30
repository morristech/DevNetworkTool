package app.deadmc.devnetworktool.adapters

import android.util.Log

import java.util.ArrayList

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.models.ConnectionHistory
import kotlinx.android.synthetic.main.item_connection_history.view.*

abstract class ConnectionHistoryAdapter(private val connectionArrayList: ArrayList<ConnectionHistory>) : BaseSwipeAdapter<ConnectionHistory>(connectionArrayList,R.layout.item_connection_swipe) {

    override fun onBindViewHolder(viewHolder: BaseSwipeAdapter<ConnectionHistory>.ViewHolder, position: Int) {
        viewHolder.itemView.nameTextView.text = connectionArrayList[position].name
        viewHolder.itemView.ipAddressAndPortTextView.text = connectionArrayList[position].ipAddress + " : " + connectionArrayList[position].port
        viewHolder.itemView.layoutConnectionHistory.setOnClickListener {
            Log.e("click","position "+position)
            onClickItem(connectionArrayList[position], position)
        }

        super.onBindViewHolder(viewHolder,position)
        /*
        viewBinderHelper.bind(viewHolder.itemView.SwipeLayout, connectionArrayList[position].id.toString())
        viewHolder.itemView.layoutEditDelete.layoutDelete.setOnClickListener {removeItem(position)}
        viewHolder.itemView.layoutEditDelete.layoutEdit.setOnClickListener {onEditItem(connectionArrayList[position],position)}
        */
        //viewHolder.itemView.layoutEditDelete.layoutParams.height = viewHolder.itemView.layoutConnectionHistory.measuredHeight
    }
}
