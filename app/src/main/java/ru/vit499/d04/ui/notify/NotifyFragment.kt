package ru.vit499.d04.ui.notify


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.vit499.d04.MainViewModel

import ru.vit499.d04.R
import ru.vit499.d04.util.Logm

/**
 * A simple [Fragment] subclass.
 */
class NotifyFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notify, container, false)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recl_notify)
        val adapter = NotifyAdapter()
        recyclerView.adapter = adapter

        mainViewModel = activity?.run {
           // Logm.aa("obj fr")
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

       // Logm.aa("a on req ev 1 ")
        mainViewModel.onReqEvent()
        mainViewModel.curObj.observe(this, Observer { obj ->
            var s: String = getString(R.string.obj_empty)
            obj?.let{
                s = obj.objDescr
                Logm.aa("cur obj in ev= $s ")
            }
            (activity as AppCompatActivity).supportActionBar?.title = s + " (${obj?.objName})"
            //(activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.title_events)
           // Logm.aa("curObjName in NotifyFragment")
        })
        mainViewModel.events.observe(viewLifecycleOwner, Observer {
            it?.let {
               // Logm.aa("ev observe")
               // Logm.aa("ev cnt= ${it.size}")
                adapter.data = it
                adapter.notifyDataSetChanged()
            }
        })

        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipeN)
        swipe.setColorSchemeColors(0x607d8b)
        swipe.setOnRefreshListener {
           // Logm.aa("a on rec ev 2 ")
            mainViewModel.onReqEvent()
        }
        mainViewModel.progress.observe(this, Observer {
            swipe.isRefreshing = it
        })

        return view
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.over_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.objFragment) {
            findNavController().navigate(R.id.action_notifyFragment_to_objFragment)
            return false
        }
        return super.onOptionsItemSelected(item)
    }

}
