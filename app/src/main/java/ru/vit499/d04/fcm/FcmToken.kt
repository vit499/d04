package ru.vit499.d04.fcm

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import ru.vit499.d04.util.Logm

class FcmToken {

    companion object {

        var strToken : String? = null

        fun getFcmToken() {

            //var res: String = ""
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Logm.aa("getInstanceId failed " + task.exception)
                        return@OnCompleteListener
                    }
                    val token = task.result?.token
                    Logm.aa(token)
                    strToken = token
                })
        }

        suspend fun waitToken() : String {
            var res : String = ""
            return withContext(Dispatchers.Default){
                withTimeout(5000){
                    while(true) {
                        if (strToken != null) {
                            res = strToken!!
                            break
                        }
                    }
                }
                res
            }
        }
    }
}

