package ru.vit499.d04.ui.notifysms


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import ru.vit499.d04.MainViewModel

import ru.vit499.d04.R
import ru.vit499.d04.util.Logm

/**
 * A simple [Fragment] subclass.
 */
class NotifyMesFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var mesViewModel: MesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notify_text, container, false)
        setHasOptionsMenu(true)
        //(activity as AppCompatActivity).supportActionBar?.title = getString(R.string.text_title_notify2)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

//        val onClick = { pos : Int ->
//            Logm.aa("-")
//        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recl_notify2)
        val adapter = NotifyMesAdapter(onClickListener = { a -> onClick(a)})
        recyclerView.adapter = adapter

        mainViewModel = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("xx")
        mesViewModel = activity?.run {
            ViewModelProviders.of(this)[MesViewModel::class.java]
        } ?: throw Exception("xx")

        mainViewModel.curObj.observe(this, Observer { obj ->
            var s: String = getString(R.string.obj_empty)
            obj?.let{
                s = obj.objDescr
            }
            (activity as AppCompatActivity).supportActionBar?.title = s + " mes"  // + getString(R.string.text_title_notify2)
        })
        mesViewModel.messages.observe(viewLifecycleOwner, Observer {
            it?.let{
                val mes = mesViewModel.getListMes()

            }
        })
        mesViewModel.listMes.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.data = it
                adapter.notifyDataSetChanged()
            }
        })


        return(view)
    }

    fun onClick(pos: Int) : Boolean {
        mesViewModel.onDelete()
        return false
    }
}
