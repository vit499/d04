package ru.vit499.d04.ui.misc

import android.app.Application
import ru.vit499.d04.util.Filem
import ru.vit499.d04.util.Logm

class Account() {

    companion object {
        var accUser: String = ""
        var accPass: String = ""
        var accServ: String = ""
        var accPort: String = ""
        var isExist: Boolean = false

        fun fill(): Boolean {
            accUser = Filem.getAccUser()
            Logm.aa("accUser=$accUser")
            accPass = Filem.getAccPass()
            accServ = Filem.getAccServ()
            accPort = Filem.getAccPort()
            isExist = true
            if (accUser.equals("") || accPass.equals("")) {
                isExist = false
            }
            if (accServ.equals("")) {
                accServ = "online.navigard.ru"
            }
            if (accPort.equals("")) {
                accPort = "1883"
            }
            return isExist
        }

        fun setLogin(user: String, pass: String) {
            accUser = user
            accPass = pass
            Filem.setAccUser(accUser)
            Filem.setAccPass(accPass)
        }

        fun setServ(serv: String, port: String) {
            accServ = serv
            accPort = port
            Filem.setAccServ(accServ)
            Filem.setAccPort(accPort)
        }

        fun setAcc(user: String, pass: String, serv: String, port: String) {
            accUser = user
            accPass = pass
            accServ = serv
            accPort = port
            Filem.setAccServ(accServ)
            Filem.setAccPort(accPort)
            Filem.setAccUser(accUser)
            Filem.setAccPass(accPass)
        }
        fun setAcc(s: ArrayList<String>): Boolean {
            accUser = s.get(0)
            accPass = s.get(1)
            accServ = s.get(2)
            accPort = s.get(3)
            Filem.setAccServ(accServ)
            Filem.setAccPort(accPort)
            Filem.setAccUser(accUser)
            Filem.setAccPass(accPass)
            isExist = true
            if (accUser.equals("") || accPass.equals("")) {
                isExist = false
            }
            if (accServ.equals("")) {
                accServ = "online.navigard.ru"
            }
            if (accPort.equals("")) {
                accPort = "1883"
            }
            return(isExist)
        }
    }
}