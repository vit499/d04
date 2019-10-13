package ru.vit499.d04.fcm

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import ru.vit499.d04.util.Logm


fun getFcmToken () : String {

    var res : String = ""
    FirebaseInstanceId.getInstance().instanceId
        .addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Logm.aa("getInstanceId failed " + task.exception)
                return@OnCompleteListener
            }
            val token = task.result?.token
            Logm.aa(token)
            res  = token ?: ""
        })
    return res
}