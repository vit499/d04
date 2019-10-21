package ru.vit499.d04.ui.main


import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import ru.vit499.d04.MainViewModel

import ru.vit499.d04.R
import ru.vit499.d04.util.Logm
import java.lang.Exception
import java.lang.StringBuilder

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private lateinit var mainViewModel : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        setHasOptionsMenu(true)

        val application = requireNotNull(this.activity).application
        val tvLog = view.findViewById<TextView>(R.id.tv_log)


        mainViewModel = activity?.run {
            Logm.aa("main fr")
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid activity")

        mainViewModel.curObj.observe(this, Observer { obj ->
            var s: String = getString(R.string.obj_empty)
            var s2: String = ""
            obj?.let{
                s = obj.objDescr
                s2 = obj.objName
            }
            (activity as AppCompatActivity).supportActionBar?.title = s
            //(activity as AppCompatActivity).supportActionBar?.subtitle = s2
            Logm.aa("cur obj in main= $s ")
        })

        mainViewModel.navigateToNewObj.observe(this, Observer {
            if(it) {
                Logm.aa("to add obj")
                findNavController().navigate(R.id.action_mainFragment_to_objNewFragment)
                mainViewModel.clrNavigationToNewObj()
            }
        })
        mainViewModel.navigateToAcc.observe(this, Observer {
            if(it){
                Logm.aa("to acc ")
                findNavController().navigate(R.id.action_mainFragment_to_accountFragment)
                mainViewModel.clrNavigationToAcc()
            }
        })
        mainViewModel.statList.observe(this, Observer { list ->
            list?.let{
                var sb = StringBuilder()
                var k = list.size
                for(i in 0 until k){
                    sb.append("\r\n")
                    sb.append(list.get(i).getName())
                }
                tvLog.text = sb.toString()
            }
        })

        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipe)
        swipe.setColorSchemeColors(0x8bc34a)
        swipe.setOnRefreshListener {
            Logm.aa("a on rec stat 2 ")
            mainViewModel.onReqStat()
        }
        mainViewModel.progress.observe(this, Observer {
            swipe.isRefreshing = it
        })

        Logm.aa("a on rec stat 1")
        mainViewModel.onReqStat()

        val buttonClose = view.findViewById<Button>(R.id.btn_close)
        val buttonOpen = view.findViewById<Button>(R.id.btn_onen)
        buttonOpen.setOnClickListener(){
            Logm.aa("a on rec stat 3 ")
            mainViewModel.onReqStat()
        }
        buttonClose.setOnClickListener(){
            mainViewModel.onHttpClose()
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

}
