package ru.vit499.d04.ui.objects


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.vit499.d04.R

/**
 * A simple [Fragment] subclass.
 */
class ObjNewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_obj_new, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_obj_new)

        return view
    }


}
