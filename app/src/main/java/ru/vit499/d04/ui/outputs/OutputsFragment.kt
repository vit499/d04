package ru.vit499.d04.ui.outputs


import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_outputs.*
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_outputs, container, false)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val tvInfo = view.findViewById<TextView>(R.id.tv_info)

        mainViewModel = activity?.run {
            Logm.aa("obj fr")
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        mainViewModel.curObj.observe(this, Observer { obj ->
            var s: String = getString(R.string.obj_empty)
            obj?.let{
                s = obj.objDescr
                Logm.aa("cur obj in out= $s ")
                tvInfo.text = getStrInfo(obj)
            }
            (activity as AppCompatActivity).supportActionBar?.title = s + " (${obj?.objName})"
            (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.title_outputs)
            Logm.aa("curObjName in OutputFragment")
        })




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

    fun getStrInfo(obj: Obj) : String {
        val sb = StringBuilder()
        sb.append("Версия:")
        sb.append("\t\t")
        sb.append(obj.vers)
        sb.append("\r\n")

        sb.append("Temp:")
        sb.append("\t\t")
        sb.append(obj.temp0)
        sb.append("\r\n")

        sb.append("gsm:")
        sb.append("\t\t")
        sb.append(obj.gsm1)
        sb.append("\r\n")

        sb.append("U:")
        sb.append("\t\t")
        sb.append(obj.dv12v)
        sb.append("\r\n")

        sb.append("tcp:")
        sb.append("\t\t")
        sb.append(obj.tcp)
        sb.append("\r\n")

        sb.append("time:")
        sb.append("\t\t")
        sb.append(obj.objTime)
        sb.append("\r\n")

        return sb.toString()
    }
}
