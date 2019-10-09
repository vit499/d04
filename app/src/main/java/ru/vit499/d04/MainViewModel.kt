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
    private var curObjKey : Long = 0
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

    val objs = database.getAllObj()

    private val _curObj = MutableLiveData<Obj?>()
    val curObj : LiveData<Obj?>
        get() = _curObj

    init{
        Filem.setDir(application)
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

    // извлечение активного объекта
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
                curObjKey = obj.objId
            }
        }
    }


    fun onAddObj (s: ArrayList<String>) {
        val obj = Obj()
        obj.objName = s.get(0)
        obj.objDescr = s.get(1)
        obj.objCode = s.get(2)
        uiScope.launch {
            withContext(Dispatchers.IO){
                database.insert(obj)
            }
        }
    }
    fun onUpdateObj (s: ArrayList<String>) {
        val obj = _curObj.value ?: return
        if(obj.objDescr.equals(s.get(0)) && obj.objCode.equals(s.get(1))) return
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val obj = _curObj.value ?: return@withContext
                //val obj = database.get(curObjKey) ?: return@withContext
                obj.objDescr = s.get(0)
                obj.objCode = s.get(1)
                database.update(obj)
                _curObj.value = obj
            }
        }
    }
    fun onDeleteObj () {
        val obj = _curObj.value ?: return
        val s = _curObjName.value ?: return
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.deleteObjByName(s)
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
        Logm.aa("shutdown")
    }
}