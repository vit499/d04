package ru.vit499.d04.ui.misc


import android.app.AlertDialog
import android.content.ClipData
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.vit499.d04.MainViewModel

import ru.vit499.d04.R
import ru.vit499.d04.util.Logm
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

        val btn1 = view.findViewById<ImageButton>(R.id.button1)
        val btn2 = view.findViewById<Button>(R.id.button2)
        btn1.setOnClickListener() {
            //alertD(activity!!, "ab", "cd", doit = { abcd() } )
            //btn1.setBackgroundColor(0xff3399ee.toInt())
            btn2.setBackgroundResource(R.drawable.btnstyle1)
        }

        btn2.setOnClickListener(){
            btn1.setBackgroundResource(R.drawable.btnstyle2)
        }

        return view
    }

    fun abcd(){
        Logm.aa("doit")
    }
    // onClickListener: (Int, Int)-> Unit
    fun alertD(context: Context, s1: String, s2: String, doit: () -> Unit) {
        val alert = AlertDialog.Builder(context)
        with (alert) {
            setTitle(s1)
            setMessage(s2)
            setPositiveButton(" Да ") {
                    dialog, whichButton ->
                //Logm.aa("yes")
                doit()
            }
            setNegativeButton("Отмена ") { dialog, whichButton -> }
            create()
            show()
        }
    }
}
