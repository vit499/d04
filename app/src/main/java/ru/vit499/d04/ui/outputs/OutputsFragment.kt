package ru.vit499.d04.ui.outputs


import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.vit499.d04.MainViewModel

import ru.vit499.d04.R
import ru.vit499.d04.database.Obj
import ru.vit499.d04.util.Logm
import java.lang.StringBuilder

/**
 * A simple [Fragment] subclass.
 */
class OutputsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var outViewModel: OutViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_outputs, container, false)
        //setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        //val tvInfo = view.findViewById<TextView>(R.id.tv_info)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recl_outputs)
        val adapter = OutputsAdapter(onClickListener = { o, b -> onClick(o, b) })
        recyclerView.adapter = adapter

        mainViewModel = activity?.run {
           // Logm.aa("obj fr")
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        outViewModel = activity?.run {
            ViewModelProviders.of(this)[OutViewModel::class.java]
        } ?: throw Exception("Invalid out fragment")

        mainViewModel.curObj.observe(this, Observer { obj ->
            var s: String = getString(R.string.obj_empty)
            obj?.let{
                s = obj.objDescr
              //  Logm.aa("cur obj in out= $s ")
                //tvInfo.text = getStrInfo(obj)
            }
            (activity as AppCompatActivity).supportActionBar?.title = s + " (${obj?.objName})"
            //(activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.title_outputs)
           // Logm.aa("curObjName in OutputFragment")
        })

        mainViewModel.outList.observe(viewLifecycleOwner, Observer {
            it?.let {
               // Logm.aa("fr out cnt= ${it.size}")
                adapter.data = it
            }
        })

        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipeO)
        swipe.setColorSchemeColors(0x8bc34a)
        swipe.setOnRefreshListener {
           // Logm.aa("a on rec stat (outs)")
            mainViewModel.onReqStat()
        }
        mainViewModel.progress.observe(this, Observer {
            swipe.isRefreshing = it
        })

        outViewModel.navigateToValOut.observe(this, Observer {
            if(it){
                this.findNavController().navigate(R.id.action_outputsFragment_to_outValueFragment)
                outViewModel.clrNavigationToValOut()
            }
        })

        return view
    }
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater?.inflate(R.menu.over_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(item.itemId == R.id.objFragment) {
//            findNavController().navigate(R.id.action_outputsFragment_to_objFragment)
//            return false
//        }
//        return super.onOptionsItemSelected(item)
//    }

    fun onClick(o: Int, b: Int) {
       // Logm.aa("out click ${b.toString()}")
        if(o == 0) mainViewModel.onPostCmd("out${(b+1).toString()}", "10010")
        else if(o == 1) {
            outViewModel.onSetValueOut(b)
        }
    }

}
