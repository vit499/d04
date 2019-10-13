package ru.vit499.d04.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import ru.vit499.d04.ui.misc.Account
import ru.vit499.d04.util.Logm
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.InetAddress
import java.net.Socket

class HttpCor() {

    val SIZEBUF: Int = 10000
    val server = Account.accServ
    //val strPort = Account.accPort
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

    fun send(
        strSend: String
    ) : Boolean {
        isOpen = false
        var resStr : String = "error"
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

            outputStreamWriter?.write(strSend)
            outputStreamWriter?.flush()
            Logm.aa("sended")

        }
        catch(ex: Exception) {
            Logm.aa("open exc: ${ex.toString()}")
        }
        return(isOpen)
    }

    suspend fun rec (timeout: Long) : String {
        val t = timeout * 1000
        return withContext(Dispatchers.Default) {
            var resStr: String = "error"
            withTimeout(t) {
                while (true) {
                    if (!isOpen) {
                        Close()
                        break;
                    }
                    if (!isActive) {
                        Close()
                        break;
                    }
                    try {
                        rcnt1 = inputStreamReader?.read(rbuf1) ?: 0

                        for (j in 0 until rcnt1) {
                            if (rcnt2 < SIZEBUF) {
                                rbuf2[rcnt2++] = rbuf1[j]
                            }
                        }
                        val s = String(rbuf2, 0, rcnt2)
                        Logm.aa("rcnt=${rcnt2.toString()} b=$s")
                        if (rcnt2 >= 5000) {
                            //val s: String = String(rbuf2, 0, rcnt2)
                            Logm.aa("rcnt=${rcnt2.toString()} b=$s")
                            //resultReq(s)
                            resStr = s
                            Close()
                            break
                        }
                    } catch (ex: Exception) {
                        Logm.aa("rec exc: ${ex.toString()}")
                    }
                }
            }
            resStr
        }
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

    suspend fun reqStat(strSend : String, timeout: Long) : String {
        var s = ""
        return withContext(Dispatchers.Default) {
            val b = send(strSend)
            if (b) {
                s = rec(timeout)
            }
            Close()
            s
        }
    }

}