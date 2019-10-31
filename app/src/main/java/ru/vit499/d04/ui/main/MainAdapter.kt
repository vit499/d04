package ru.vit499.d04.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.vit499.d04.R

class MainAdapter(
    private val onClickListener: (Int, Int)-> Unit
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var data = listOf<StatusItem>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, position, onClickListener)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor (itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv1 : TextView = itemView.findViewById(R.id.recl_item_state1)
        val tv2 : TextView = itemView.findViewById(R.id.recl_item_state2)
        val tv3 : TextView = itemView.findViewById(R.id.recl_item_state3)

        fun bind(item: StatusItem, position: Int, onClickListener: (Int, Int) -> Unit) {
            tv1.text = item.getName()
            tv2.text = item.getStrStat()
            tv3.text = item.getCmd()

            if(item.getStrPart() == 1) {

            }
            else {

            }

            itemView.setOnLongClickListener(){
                onClickListener(0, position)
                true
            }
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.recl_status_item, parent, false)
                return ViewHolder(view)
            }
        }
    }
}

//class StatusItemDiffCallback : DiffUtil.ItemCallback<StatusItem>() {
//
//    override fun areItemsTheSame(oldItem: StatusItem, newItem: StatusItem): Boolean {
//        return oldItem.getInd() == newItem.getInd()
//    }
//
//    override fun areContentsTheSame(oldItem: StatusItem, newItem: StatusItem): Boolean {
//        return oldItem.getIntColor() == newItem.getIntColor()
//    }
//}