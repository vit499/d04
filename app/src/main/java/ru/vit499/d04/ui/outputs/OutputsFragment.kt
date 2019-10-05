package ru.vit499.d04.ui.outputs


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController

import ru.vit499.d04.R

/**
 * A simple [Fragment] subclass.
 */
class OutputsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_outputs, container, false)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_outputs)
        return view
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.over_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.objFragment) {
            findNavController().navigate(R.id.action_outputsFragment_to_objFragment)
            return false
        }
        return super.onOptionsItemSelected(item)
    }

}
