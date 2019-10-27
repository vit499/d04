package ru.vit499.d04.mq

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import ru.vit499.d04.util.Logm

class MqttWorker(
    context: Context,
    parameters: WorkerParameters
    ) : Worker(context, parameters) {


    override fun doWork(): Result {

        val numObj = inputData.getString("CURRENT_NAME")
        if(numObj == null) return Result.failure()
        Logm.aa("mqtt work start $numObj")
        try{

            while(true){
                if(isStopped){
                    Logm.aa("mqqt work cancel")
                    break
                }
            }
        }
        catch (ex: Exception){

        }

        Logm.aa("mqtt work finished")
        return Result.failure()
    }

    override fun onStopped() {
        super.onStopped()
        //Logm.aa("work stoped")
    }
}