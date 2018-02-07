package app.deadmc.devnetworktool.adapters

import android.util.Log
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.models.ConnectionHistory
import kotlinx.android.synthetic.main.item_url_history.view.*

abstract class UrlHistoryAdapter(private val connectionArrayList: ArrayList<ConnectionHistory>) : BaseSwipeAdapter<ConnectionHistory>(connectionArrayList,R.layout.item_url_history_swipe) {
    override fun onBindViewHolder(viewHolder: BaseSwipeAdapter<ConnectionHistory>.ViewHolder, position: Int) {
        viewHolder.itemView.urlTextView.text = connectionArrayList[position].ipAddress
        viewHolder.itemView.dateTextView.text = connectionArrayList[position].lastUsageTime
        viewHolder.itemView.layoutUrlHistory.setOnClickListener {
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