package ru.vit499.d04.ui.misc


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.vit499.d04.MainViewModel

import ru.vit499.d04.R
import ru.vit499.d04.databinding.FragmentAccountBinding
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : Fragment() {

    private lateinit var binding : FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_account)

        val viewModel = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("invalid fragment account")

        binding.et551.setText(Account.accUser)
        binding.et552.setText(Account.accPass)
        binding.et553.setText(Account.accServ)
        binding.et554.setText(Account.accPort)

        binding.btn551.setOnClickListener(){


            val acc = fillAcc()
            val isValid = isValidAcc(acc)
            if(!isValid){
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.err_acc_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
            }
            else {
                viewModel.onSaveAcc(acc!!)
                //findNavController().navigate(R.id.action_accountFragment_to_mainFragment)
                (activity as AppCompatActivity).onBackPressed()
            }
        }
        return binding.root
    }

    fun fillAcc() : ArrayList<String>? {
        var acc = ArrayList<String>()
        val user : String? = binding.et551.text.toString()
        user ?: return null
        val pass : String? = binding.et552.text.toString()
        pass ?: return null
        val serv : String? = binding.et553.text.toString()
        serv ?: return null
        val port : String? = binding.et554.text.toString()
        port ?: return null


        acc.add(user)
        acc.add(pass)
        acc.add(serv)
        acc.add(port)
        return acc
    }
    fun isValidAcc(acc: ArrayList<String>?) : Boolean {
        if(acc == null) return false
        return true
    }


}
