package ru.vit499.d04.util

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.StringBuilder

class Logm {

    companion object {

        var strL : String? = null
        var sb = StringBuilder()

        fun aa(s: String?) {
            s.let {
                var a = s
                if(a.equals("")) a = "-"
                Log.i("aa", a)

                //var temp = strL + "\r\n" + a
                sb.append("\r\n")
                sb.append(a)
                //strL = temp
            }
        }

        fun clear() {
            sb.clear()
        }
        fun getLog() : String {
            return sb.toString()
        }
    }
}

//class LogViewModel() : ViewModel() {
//
//    private var _strLog = MutableLiveData<String?>()
//    val strLog : LiveData<String?>
//        get() = _strLog
//    fun onClearLog() {
//        _strLog.value = ""
//    }
//
//    fun getLog() {
//        //val s1 = _strLog.value + s
//        val strLog = Logm.strL
//        _strLog.value = strLog
//    }
//
//    init {
//        _strLog.value = ""
//    }
//}