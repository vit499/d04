package ru.vit499.d04.util

import android.app.Application
import android.content.Context
import java.io.File
import java.lang.Exception

class Filem() {

    companion object{

        val fileCurObj : String = "CurrentObj.txt"
        val fileAccUser : String = "accUser.txt"
        val fileAccPass : String = "accPass.txt"
        val fileAccServ : String = "accServ.txt"
        val fileAccPort : String = "accPort.txt"
        var strDir : String = ""
        //val strDir : String = (application as Context).getFilesDir().getPath().toString()

        fun setDir(application: Application) {
            strDir = (application as Context).getFilesDir().getPath().toString()
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
                Logm.aa(ex.toString())
            }
            Logm.aa("get val $str from file $f ")
            return str
        }
        fun setVal(f1: String, str: String) {
            var f = strDir + "/" + f1
            try {
                val file = File(f)
                Logm.aa("save val $str to file $f")
                file.writeText(str)
            }
            catch(ex: Exception){
                Logm.aa(ex.toString())
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
    }
}