package ru.vit499.d04.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.vit499.d04.MainActivity
import ru.vit499.d04.R
import ru.vit499.d04.util.Logm
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import ru.vit499.d04.ui.misc.Account
import ru.vit499.d04.util.Stp


class FbNotifyService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {


        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Logm.aa("Fb message")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            //Log.d(TAG, "Message data payload: " + remoteMessage.data)

            val p1 = remoteMessage.getData().get("name");
            val p2 = remoteMessage.getData().get("dv_ev");
            if(p1 != null && p2 != null && !p2.equals("")) {
                sendNotification(p1, "event")
            }

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob()
//            } else {
//                // Handle message within 10 seconds
//                handleNow()
//            }
        }

        // Check if message contains a notification payload.
//        remoteMessage.notification?.let {
//            Logm.aa("Message Notification Body: ${it.body}")
//        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
      //  sendNotification("abcd")
    }

    private fun scheduleJob() {
        // [START dispatch_job]
        //val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
        //WorkManager.getInstance().beginWith(work).enqueue()
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    //
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }


    private fun sendNotification(numObj: String, messageBody: String) {
//        if(Stp.getEn()){
//            Logm.aa(" ----------------------- en -------------- ")
////            val intentBr = Intent("FB1")
////            intentBr.putExtra("NOTICE", messageBody)
////            intentBr.putExtra("OBJ", numObj)
////            this.sendBroadcast(intentBr)
//            Stp.setFbId(numObj, messageBody)
//            //return
//        }
//        else {
//            Logm.aa(" ------------------------- dis ------------- ")
//        }
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra("NOTICE", messageBody)
        intent.putExtra("OBJ", numObj)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0 /* Request code */,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val mesTitle = "d04"
        val mesText = numObj + " " + messageBody
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val vibrate = longArrayOf(200, 500, 200)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notify2)
            .setContentTitle(mesTitle)
            .setContentText(mesText)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setVibrate(vibrate)
            .setLights(Color.WHITE, 100, 3000)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
       // val id = numObj.toInt()
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }


    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}