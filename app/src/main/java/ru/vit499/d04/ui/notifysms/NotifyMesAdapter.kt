package ru.vit499.d04.ui.notifysms

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.vit499.d04.R
import ru.vit499.d04.database.Mes

class NotifyMesAdapter(
    private val onClickListener: (Int)-> Boolean
) : RecyclerView.Adapter<NotifyMesAdapter.ViewHolder>() {

    var data = listOf<Mes>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, onClickListener)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor (itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv1 : TextView = itemView.findViewById(R.id.recl_item_mes1)
        val tv2 : TextView = itemView.findViewById(R.id.recl_item_mes2)    // ffcc80
        //val im : ImageButton = itemView.findViewById(R.id.imageButtonMes)
        //val layout : LinearLayout = itemView.findViewById(R.id.event_item_layout)

        fun bind(item: Mes, onClickListener: (Int) -> Boolean) {
            tv1.text = item.mesContent
            tv2.text = item.mesTime1
            tv1.setOnLongClickListener() {
                onClickListener(1)
            }
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.recl_mes_item, parent, false)
                return ViewHolder(view)
            }
        }
    }
}