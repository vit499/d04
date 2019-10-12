package ru.vit499.d04.http

import android.app.Application
import android.util.Log
import ru.vit499.d04.ui.misc.Account
import ru.vit499.d04.util.Logm
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.InetAddress
import java.net.Socket

class HttpReq(
    val application: Application) {

//    companion object{
//        //val server: String = "online1.navigard.ru"
//        val server: String = "vit499.ru"
//        val port: Int = 80
//        val user: String = "aa11"
//        val pass: String = "aa11"
//        val numObj: String = "2223"
//        val SIZEBUF: Int = 10000
//    }

    val SIZEBUF: Int = 10000
    val server = "vit499.ru" // Account.accServ
    val strPort = Account.accPort
    val port: Int = 80  // strPort.toInt()
    val user = Account.accUser
    val pass = Account.accPass

    var isOpen: Boolean = false
    var socket: Socket? = null
    var outputStreamWriter: OutputStreamWriter? = null
    var inputStreamReader: InputStreamReader? = null
    var inputStream: InputStream? = null
    var rbuf1: CharArray = CharArray(10000)
    var rbuf2: CharArray = CharArray(10000)
    //var rbuf1: ByteArray = ByteArray(10000)
    //var rbuf2: ByteArray = ByteArray(10000)
    var rcnt1: Int = 0
    var rcnt2: Int = 0

    fun Send(
        strSend: String,
        resultReq: (String) -> Unit
    ) : Boolean {
        isOpen = false
        try{
            socket = Socket(InetAddress.getByName(server), port)
            Logm.aa("connected")
            socket?.setSoTimeout(2000)
            val outputStream : OutputStream? = socket?.getOutputStream() ?: return false
            val inputStream : InputStream? = socket?.getInputStream() ?: return false
            outputStreamWriter = OutputStreamWriter(outputStream!!)
            inputStreamReader = InputStreamReader(inputStream!!)
            //inputStream = socket?.getInputStream()

            rcnt2 = 0
            isOpen = true


            //rcnt = inputStreamReader?.read(rbuf) ?: 0
            //rcnt = inputStream?.read(rbuf1) ?: 0

            //Log.i("aa", "rcnt=${rcnt.toString()} b=${rbuf1.toString()}")

            val thread = Thread(){
                while (true) {
                    if(!isOpen) {
                        break;
                    }
                    try{
                        //rcnt1 = inputStream?.read(rbuf1) ?: 0
                        rcnt1 = inputStreamReader?.read(rbuf1) ?: 0

                        for (j in 0 until rcnt1) {
                            if (rcnt2 < SIZEBUF) {
                                rbuf2[rcnt2++] = rbuf1[j]
                            }
                        }
                        if(rcnt2 >= 100) {
                            val s: String = String(rbuf2, 0, rcnt2)
                            Logm.aa("rcnt=${rcnt2.toString()} b=$s")
                            resultReq(s)
                            break
                        }
                    }
                    catch(ex: Exception){
                        Logm.aa("rec exc: ${ex.toString()}")
                    }
                }
            }
            thread.start()

            outputStreamWriter?.write(strSend)
            outputStreamWriter?.flush()
            Logm.aa("sended")


        }
        catch(ex: Exception) {
            Logm.aa("open exc: ${ex.toString()}")
        }
        return(isOpen)
    }

    fun Send1(strSend: String, resultReq: (String) -> Unit){
        val thread = Thread() {
            Send(strSend, resultReq)
        }
        thread.start()
    }

    fun Close(){

        try {
            outputStreamWriter?.close()
            inputStreamReader?.close()
            socket?.shutdownOutput()
            socket?.shutdownInput()
            socket?.close()
        }
        catch(ex: Exception){
            Logm.aa("close exc: ${ex.toString()}")
        }
        isOpen = false
    }

    class RecThread: Thread() {
        public override fun run() {
            while (true) {
                try{
                    //rcnt = inputStream?.read(rbuf1) ?: 0
                }
                catch(ex: Exception){

                }
            }
        }
    }
}