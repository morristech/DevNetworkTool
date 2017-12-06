package app.deadmc.devnetworktool.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.interfaces.model.BaseModel
import app.deadmc.devnetworktool.views.ViewBinderHelper
import kotlinx.android.synthetic.main.item_connection_swipe.view.*
import kotlinx.android.synthetic.main.item_edit_delete.view.*
import java.util.ArrayList


abstract class BaseSwipeAdapter<T:BaseModel>(protected val arrayList: ArrayList<T>, val layoutId:Int, val onlyDelete:Boolean=false) : RecyclerView.Adapter<BaseSwipeAdapter<T>.ViewHolder>() {

    protected val viewBinderHelper = ViewBinderHelper()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    abstract fun onClickItem(element: T, position: Int)

    abstract fun onDeleteItem(element: T)

    abstract fun onEditItem(element: T, position: Int)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BaseSwipeAdapter<T>.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: BaseSwipeAdapter<T>.ViewHolder, position: Int) {
        viewBinderHelper.bind(viewHolder.itemView.swipeRevealLayout, arrayList[position].getUniqueId())
        viewHolder.itemView.layoutEditDelete.layoutDelete.setOnClickListener {removeItem(position)}
        viewHolder.itemView.layoutEditDelete.layoutEdit.setOnClickListener { onEditItem(arrayList[position],position)}

        if (onlyDelete)
            viewHolder.itemView.layoutEditDelete.layoutEdit.visibility = View.GONE
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun addItem(element: T) {
        arrayList.add(element)
        notifyItemInserted(arrayList.size)
    }

    fun removeItem(position: Int) {
        val element = arrayList[position]
        arrayList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, arrayList.size)
        onDeleteItem(element)
    }

    fun editItem(position: Int) {
        notifyItemChanged(position)
    }


}
