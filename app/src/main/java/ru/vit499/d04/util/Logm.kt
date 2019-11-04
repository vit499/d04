package ru.vit499.d04.util

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crashlytics.android.Crashlytics
import java.io.File
import java.lang.Exception
import java.lang.StringBuilder

class Logm {

    companion object {

        var strL : String? = null
        var sb = StringBuilder()

        fun aa(s: String?) {
            s?.let {
                var a = s
                if(a.equals("")) a = "-"
                Log.i("aa", a)

                //var temp = strL + "\r\n" + a
                sb.append("\r\n")
                sb.append(a)
                //strL = temp
            }
        }

        fun aa(b: Buf){
            //if(b == null) return
            //if(len == null) return
            val s : String? = Str.hex2str(b.buf, b.len)
            if(s == null) return
            Log.i("aa", s)
        }

        fun aa(b: ByteArray, len: Int){
            //if(b == null) return
            //if(len == null) return
            val s : String? = Str.hex2str(b, len)
            if(s == null) return
            Log.i("aa", s)
        }

        fun ex(e: Exception){
            Crashlytics.logException(e)
        }

        fun clear() {
            sb.clear()
        }
        fun getLog() : String {
            return sb.toString()
        }

        var currentLogFile : String = "1"

        fun setVal(f1: String, str: String) {
            var f = Filem.strDir + "/" + f1
            try {
                val file = File(f)
                // Logm.aa("save val $str to file $f")
                file.writeText(str)
            }
            catch(ex: Exception){
                //Logm.ex(ex)
            }
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