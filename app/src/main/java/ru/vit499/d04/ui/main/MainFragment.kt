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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import ru.vit499.d04.MainViewModel

import ru.vit499.d04.R
import ru.vit499.d04.http.HttpReq
import ru.vit499.d04.util.Logm
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private var httpReq: HttpReq? = null
    private lateinit var mainViewModel : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        setHasOptionsMenu(true)

        val application = requireNotNull(this.activity).application
        val buttonSend = view.findViewById<Button>(R.id.buttonHttp)
        val buttonClose = view.findViewById<Button>(R.id.buttonClose)
        val tvLog = view.findViewById<TextView>(R.id.tv_log)
        buttonSend.setOnClickListener(){
            mainViewModel.onReqStat()

        }
        buttonClose.setOnClickListener(){
            //httpReq?.Close()
            mainViewModel.onHttpClose()
        }

        mainViewModel = activity?.run {
            Logm.aa("main fr")
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid activity")

        mainViewModel.curObj.observe(this, Observer { obj ->
            var s: String = getString(R.string.obj_empty)
            obj?.let{
                Logm.aa("obj name=${obj.objName} ")
                s = obj.objName
            }
            (activity as AppCompatActivity).supportActionBar?.title = s
            Logm.aa("curObjName in MainFragment")
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
        mainViewModel.strHttpStat.observe(this, Observer { s ->
            s?.let{
                var s1 : StringBuffer = StringBuffer()
                s1.append(tvLog.text.toString())
                s1.append(s)
                tvLog.text = s1.toString()
            }
        })

        //(activity as AppCompatActivity).actionBar?.title = ""
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

    override fun onPause() {
        super.onPause()
        httpReq?.Close()
    }
}
