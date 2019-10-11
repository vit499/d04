package ru.vit499.d04.ui.main


import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
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
        buttonSend.setOnClickListener(){
            httpReq = HttpReq(application)
            httpReq?.Send1("GET / HTTP/1.1\r\nHost: vit499.ru\r\n\r\n")
        }
        buttonClose.setOnClickListener(){
            httpReq?.Close()
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
