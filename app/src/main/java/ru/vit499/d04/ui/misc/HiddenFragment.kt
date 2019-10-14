package ru.vit499.d04.ui.misc


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.vit499.d04.MainViewModel

import ru.vit499.d04.R
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class HiddenFragment : Fragment() {

    private lateinit var mainViewModel : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hidden, container, false)

        mainViewModel = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("xx")

        val tvLog = view.findViewById<TextView>(R.id.tv_log_sub)
        val button = view.findViewById<Button>(R.id.buttonSub)
        button.setOnClickListener(){
            mainViewModel.onFbSub()
        }

        mainViewModel.strHttpStat.observe(this, Observer { s ->
            s?.let{
                var s1 : StringBuffer = StringBuffer()
                s1.append(tvLog.text.toString())
                s1.append("\r\n")
                s1.append(s)
                tvLog.text = s1.toString()
            }
        })

        return view
    }


}
