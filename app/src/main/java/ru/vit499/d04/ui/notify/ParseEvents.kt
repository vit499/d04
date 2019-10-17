package ru.vit499.d04.ui.notify

import ru.vit499.d04.util.Logm
import ru.vit499.d04.util.Str
import java.util.ArrayList

class ParseEvents {


    companion object {

        fun ParseEvents(s: String): ArrayList<NotifyItem> {
            val evLists = ArrayList<NotifyItem>()

            //Logm.aa(s)
            val src = s.toByteArray()
            val len_src = src.size

            var a = 0
            var b = 0
            var c = 0
            var i: Int

            while (true) {
                a = -1
                b = -1
                i = c
                while (i < len_src) {
                    if (src[i] == '{'.toByte()) {
                        a = i
                        break
                    }
                    i++
                }
                i = c
                while (i < len_src) {
                    if (src[i] == '}'.toByte()) {
                        b = i
                        c = i + 1
                        break
                    }
                    i++
                }
                if (a == -1 || b == -1) break
                val len_str = b - a
                if (len_str < 40) continue
                //String s1 = Str.byte2str(src, a, len_str);    // {"dv_ev":"7754E60200000","dv_time":"20190720183717"},
                //Logm.Log(s1);
                val k1 = Str.indexof(src, a, '"'.toByte(), 3, len_str) // s1.indexOf(':'); // + 2;
                if (k1 == -1) continue
                val k2 = Str.indexof(src, a, '"'.toByte(), 4, len_str) // s1.indexOf(':'); // + 2;
                if (k2 == -1) continue
                val s2 = Str.byte2str(src, a + k1, a + k2 - 1)
                val k3 = Str.indexof(src, a, '"'.toByte(), 7, len_str) // s1.indexOf(':'); // + 2;
                if (k3 == -1) continue
                val k4 = Str.indexof(src, a, '"'.toByte(), 8, len_str) // s1.indexOf(':'); // + 2;
                if (k4 == -1) continue
                val s3 = Str.byte2str(src, a + k3, a + k4 - 1)
                //k1 += 2;
                //String s2 = s1.substring(k1, k1+13);
                //int k2 = s1.indexOf(':', k1 + 13);
                //if(k2 == -1) continue;
                //k2 += 2;
                //String s3 = s1.substring(k2, k2+10);
                val ev = NotifyItem(s2, s3)
                evLists.add(ev)
                if (c >= len_src) break
                if (i >= len_src) break
                //if(list.size() > 10) break;
            }
            //Logm.Log("sz evList=" + evLists.size.toString())
            //Logm.aa("ev: ${evLists.toString()}")
            return evLists
        }
    }
}