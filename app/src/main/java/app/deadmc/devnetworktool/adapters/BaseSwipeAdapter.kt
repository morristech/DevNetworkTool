package app.deadmc.devnetworktool.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.helpers.safe
import app.deadmc.devnetworktool.interfaces.model.BaseModel
import app.deadmc.devnetworktool.views.SwipeLayout
import kotlinx.android.synthetic.main.item_edit_delete.view.*
import java.util.*


abstract class BaseSwipeAdapter<T:BaseModel>(protected val arrayList: ArrayList<T>, val layoutId:Int, val onlyDelete:Boolean=false) : RecyclerView.Adapter<BaseSwipeAdapter<T>.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    val TAG = this.javaClass.simpleName

    protected lateinit var recyclerView: RecyclerView

    abstract fun onClickItem(element: T, position: Int)

    abstract fun onDeleteItem(element: T)

    abstract fun onEditItem(element: T, position: Int)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView!!
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BaseSwipeAdapter<T>.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: BaseSwipeAdapter<T>.ViewHolder, position: Int) {
        viewHolder.itemView.layoutEditDelete.layoutDelete.setOnClickListener {removeItem(position)}
        viewHolder.itemView.layoutEditDelete.layoutEdit.setOnClickListener { onEditItem(arrayList[position],position)}
        viewHolder.itemView.findViewById<SwipeLayout>(R.id.swipeLayout).swipeLayoutCallback = {removeItem(position)}

        if (onlyDelete)
            viewHolder.itemView.layoutEditDelete.layoutEdit.visibility = View.GONE
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun addItem(element: T) {
        arrayList.add(0,element)
        notifyItemInserted(0)
    }

    fun addAll(list:ArrayList<T>) {
        safe {
            arrayList.clear()
            arrayList.addAll(list)
            notifyDataSetChanged()
        }
    }


    fun removeItem(position: Int) {
        if (position >= arrayList.size)
            return
        val element = arrayList[position]
        arrayList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, arrayList.size)
        onDeleteItem(element)
    }

    fun editItem(position: Int) {
        if (position >= arrayList.size)
            return
        notifyItemChanged(position)
    }



}
