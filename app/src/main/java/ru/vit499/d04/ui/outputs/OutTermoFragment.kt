package ru.vit499.d04.ui.outputs


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import ru.vit499.d04.MainViewModel

import ru.vit499.d04.R
import ru.vit499.d04.databinding.FragmentOutTermoBinding
import ru.vit499.d04.util.Colors
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class OutTermoFragment : Fragment() {

    private lateinit var binding : FragmentOutTermoBinding
    lateinit var out : OutItem
    var newValue : Int = 0
    var oldValue : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_out_termo, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_out_termo, container, false)
        val arguments = OutTermoFragmentArgs.fromBundle(arguments!!).indexOut
        val mainViewModel = activity?.run{
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("exc")
        out = mainViewModel.getOutItem(arguments) ?: return binding.root
        //out = OutItem(1, 7, 0, 20, 4, 31)

        if(out == null) return binding.root
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_out_termo) + " " + (out.num + 1).toString()

        binding.tvTermoDescr.text = out.getTextTermo(activity!!)
        //binding.tvTermoIndex.text = out.getStrIndTemper() // out.indTemp.toString()
        binding.tvTemperDevice.text = out.getStrIndTemper()
        binding.tvTermoFactTemper.text = out.getTemperature()
        binding.tvTermoToggle.text = out.getFtout()
        binding.seekbarTermoToggle.progress = out.ftout
        newValue = out.ftout
        oldValue = out.ftout
        binding.seekbarTermoToggle.setOnSeekBarChangeListener(seekListener(binding.tvTermoToggle))

        binding.btnTermoSave.setOnClickListener() {
            newValue = binding.seekbarTermoToggle.progress
            if (newValue != oldValue) {
                val key = "setout" + (out.num+1).toString()
                val value = newValue.toString()
                mainViewModel.onPostCmd(key, value)
                snack()
            }
            this.findNavController().navigate(R.id.action_outTermoFragment_to_outputsFragment)
        }
        return binding.root
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
    fun snack () {
        val snbar = Snackbar.make(binding.root, getString(R.string.text_cmd_sended), Snackbar.LENGTH_SHORT)
        snbar.setDuration(500)
        val v = snbar.view
        v.setBackgroundColor(Colors.PaleTurquoise)
        val sTv : TextView = v.findViewById(com.google.android.material.R.id.snackbar_text);
        sTv.setTextColor(Colors.Black)
        snbar.show()
    }

}
