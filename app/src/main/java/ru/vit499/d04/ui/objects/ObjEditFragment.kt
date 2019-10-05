package ru.vit499.d04.ui.objects


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import ru.vit499.d04.R

/**
 * A simple [Fragment] subclass.
 */
class ObjEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_obj_edit, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_obj_new)
        //val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            //findNavController().navigate(R.id.action_objEditFragment_to_objFragment)
        //}

        return view
    }



}
