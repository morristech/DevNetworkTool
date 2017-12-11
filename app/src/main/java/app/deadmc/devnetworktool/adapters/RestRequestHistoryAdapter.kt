package app.deadmc.devnetworktool.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.helpers.getDateAndTime
import app.deadmc.devnetworktool.models.RestRequestHistory
import kotlinx.android.synthetic.main.item_rest_request_history.view.*

abstract class RestRequestHistoryAdapter(val context: Context, val requestHistoryList:ArrayList<RestRequestHistory> ) :
        BaseSwipeAdapter<RestRequestHistory>(requestHistoryList, R.layout.item_rest_request_history_swipe, onlyDelete = true) {
    override fun onBindViewHolder(viewHolder: BaseSwipeAdapter<RestRequestHistory>.ViewHolder, position: Int) {
        val restRequestHistory = requestHistoryList[position]

        viewHolder.itemView.layoutRestRequestHistory.urlTextView.text = restRequestHistory.url
        viewHolder.itemView.layoutRestRequestHistory.methodTextView.text = restRequestHistory.method
        viewHolder.itemView.layoutRestRequestHistory.dateTextView.text = getDateAndTime(restRequestHistory.timeLastUsage,context)
        viewHolder.itemView.layoutRestRequestHistory.setOnClickListener { onClickItem(restRequestHistory,position) }

        super.onBindViewHolder(viewHolder,position)
    }

    override fun onClickItem(element: RestRequestHistory, position: Int) {
    }

    override fun onEditItem(element: RestRequestHistory, position: Int) {
    }
}