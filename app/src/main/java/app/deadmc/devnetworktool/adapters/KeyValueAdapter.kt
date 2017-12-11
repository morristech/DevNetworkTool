package app.deadmc.devnetworktool.adapters

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.models.KeyValueModel
import kotlinx.android.synthetic.main.item_key_value.view.*

abstract class KeyValueAdapter(val keyValueList:ArrayList<KeyValueModel>) : BaseSwipeAdapter<KeyValueModel>(keyValueList, R.layout.item_key_value_swipe) {
    override fun onBindViewHolder(viewHolder: BaseSwipeAdapter<KeyValueModel>.ViewHolder, position: Int) {
        viewHolder.itemView.layoutKeyValue.keyTextView.text = keyValueList[position].key
        viewHolder.itemView.layoutKeyValue.valueTextView.text = keyValueList[position].value
        viewHolder.itemView.layoutKeyValue.setOnClickListener {
            onClickItem(keyValueList[position], position)
        }

        super.onBindViewHolder(viewHolder,position)
    }

    override fun onClickItem(element: KeyValueModel, position: Int) {}

    override fun onDeleteItem(element: KeyValueModel) {}
}