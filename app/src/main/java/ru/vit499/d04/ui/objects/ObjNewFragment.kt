package ru.vit499.d04.ui.objects


import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import ru.vit499.d04.MainViewModel
import ru.vit499.d04.R
import ru.vit499.d04.databinding.FragmentObjNewBinding
import java.lang.Exception
import android.widget.Toast
import android.text.Spanned
import com.google.android.material.snackbar.Snackbar
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController


/**
 * A simple [Fragment] subclass.
 */
class ObjNewFragment : Fragment() {

    private lateinit var binding: FragmentObjNewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_obj_new, container, false)

        val viewModel = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("invalid fragment obj new")

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_obj_new)

        //val s : ArrayList<String> = viewModel.

        val filter1 = InputFilter { source, start, end, dest, dstart, dend ->
            val blockCharacterSet = "0123456789ABCDEF"
            if (source != null && !blockCharacterSet.contains("" + source)) {
//                Snackbar.make(
//                    activity!!.findViewById(android.R.id.content),
//                    getString(R.string.err_obj_message),
//                    Snackbar.LENGTH_SHORT // How long to display the message.
//                ).show()
                Toast.makeText(activity, "Допустимы 0-9,A-F", Toast.LENGTH_SHORT).show()
                return@InputFilter ""
            }
            null
        }
        val filter2 = InputFilter.LengthFilter(4)
        binding.et611.setFilters(arrayOf(filter1, filter2))
        binding.et613.setFilters(arrayOf(filter2))


        binding.btn61.setOnClickListener() {
            var isValid : Boolean = true
            val objName : String = binding.et611.text.toString()
            if(objName.length < 4) {
                Toast.makeText(getActivity(), "Необходимо ввести 4 цифры", Toast.LENGTH_SHORT).show()
                isValid = false
            }
            val objDescr : String = binding.et612.text.toString()
            val objCode : String = binding.et613.text.toString()
            if(objCode.length < 4) {
                Toast.makeText(getActivity(), "Необходимо ввести 4 цифры", Toast.LENGTH_SHORT).show()
                isValid = false
            }
            if(isValid) {
                var s = ArrayList<String>()
                s.add(objName)
                s.add(objDescr)
                s.add(objCode)
                viewModel.onAddObj(s)
                findNavController().navigate(R.id.action_objNewFragment_to_objFragment)
            }
        }

        binding.et611.requestFocus()
        val imm =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        return binding.root
    }


}
