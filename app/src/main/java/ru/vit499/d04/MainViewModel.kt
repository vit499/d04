package ru.vit499.d04

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.vit499.d04.database.Obj
import ru.vit499.d04.database.ObjDatabaseDao
import ru.vit499.d04.ui.misc.Account
import ru.vit499.d04.util.Filem
import ru.vit499.d04.util.Logm

class MainViewModel(
    val database: ObjDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var _curObjName = MutableLiveData<String>()
    val curObjName : LiveData<String>
        get() = _curObjName

    private val _objExist = MutableLiveData<Boolean>()
    val objExit : LiveData<Boolean>
        get() = _objExist
    private val _accExist = MutableLiveData<Boolean>()
    val accExist : LiveData<Boolean>
        get() = _accExist


    init{
        val objName = Filem.getCurrentObjName()
        _curObjName.value = objName
        _objExist.value = !objName.equals("")
        val acc = Account.fill()
        _accExist.value = acc

        Logm.aa(curObjName.value)
    }

    fun saveAcc(s: ArrayList<String>) {
        val acc = Account.setAcc(s)
        _accExist.value = acc
    }
}