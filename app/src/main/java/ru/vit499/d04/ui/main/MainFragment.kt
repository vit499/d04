package ru.vit499.d04.ui.main


import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

import ru.vit499.d04.R
import ru.vit499.d04.http.HttpReq

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private var httpReq: HttpReq? = null

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
