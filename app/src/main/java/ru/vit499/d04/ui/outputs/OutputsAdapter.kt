package ru.vit499.d04.ui.outputs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.vit499.d04.R
import ru.vit499.d04.util.Logm

class OutputsAdapter(
    private val onClickListener: (Int, Int) -> Unit
) : RecyclerView.Adapter<OutputsAdapter.ViewHolder>() {

    var data = listOf<OutItem>()
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
        val tv1 : TextView = itemView.findViewById(R.id.recl_item_o1)
        val tv2 : TextView = itemView.findViewById(R.id.recl_item_o2)
        val tv3 : TextView = itemView.findViewById(R.id.recl_item_o3)
        val im : ImageButton = itemView.findViewById(R.id.imageButtonOut)
        val layout : RelativeLayout = itemView.findViewById(R.id.out_item_layout)

        fun bind(item: OutItem, position: Int, onClickListener: (Int, Int) -> Unit) {
            tv1.text = item.getNumber()
            tv2.text = item.getFunct()
            tv3.text = item.getState()
            layout.setBackgroundColor(item.getColorOut())
            im.setBackgroundColor(item.getColorOut())
            itemView.setOnLongClickListener(){
                onClickListener(0, position)
                true
            }
            im.setOnClickListener() {
                onClickListener(1, position)
            }
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.recl_outputs_item, parent, false)
                return ViewHolder(view)
            }
        }
    }
}