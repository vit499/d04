package ru.vit499.d04.util

import android.util.Log

class Logm {

    companion object {

        fun aa(s: String?) {
            s.let {
                var a = s
                if(a.equals("")) a = "-"
                Log.i("aa", a)
            }
        }
    }
}