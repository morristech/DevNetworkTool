package app.deadmc.devnetworktool.ui.adapters

import android.content.Context
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.utils.getDateTimeFromTimestamp
import app.deadmc.devnetworktool.data.models.RestRequestHistory
import kotlinx.android.synthetic.main.item_rest_request_history.view.*

abstract class RestRequestHistoryAdapter(val context: Context, val requestHistoryList:ArrayList<RestRequestHistory> ) :
        BaseSwipeAdapter<RestRequestHistory>(requestHistoryList, R.layout.item_rest_request_history_swipe, onlyDelete = true) {
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val restRequestHistory = requestHistoryList[position]

        viewHolder.itemView.layoutRestRequestHistory.urlTextView.text = restRequestHistory.url
        viewHolder.itemView.layoutRestRequestHistory.methodTextView.text = restRequestHistory.method
        viewHolder.itemView.layoutRestRequestHistory.dateTextView.text = getDateTimeFromTimestamp(restRequestHistory.lastUsage)
        viewHolder.itemView.layoutRestRequestHistory.setOnClickListener { onClickItem(restRequestHistory,position) }

        super.onBindViewHolder(viewHolder,position)
    }

    override fun onClickItem(element: RestRequestHistory, position: Int) {
    }

    override fun onEditItem(element: RestRequestHistory, position: Int) {
    }
}