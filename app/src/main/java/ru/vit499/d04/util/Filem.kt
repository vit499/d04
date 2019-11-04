package ru.vit499.d04.util

import android.app.Application
import android.content.Context
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Filem() {

    companion object{

        val fileCurObj : String = "CurrentObj.txt"
        val fileAccUser : String = "accUser.txt"
        val fileAccPass : String = "accPass.txt"
        val fileAccServ : String = "accServ.txt"
        val fileAccPort : String = "accPort.txt"
        val fileSetMqEn : String = "setMqEn.txt"
        var strDir : String = ""
        //val strDir : String = (application as Context).getFilesDir().getPath().toString()
        var currentLogFile : String = "1"

        fun setDir(application: Application) {
            strDir = (application as Context).getFilesDir().getPath().toString()

            //val sdf = SimpleDateFormat("YYMMdd_HHmmss", Locale.getDefault())
            //currentLogFile = sdf.format(Date())

            Logm.aa("strDir = $strDir")
        }
        fun getVal(f1 : String): String {
            var str : String = ""
            var f = strDir + "/" + f1
            try {
                val file = File(f)
                if (file.exists()) {
                    str = File(f).readText()
                }
            }
            catch (ex: Exception){
                //Logm.ex(ex)
            }
           // Logm.aa("get val $str from file $f ")
            return str
        }
        fun setVal(f1: String, str: String) {
            var f = strDir + "/" + f1
            try {
                val file = File(f)
               // Logm.aa("save val $str to file $f")
                file.writeText(str)
            }
            catch(ex: Exception){
                Logm.ex(ex)
            }
        }

        fun getAccUser() : String {
            return(getVal(fileAccUser))
        }
        fun getAccPass() : String {
            return(getVal(fileAccPass))
        }
        fun getAccServ() : String {
            return(getVal(fileAccServ))
        }
        fun getAccPort() : String {
            return(getVal(fileAccPort))
        }
        fun getCurrentObjName() : String {
            var s = getVal(fileCurObj)
            if(s.equals("")) s = "-"
            return(s)
        }
        fun getSetMqEn() : String {
            var s = getVal(fileSetMqEn)
            if(s.equals("")) s = "1"
            return(s)
        }

        fun setAccUser(s : String) {
            return(setVal(fileAccUser, s))
        }
        fun setAccPass(s : String) {
            return(setVal(fileAccPass, s))
        }
        fun setAccServ(s : String) {
            return(setVal(fileAccServ, s))
        }
        fun setAccPort(s : String) {
            return(setVal(fileAccPort, s))
        }
        fun setCurrentObjName(s : String) {
            return(setVal(fileCurObj, s))
        }
        fun setSetMqEn(s : String) {
            return(setVal(fileSetMqEn, s))
        }
    }
}