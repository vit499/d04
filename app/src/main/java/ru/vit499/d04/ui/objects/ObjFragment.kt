package ru.vit499.d04.ui.objects


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import ru.vit499.d04.MainViewModel

import ru.vit499.d04.R
import ru.vit499.d04.util.Logm
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class ObjFragment : Fragment() {

    private lateinit var objViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_obj, container, false)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_obj)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recl_obj)
        val adapter = ObjAdapter()
        recyclerView.adapter = adapter

        objViewModel = activity?.run {
            Logm.aa("obj fr")
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")


        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.obj_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.objNewFragment) {
            //Log.i("aa", "opt to edit obj")
            findNavController().navigate(R.id.action_objFragment_to_objNewFragment)
            return false
        }
        if(item.itemId == R.id.aboutFragment) {
            NavigationUI.onNavDestinationSelected(item!!, view!!.findNavController())
            return false
        }
        return super.onOptionsItemSelected(item)
//        if(item.itemId == R.id.objEditFragment) {
//            Log.i("aa", "opt to edit obj")
//            return NavigationUI.onNavDestinationSelected(item!!, view!!.findNavController())
//                    || super.onOptionsItemSelected(item)
//        }
//        return super.onOptionsItemSelected(item)
    }


}
