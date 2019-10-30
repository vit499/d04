package ru.vit499.d04.ui.misc

import ru.vit499.d04.util.Filem

class Settings {

    companion object{

        var mqttEnable : Boolean = false

        fun initSettings() {

            val s = Filem.getSetMqEn()
            if(s.equals("1")) mqttEnable = true
            else mqttEnable = false
        }
        fun updMqttEnable(b: Boolean) {
            if(mqttEnable != b) {
                mqttEnable = b
                var s = "0"
                if(mqttEnable) s = "1"
                Filem.setSetMqEn(s)
            }
        }
    }
}