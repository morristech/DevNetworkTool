package app.deadmc.devnetworktool.ui.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.data.models.JsonInput
import app.deadmc.devnetworktool.data.models.ReceivedMessage
import kotlinx.android.synthetic.main.item_received_message.view.*

abstract class ReceivedMessagesAdapter(val context: Context, val receivedMessageList:ArrayList<ReceivedMessage> ) :
        BaseSwipeAdapter<ReceivedMessage>(receivedMessageList, R.layout.item_received_message_swipe, onlyDelete = true) {
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val receivedMessage = receivedMessageList[position]

        viewHolder.itemView.layoutReceivedMessage.layoutText.textViewText.text = receivedMessage.text
        viewHolder.itemView.layoutReceivedMessage.layoutText.textViewTime.text = receivedMessage.time
        if (receivedMessage.isFromServer)
            viewHolder.itemView.imageViewFrom.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.server))
        else
            viewHolder.itemView.imageViewFrom.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.client))

        viewHolder.itemView.layoutReceivedMessage.setOnClickListener { onClickItem(receivedMessage,position) }

        super.onBindViewHolder(viewHolder,position)
    }

    override fun onClickItem(element: ReceivedMessage, position: Int) {
    }

    override fun onEditItem(element: ReceivedMessage, position: Int) {
    }

    override fun addItem(element: ReceivedMessage) {
        arrayList.add(0,element)
        notifyItemInserted(arrayList.size-1)
    }
}