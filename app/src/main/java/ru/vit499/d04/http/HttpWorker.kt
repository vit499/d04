package ru.vit499.d04.http

import android.content.Context
import android.os.CountDownTimer
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.*
import ru.vit499.d04.ui.misc.Account
import ru.vit499.d04.util.Logm
import java.io.*
import java.net.InetAddress
import java.net.Socket

class HttpWorker(
    context: Context,
    param: WorkerParameters
) : CoroutineWorker(context, param) {

    companion object{
        const val WORK_NAME = "HttpWorker"
        val SIZEBUF: Int = 100000
        val ONE_SEC : Long = 1000
        val port: Int = 80  // strPort.toInt()
        val tmax: Int = 10
    }

    val server = Account.accServ
    //val strPort = Account.accPort

    private val timer: CountDownTimer

    var isOpen: Boolean = false
    var socket: Socket? = null
    var outputStreamWriter: OutputStreamWriter? = null
    //var inputStreamReader: InputStreamReader? = null
    var dataInputStream : DataInputStream? = null
    var inputStream: InputStream? = null
    var rbuf1: ByteArray = ByteArray(SIZEBUF)
    var rbuf2: ByteArray = ByteArray(SIZEBUF)
    var rcnt1: Int = 0
    var rcnt2: Int = 0

    init {
        timer = object : CountDownTimer(tmax.toLong()*ONE_SEC, ONE_SEC) {
            override fun onTick(mSec: Long) {
                val t = (mSec / ONE_SEC)
                //Logm.aa("timer=$t")
            }

            override fun onFinish() {
                Logm.aa("timer end")
                Close()
            }
        }
        timer.start()
    }

    fun send(
        strSend: String
    ) : Boolean {
        isOpen = false
        //var resStr : String = "error"
        try{
            socket = Socket(InetAddress.getByName(server), port)
            Logm.aa("connected")
            socket?.setSoTimeout(2000)
            val outputStream : OutputStream? = socket?.getOutputStream() ?: return false
            val inputStream : InputStream? = socket?.getInputStream() ?: return false
            outputStreamWriter = OutputStreamWriter(outputStream!!)
            dataInputStream = DataInputStream(inputStream!!)

            rcnt2 = 0

            outputStreamWriter?.write(strSend)
            outputStreamWriter?.flush()
            Logm.aa("sended")
            isOpen = true
        }
        catch(ex: java.lang.Exception) {
            Logm.aa("open exc: ${ex.toString()}")
            Logm.ex(ex)
        }
        catch(ex: IOException){
            Logm.aa("open exc: ${ex.toString()}")
            Logm.ex(ex)
        }
        return(isOpen)
    }

    suspend fun rec (timeout: Long, wa : Int) : String {
        val t = timeout * 1000
        return withContext(Dispatchers.Default) {
            var resStr: String = "error"
            withTimeout(t) {
                while (true) {
                    if (!isActive) {
                        Logm.aa("rec end isActive")
                        Close()
                        break;
                    }
                    if(socket == null || dataInputStream == null) {
                        Logm.aa("rec end socket null")
                        Close()
                        break;
                    }
                    try {
                        if(socket!!.isClosed || !socket!!.isConnected) {
                            Logm.aa("rec end socket closed")
                            Close()
                            break;
                        }
                        rcnt1 = dataInputStream?.read(rbuf1) ?: 0
                        if(rcnt1 > 0) {
                            for (j in 0 until rcnt1) {
                                if (rcnt2 < SIZEBUF) {
                                    rbuf2[rcnt2++] = rbuf1[j]
                                }
                            }
                            var s : String? = null
                            if(wa == 1) {
                                s = recState(rbuf2, rcnt2)
                            }
                            else if(wa == 2) {
                                s = recEvent(rbuf2, rcnt2)
                            }
                            else {
                                s = rec1111(rbuf2, rcnt2)
                            }
                            if (s != null) {
                                resStr = s
                                Close()
                                break
                            }
                        }
                    } catch (ex: IOException) {
                        Logm.aa("rec IOexc: ${ex.toString()}")
                    } catch (ex: java.lang.Exception){
                        Logm.aa("rec exc: ${ex.toString()}")
                    }
                }
            }
            resStr
        }
    }

    fun Close(){
        Logm.aa("http close")
        try {
            outputStreamWriter?.close()
            dataInputStream?.close()
            socket?.shutdownOutput()
            socket?.shutdownInput()
            socket?.close()
        }
        catch(ex: java.lang.Exception){
            //Logm.aa("close exc: ${ex.toString()}")
        }
        isOpen = false
    }

    override suspend fun doWork(): Result = coroutineScope {
        Logm.aa("worker start ... ")
        var s: String = "error"
        try{
            val strReq = inputData.getString("str")
            val b = send(strReq!!)
            if (b) {
                s = rec(15, 1)
            }
            else {
                s = "error"
            }

            Logm.aa("work res: $s")
            Result.success()
        }
        catch (ex: Exception){
            Result.failure()
        }
    }
}