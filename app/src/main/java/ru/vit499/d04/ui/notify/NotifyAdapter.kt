package ru.vit499.d04.ui.notify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.vit499.d04.R

class NotifyAdapter() : RecyclerView.Adapter<NotifyAdapter.ViewHolder>() {

    var data = listOf<NotifyItem>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor (itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv1 : TextView = itemView.findViewById(R.id.recl_item_ev1)
        val tv2 : TextView = itemView.findViewById(R.id.recl_item_ev2)    // ffcc80
        val layout : LinearLayout = itemView.findViewById(R.id.event_item_layout)

        fun bind(item: NotifyItem) {
            tv1.text = item.getDescr()
            tv2.text = item.getTime()
            layout.setBackgroundColor(item.getColor())
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.recl_event_item, parent, false)
                return ViewHolder(view)
            }
        }
    }
}