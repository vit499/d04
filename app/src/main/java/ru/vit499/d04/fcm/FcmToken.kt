package ru.vit499.d04.fcm

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.coroutines.*
import ru.vit499.d04.util.Logm
import ru.vit499.d04.util.strSendToken
import com.google.android.gms.tasks.Tasks
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import androidx.annotation.NonNull




class FcmToken {

    companion object {

        var strToken : String? = null

        suspend fun getFcmToken4(): String? {
            strToken = null
            return withContext(Dispatchers.Default) {
                val task = FirebaseInstanceId.getInstance().instanceId
                //if(task == null) return@withContext null
                try {
                    val result = Tasks.await(task, 2000, TimeUnit.MILLISECONDS)
                    val token = result.token
                    Logm.aa("token=$token")
                    strToken = token;
                } catch (e: ExecutionException) {
                    Logm.aa(e.toString())
                    return@withContext null
                } catch (e: InterruptedException) {
                    Logm.aa(e.toString())
                    return@withContext null
                } catch (e: TimeoutException) {
                    Logm.aa(e.toString())
                    return@withContext null
                }
                strToken
            }
        }

        fun getFcmToken() {
            strToken = ""
            //var res: String = ""
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Logm.aa("getInstanceId failed " + task.exception)
                        return@OnCompleteListener
                    }
                    val token = task.result?.token
                    Logm.aa("fb_listener:")
                    Logm.aa(token)
                    strToken = token
                })
        }

        suspend fun waitToken() : String {
            var res : String = ""
            return withContext(Dispatchers.Default){
                withTimeout(5000){
                    while(true) {
                        if (strToken != null && !strToken.equals("")) {
                            res = strToken!!
                            break
                        }
                    }
                }
                res
            }
        }

//        suspend fun getFcmToken23 (arr: ArrayList<String>, send: suspend (String) -> Unit) {
//            strToken = ""
//            //var res: String = ""
//            withContext(Dispatchers.Default) {
//                FirebaseInstanceId.getInstance().instanceId
//                    .addOnCompleteListener(OnCompleteListener { task ->
//                        if (!task.isSuccessful) {
//                            Logm.aa("getInstanceId failed " + task.exception)
//                            return@OnCompleteListener
//                        }
//                        val token = task.result?.token
//                        Logm.aa("fb_listener:")
//                        Logm.aa(token)
//                        strToken = token ?: ""
//                        val strReq = strSendToken(arr, token!!)
//                        withContext(Dispatchers.Default) {
//                            send(strReq)
//                        }
//                    })
//            }
//        }
    }
}

