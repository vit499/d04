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
//    private var _curObjName = MutableLiveData<String>()
//    val curObjName : LiveData<String>
//        get() = _curObjName

//    private val _objExist = MutableLiveData<Boolean>()
//    val objExit : LiveData<Boolean>
//        get() = _objExist
    private var objExist : Boolean = false
    private var accExist : Boolean = false
    private var curObjKey : Long = 0
    private var curObjName : String = ""
    private var curObjEditName : String = ""
//    private val _accExist = MutableLiveData<Boolean>()
//    val accExist : LiveData<Boolean>
//        get() = _accExist

    // если нет объектов, то из mainFragment переходим в добавление объекта
    private val _navigateToNewObj = MutableLiveData<Boolean>()
    val navigateToNewObj : LiveData<Boolean>
        get() = _navigateToNewObj
    fun clrNavigationToNewObj() {
        _navigateToNewObj.value = false
    }

    // если нет аккаунта, то переходим в настройки аккаунта
    private val _navigateToAcc = MutableLiveData<Boolean>()
    val navigateToAcc : LiveData<Boolean>
        get() = _navigateToAcc
    fun clrNavigationToAcc() {
        _navigateToAcc.value = false
    }

    // возврат из редактирования объекта
    private val _navigateToObj = MutableLiveData<Boolean>()
    val navigateToObj : LiveData<Boolean>
        get() = _navigateToObj
    fun clrNavigateToObj() {
        _navigateToObj.value = false
    }

    val objs = database.getAllObj()

    private val _curObj = MutableLiveData<Obj?>()
    val curObj : LiveData<Obj?>
        get() = _curObj

    private val _curObjEdit = MutableLiveData<Obj?>()
    val curObjEdit : LiveData<Obj?>
        get() = _curObjEdit

    init{
        Filem.setDir(application)
        _navigateToNewObj.value = false
        curObjName = Filem.getCurrentObjName()
        objExist = !curObjName.equals("")
        val acc = Account.fill()
        accExist = acc

        Logm.aa(curObjName)

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

    // извлечение активного объекта при старте
    fun initCurObj () {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val s = curObjName ?:return@withContext
                if(s.equals("")) return@withContext
                val obj = database.getObjByName(s)

                Logm.aa(formStrObj(obj))
                if(obj != null) {
                    _curObj.postValue(obj)
                    curObjKey = obj.objId
                }
                else {
                    val obj1 = database.getObj()
                    if(obj1 != null){
                        Logm.aa("obj1 ")
                        _curObj.postValue(obj1)
                        curObjKey = obj1.objId
                        Filem.setCurrentObjName(curObjName)
                    }
                    else {
                        Logm.aa("empty ")
                        _curObj.postValue(null)
                        objExist = false
                        _navigateToNewObj.postValue(true)
                    }
                }
            }
        }
    }

    // выбор текущего объекта
    fun getObjById (id: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO){
                val obj = database.getObjById(id)
                obj?.let {
                    _curObj.postValue(obj)
                    curObjName = obj.objName
                    Filem.setCurrentObjName(curObjName)

                } ?: return@withContext
            }
        }
    }
    fun getObjEditById (id: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO){
                val obj = database.getObjById(id)
                obj?.let {
                    _curObjEdit.postValue(obj)
                } ?: return@withContext
            }
        }
    }


    fun onAddObj (s: ArrayList<String>) {
        Logm.aa("n=${s.get(0)} d=${s.get(1)} c=${s.get(2)}")
        val obj = Obj()
        obj.objName = s.get(0)
        obj.objDescr = s.get(1)
        obj.objCode = s.get(2)

        uiScope.launch {
            withContext(Dispatchers.IO){
                database.insert(obj)
                _curObj.postValue(obj)
                curObjName = obj.objName
                Filem.setCurrentObjName(curObjName)
                objExist = true
                Logm.aa("obj cnt= ${objs.value?.size}")
            }
        }
    }
    fun onEditObj (s: ArrayList<String>) {
        Logm.aa("onEdit, name: ${s.get(0)}")
        val obj = _curObjEdit.value ?: return
        Logm.aa("onEditOjb, ${obj.objName}")
        if(obj.objDescr.equals(s.get(1)) && obj.objCode.equals(s.get(2))) return
        Logm.aa("start update")
        val idObj = obj.objId
        uiScope.launch {
            withContext(Dispatchers.IO) {
                //val cObj = database.getObjById(idObj) ?: return@withContext
                obj.objDescr = s.get(1)
                obj.objCode = s.get(2)
                database.update(obj)
                Logm.aa("updated")
                //_curObj.value = obj
                _navigateToObj.postValue(true)
                Logm.aa("return")
            }
        }
    }
    fun onDeleteObj () {
        val obj = _curObjEdit.value ?: return
        val s = obj.objName
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.deleteObjByName(s)
                if(s.equals(curObjName)) {
                    val cObj = database.getObj()
                    if(cObj != null) {
                        _curObj.postValue(cObj)
                        curObjName = cObj.objName
                    }
                    else {
                        _curObj.postValue(null)
                        curObjName = "-"
                        objExist = false
                        _navigateToNewObj.postValue(true)
                    }
                }
                _navigateToObj.postValue(true)
            }
        }
    }

    // установка (выбор) текущего объекта
    fun onCurrentObj (id: Long) {
        getObjById(id)
    }
    fun getCurrentObj () : Obj? {
        val obj = _curObj.value
        return obj
    }

    // установка (выбор) объекта для редактирования, удаления
    fun onCurrentObjEdit (id: Long) {
        getObjEditById(id)
    }
    fun getCurrentObjEdit () : Obj? {
        val obj = _curObjEdit.value
        return obj
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