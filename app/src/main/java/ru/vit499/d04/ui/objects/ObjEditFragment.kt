package ru.vit499.d04.ui.objects


import android.app.AlertDialog
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import ru.vit499.d04.MainViewModel

import ru.vit499.d04.R
import ru.vit499.d04.databinding.FragmentObjEditBinding
import ru.vit499.d04.util.Logm
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class ObjEditFragment : Fragment() {

    private lateinit var binding : FragmentObjEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_obj_edit, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_obj_edit)

        val viewModel = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("invalid fragment obj edit")

        val filter2 = InputFilter.LengthFilter(4)
        binding.et623.setFilters(arrayOf(filter2))

        viewModel.curObjEdit.observe(this, Observer { obj ->
            Logm.aa(" observe edit obj ")
            obj?.let {
                binding.et621.setText(obj.objName)
                binding.et622.setText(obj.objDescr)
                binding.et623.setText(obj.objCode)
            }
        })
        viewModel.navigateToObj.observe(this, Observer {
            if(it){
                (activity as AppCompatActivity).onBackPressed()
                viewModel.clrNavigateToObj()
            }
        })

        binding.btn621.setOnClickListener() {
            var isValid : Boolean = true
            val objName : String = binding.et621.text.toString()
            val objDescr : String = binding.et622.text.toString()
            val objCode : String = binding.et623.text.toString()
            if(objCode.length < 4) {
                Toast.makeText(getActivity(), "Необходимо ввести 4 цифры", Toast.LENGTH_SHORT).show()
                isValid = false
            }
            if(isValid) {
                var s = ArrayList<String>()
                s.add(objName)
                s.add(objDescr)
                s.add(objCode)
                viewModel.onEditObj(s)
                //this.findNavController().navigate(R.id.action_objNewFragment_to_objFragment)
                //(activity as AppCompatActivity).onBackPressed()
            }
        }
        binding.btn622.setOnClickListener(){

            val alert = AlertDialog.Builder(activity)
            with (alert) {
                setTitle(R.string.delete_object)
                setMessage(R.string.are_you_sure)
                setPositiveButton(" Да ") {
                        dialog, whichButton ->
                    viewModel.onDeleteObj()
                }
                setNegativeButton("Отмена") { dialog, whichButton -> }
                create()
                show()
            }
            //viewModel.onDeleteObj()
        }

//        viewModel.navigateToObj.observe(this, Observer {
//            if(it == true){
//                //findNavController().navigate(R.id.action_objNewFragment_to_objFragment)
//                this.findNavController().navigate(ObjEditFragmentDirections.actionObjEditFragmentToObjFragment())
//                viewModel.clrNavigateToObj()
//            }
//        })

        return binding.root
    }



}
