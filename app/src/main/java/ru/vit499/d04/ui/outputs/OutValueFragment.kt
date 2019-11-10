package ru.vit499.d04.ui.outputs


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import ru.vit499.d04.R

/**
 * A simple [Fragment] subclass.
 */
class OutValueFragment : Fragment() {

    //var tvVal : TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_out_value, container, false)

        val outViewModel = activity?.run {
            ViewModelProviders.of(this).get(OutViewModel::class.java)
        } ?: throw Exception("")

        val layout1 = view.findViewById<LinearLayout>(R.id.linearLayout64_1)
        val switch1 = view.findViewById<Switch>(R.id.switch1)

        val tvTimeOn = view.findViewById<TextView>(R.id.tv64_12)
        tvTimeOn.text = 10.toString()
        val tvTempFact = view.findViewById<TextView>(R.id.tv64_22)
        tvTempFact.text = 15.toString()
        val tvTempSet = view.findViewById<TextView>(R.id.tv64_32)
        tvTempSet.text = 12.toString()
        val seekBarTimeOn = view.findViewById<SeekBar>(R.id.seekBarTimeOn)
        seekBarTimeOn.progress = 10
        seekBarTimeOn.setOnSeekBarChangeListener(seekListener(tvTimeOn!!))
        val seekBar2 = view.findViewById<SeekBar>(R.id.seekBar2)
        seekBar2.progress = 12
        seekBar2.setOnSeekBarChangeListener(seekListener(tvTempSet!!))

        val btnTurnOn = view.findViewById<Button>(R.id.btn_64_1)
        btnTurnOn.setOnClickListener(){
            outViewModel.onBackFromValOut(1)
        }
        val btnSave = view.findViewById<Button>(R.id.btn_64_2)
        btnSave.setOnClickListener(){
            outViewModel.onBackFromValOut(1)
        }

        if(switch1.isChecked){
            btnTurnOn.isEnabled = true
            seekBarTimeOn.isEnabled = true
            //btnSave.isEnabled = false
            seekBar2.isEnabled = false
        }
        else {
            btnTurnOn.isEnabled = false
            seekBarTimeOn.isEnabled = false
            //btnSave.isEnabled = true
            seekBar2.isEnabled = true
        }
        switch1.setOnCheckedChangeListener(){ buttonView, isChecked ->
            if(isChecked){
                btnTurnOn.isEnabled = true
                seekBarTimeOn.isEnabled = true
                //btnSave.isEnabled = false
                seekBar2.isEnabled = false
            }
            else {
                btnTurnOn.isEnabled = false
                seekBarTimeOn.isEnabled = false
                //btnSave.isEnabled = true
                seekBar2.isEnabled = true
            }
        }

        outViewModel.navigateBackFromValOut.observe(this, Observer {
            if(it){
                this.findNavController().navigate(R.id.action_outValueFragment_to_outputsFragment)
                outViewModel.clrNavigationBackFromValOut()
            }
        })
        return view
    }

    internal class seekListener(val tv: TextView) : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seek: SeekBar, i: Int, fromUser: Boolean) {
            if(fromUser) {
                tv.text = i.toString()
            }
        }
        override fun onStartTrackingTouch(seek: SeekBar) {
        }
        override fun onStopTrackingTouch(seek: SeekBar) {
        }
    }

}
