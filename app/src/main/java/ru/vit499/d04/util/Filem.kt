package ru.vit499.d04.util

import java.io.File
import java.lang.Exception

class Filem {

    companion object{

        val fileCurObj : String = "CurrentObj.txt"
        val fileAccUser : String = "accUser.txt"
        val fileAccPass : String = "accPass.txt"
        val fileAccServ : String = "accServ.txt"
        val fileAccPort : String = "accPort.txt"

        fun getVal(f : String): String {
            var str : String = ""
            try {
                val file = File(f)
                if (file.exists()) {
                    str = File(f).readText()
                }
            }
            catch (ex: Exception){
                Logm.aa(ex.toString())
            }
            return str
        }
        fun setVal(f: String, str: String) {
            try {
                val file = File(f)
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
            return(getVal(fileCurObj))
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