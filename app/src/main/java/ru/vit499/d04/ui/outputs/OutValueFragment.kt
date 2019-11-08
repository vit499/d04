package ru.vit499.d04.ui.outputs


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import ru.vit499.d04.R

/**
 * A simple [Fragment] subclass.
 */
class OutValueFragment : Fragment() {

    var tvVal : TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_out_value, container, false)


        val outViewModel = activity?.run {
            ViewModelProviders.of(this).get(OutViewModel::class.java)
        } ?: throw Exception("")

        val btnSave = view.findViewById<Button>(R.id.btn_64_1)
        btnSave.setOnClickListener(){
            outViewModel.onBackFromValOut(1)
        }

        val npVal = view.findViewById<NumberPicker>(R.id.npVal)
        npVal.maxValue = 30
        npVal.minValue = 0
        npVal.wrapSelectorWheel = true
        tvVal = view.findViewById<TextView>(R.id.tv64_3)
        val seekBar = view.findViewById<SeekBar>(R.id.seekBar2)
        npVal.setOnValueChangedListener { numberPicker, i, i2 ->
            tvVal!!.text = i2.toString()
            seekBar.progress = i2
        }
        seekBar.setOnSeekBarChangeListener(seekListener(tvVal!!, npVal))

        outViewModel.navigateBackFromValOut.observe(this, Observer {
            if(it){
                this.findNavController().navigate(R.id.action_outValueFragment_to_outputsFragment)
                outViewModel.clrNavigationBackFromValOut()
            }
        })
        return view
    }

    internal class seekListener(val tv: TextView, val nb: NumberPicker) : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seek: SeekBar, i: Int, fromUser: Boolean) {
            if(fromUser) {
                tv.text = i.toString()
                nb.value = i
            }
        }
        override fun onStartTrackingTouch(seek: SeekBar) {
        }
        override fun onStopTrackingTouch(seek: SeekBar) {
        }
    }

}
