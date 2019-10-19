package ru.vit499.d04

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import ru.vit499.d04.database.Obj
import ru.vit499.d04.database.ObjDatabaseDao
import ru.vit499.d04.fcm.FcmToken
import ru.vit499.d04.http.HttpCor
import ru.vit499.d04.objstate.ObjStringUpd
import ru.vit499.d04.ui.misc.Account
import ru.vit499.d04.ui.notify.NotifyItem
import ru.vit499.d04.ui.notify.ParseEvents
import ru.vit499.d04.util.*
import ru.vit499.d04.util.Str
import ru.vit499.d04.objstate.ObjState
import ru.vit499.d04.objstate.OutState
import ru.vit499.d04.ui.main.StatList
import ru.vit499.d04.ui.outputs.OutItem


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

    val objs = database.getAllObj1()

    private val _listObj = MutableLiveData<List<Obj>>()
    val listObj : LiveData<List<Obj>>
        get() = _listObj

    private val _curObj = MutableLiveData<Obj?>()
    val curObj : LiveData<Obj?>
        get() = _curObj

    private val _curObjEdit = MutableLiveData<Obj?>()
    val curObjEdit : LiveData<Obj?>
        get() = _curObjEdit

    private val _statList = MutableLiveData<List<StatList>>()
    val statList : LiveData<List<StatList>>
        get() = _statList

    private val _outList = MutableLiveData<List<OutItem>>()
    val outList : LiveData<List<OutItem>>
        get() = _outList

    private val _navigateBackFromObj = MutableLiveData<Boolean>()
    val navigateBackFromObj : LiveData<Boolean>
        get() = _navigateBackFromObj
    fun onNavigateBackFromObj () {
        _navigateBackFromObj.value = false
    }

    private val _navigateToEditObj = MutableLiveData<Boolean>()
    val navigateToEditObj : LiveData<Boolean>
        get() = _navigateToEditObj
    fun onNavigateToEditObj () {
        _navigateToEditObj.value = false
    }

    private val _navBackFromEditObj = MutableLiveData<Boolean>()
    val navBackFromEditObj : LiveData<Boolean>
        get() = _navBackFromEditObj
    fun onNavBackFromEditObj(){
        _navBackFromEditObj.value = false
    }

    init{
        Log.i("aa", "--- init --- ")
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
        _navigateBackFromObj.value = false
        _navigateToEditObj.value = false
        _navigateToObj.value = false
    }

    // извлечение активного объекта при старте
    fun initCurObj () {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val s = curObjName ?:return@withContext
                if(s.equals("")) return@withContext
                val obj = database.getObjByName(s)

                //val arr : List<Obj> = database.getAllObj()
                //Logm.aa("arr: ${arr.size.toString()}")
                //_listObj.postValue(arr)

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
                //Logm.aa("obj cnt= ${objs.value?.size.toString()}")
                //Logm.aa("obj cnt= ${_listObj.value?.size.toString()}")
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
                    Logm.aa("new current obj: ${curObjName}")
                    _navigateBackFromObj.postValue(true)
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
                    _navigateToEditObj.postValue(true)
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
                _navigateToObj.postValue(true)
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
        httpJob.cancel()
        httpR?.Close()
        Logm.aa("shutdown")
    }

    //--------------------------

    val httpJob = Job()
    val httpScope = CoroutineScope(Dispatchers.Main + httpJob)

    private var _strHttpStat = MutableLiveData<String>()
    val strHttpStat : LiveData<String>
        get() = _strHttpStat

    private var _progress = MutableLiveData<Boolean>()
    val progress : LiveData<Boolean>
        get() = _progress

    private var httpR: HttpCor? = null

    fun onReqStat () {
        //val strReq = "GET / HTTP/1.1\r\nHost: vit499.ru\r\n\r\n"
        val strReq = strReqHttp(curObjName, "state")
        Logm.aa(strReq)
        _progress.value = true
        if(httpR == null) httpR = HttpCor()
        httpScope.launch {
            val s = httpR?.reqStat(strReq, 1, 10) ?: "-"
            UpdState(s)
            _progress.postValue(false)
        }
    }
    fun onReqEvent () {
        //val strReq = "GET / HTTP/1.1\r\nHost: vit499.ru\r\n\r\n"
        val strReq = strReqHttp(curObjName, "events")
        Logm.aa(strReq)
        _progress.value = true
        if(httpR == null) httpR = HttpCor()
        httpScope.launch {
            val s = httpR?.reqStat(strReq, 2, 10) ?: "-"
            updEvents(s)
            _progress.postValue(false)
        }
    }

    fun onHttpClose(){
        httpR?.Close()
        //httpJob.cancel()
    }

    //-------------------- fcm -------------

    suspend fun getArrObj() : ArrayList<String>? {
        return withContext(Dispatchers.IO) {
            var arr = ArrayList<String>()
            val arrO : List<Obj> = database.getAllObj()

            val cnt = arrO.size ?: 0
            //Logm.aa("cnt: ${cnt.toString()}")
            for (i in 0 until cnt) {
                val s = arrO.get(i)?.objName
                s?.let { arr.add(s) }
            }
            arr
        }
    }
    fun onFbSub () {

        _progress.value = true
        httpScope.launch {
            var arr = getArrObj() ?: return@launch
            //Logm.aa("arr: ${arr.toString()}")
            FcmToken.getFcmToken()
            val token = FcmToken.waitToken()
            val strReq = strSendToken(arr, token)
            Logm.aa(strReq)
            if(httpR == null) httpR = HttpCor()
            val s = httpR?.reqStat(strReq, 3, 10) ?: "-"
            updObjStat(s)
            _progress.postValue(false)
        }
    }

    //--------------------------- events

    private var _events = MutableLiveData<List<NotifyItem>>()
    val events : LiveData<List<NotifyItem>>
        get() = _events

    fun updEvents (s: String){
        var arr : List<NotifyItem>? = null
        arr = ParseEvents.ParseEvents(s)
        _events.postValue(arr)
    }

    //================================== state

    fun getListState(s: String) : ArrayList<Buf>? {
        val str1 : ByteArray = s.toByteArray()
        val len_str1 = str1.size
        val src = ByteArray(len_str1)

        if (!Str.checkHttpOk(str1, len_str1)) {

            return null                 // to do
        }
        var len_content = Str.findContent(src, str1, len_str1)        // to do
        val list : ArrayList<Buf> = Str.mes_substr(src, len_content)  // to do
        return list
    }

    fun UpdState(s: String){

        val list = getListState(s)
        if(list == null) {
            return             // to do
        }

        var obj = _curObj.value ?: return
        Logm.aa("start update")
        val idObj = obj.objId
        uiScope.launch {
            withContext(Dispatchers.IO) {

                val objUpd = ObjStringUpd(obj)
                obj = objUpd.UpdStringAll(list)

                database.update(obj)

                val objState = ObjState(obj)
                val sList = objState.getObjStatList()

                val outState = OutState(obj)
                val oList = outState.getOutStatList()

                _outList.postValue(oList)
                _statList.postValue(sList)
                //_curObj.postValue(obj)
                Logm.aa("state updated")
            }
        }
    }

    fun updObjStat (s: String) {
        Logm.aa("http end:")
        Logm.aa(s)
        _strHttpStat.postValue(s)
    }
}