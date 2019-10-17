package ru.vit499.d04.ui.objects

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.vit499.d04.R
import ru.vit499.d04.database.Obj
import ru.vit499.d04.util.Logm

class ObjAdapter(
    private val onClickListener: (Int, Long) -> Unit
) : RecyclerView.Adapter<ObjAdapter.ViewHolder>() {

    var data = listOf<Obj>()
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
        val tv1 : TextView = itemView.findViewById(R.id.textViewHead)
        val tv2 : TextView = itemView.findViewById(R.id.textViewDesc)
        val im : ImageButton = itemView.findViewById(R.id.imageButtonObj)

        fun bind(obj: Obj, pos: Int, onClickListener: (Int, Long) -> Unit) {
            tv2.text = obj.objName
            tv1.text = obj.objDescr

            tv1.setOnClickListener(){
                onClickListener(0, obj.objId)
            }
            tv2.setOnClickListener(){
                onClickListener(0, obj.objId)
            }
            im.setOnClickListener() {
                onClickListener(1, obj.objId)
            }
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.recl_obj_view, parent, false)
                return ViewHolder(view)
            }
        }
    }
}

