package ru.vit499.d04

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import ru.vit499.d04.database.Obj
import ru.vit499.d04.database.ObjDatabaseDao
import ru.vit499.d04.ui.misc.Account
import ru.vit499.d04.util.Filem
import ru.vit499.d04.util.Logm
import ru.vit499.d04.util.formStrObj

class MainViewModel(
    val database: ObjDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope((Dispatchers.Main + viewModelJob))

    // current object (name)
    private var _curObjName = MutableLiveData<String>()
    val curObjName : LiveData<String>
        get() = _curObjName

//    private val _objExist = MutableLiveData<Boolean>()
//    val objExit : LiveData<Boolean>
//        get() = _objExist
    private var objExist : Boolean = false
    private var accExist : Boolean = false
//    private val _accExist = MutableLiveData<Boolean>()
//    val accExist : LiveData<Boolean>
//        get() = _accExist

    private val _navigateToNewObj = MutableLiveData<Boolean>()
    val navigateToNewObj : LiveData<Boolean>
        get() = _navigateToNewObj
    fun clrNavigationToNewObj() {
        _navigateToNewObj.value = false
    }

    private val _navigateToAcc = MutableLiveData<Boolean>()
    val navigateToAcc : LiveData<Boolean>
        get() = _navigateToAcc
    fun clrNavigationToAcc() {
        _navigateToAcc.value = false
    }

    private val objs = database.getAllObj()

    private val _curObj = MutableLiveData<Obj?>()
    val curObj : LiveData<Obj?>
        get() = _curObj

    init{
        _navigateToNewObj.value = false
        val objName = Filem.getCurrentObjName()
        _curObjName.value = objName
        objExist = !objName.equals("")
        val acc = Account.fill()
        accExist = acc

        Logm.aa(curObjName.value)

        initCurObj()

        if(!accExist) {
            Logm.aa(" acc not exist ")
            _navigateToAcc.value = true
        }
        else if(!objExist) {
            Logm.aa(" obj name = null ")
            _navigateToNewObj.value = true
        }
    }

    fun initCurObj () {
        uiScope.launch {
            getObjFromDatabase()
        }
    }
    private suspend fun getObjFromDatabase() {
        withContext(Dispatchers.IO) {
            var s = _curObjName.value ?: "0000"
            var obj = database.getObjByName(s)

            Logm.aa(formStrObj(obj))
            obj?.let {
                _curObj.value = obj
            }
        }
    }

    fun onSaveAcc(s: ArrayList<String>) {
        val acc = Account.setAcc(s)
        accExist = acc
        if(!accExist) {
            _navigateToAcc.value = true
        }
        else if(!objExist){
            _navigateToNewObj.value = true
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}