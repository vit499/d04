package ru.vit499.d04.ui.misc


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import ru.vit499.d04.BuildConfig

import ru.vit499.d04.R

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_about)
        val textAbout = view.findViewById<TextView>(R.id.text_view_about)
        val versionName = "Версия: " + BuildConfig.VERSION_NAME
        textAbout.text = versionName
        textAbout.setOnClickListener(){
            findNavController().navigate(R.id.action_aboutFragment_to_hiddenFragment)
        }
        return view
    }


}
