package ru.vit499.d04.ui.info


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.vit499.d04.MainViewModel

import ru.vit499.d04.R
import ru.vit499.d04.util.Logm
import ru.vit499.d04.util.getStrInfo

/**
 * A simple [Fragment] subclass.
 */
class InfoFragment : Fragment() {

    private lateinit var mainViewModel : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_info, container, false)

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
            (activity as AppCompatActivity).supportActionBar?.title = s
            //(activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.title_outputs)
            Logm.aa("curObjName in InfoFragment")
        })

        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipeI)
        swipe.setColorSchemeColors(0x8bc34a)
        swipe.setOnRefreshListener {
            mainViewModel.onReqStat()
        }
        mainViewModel.progress.observe(this, Observer {
            swipe.isRefreshing = it
        })

        return view
    }


}
