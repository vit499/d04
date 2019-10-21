package ru.vit499.d04.ui.misc


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import ru.vit499.d04.R
import ru.vit499.d04.util.Logm

/**
 * A simple [Fragment] subclass.
 */
class LogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log, container, false)
        val button = view.findViewById<Button>(R.id.buttonClear)
        val tvLog = view.findViewById<TextView>(R.id.tv_log)

        tvLog.text = Logm.getLog()

        button.setOnClickListener(){
            Logm.clear()
            tvLog.text = ""
        }

        return view
    }


}
