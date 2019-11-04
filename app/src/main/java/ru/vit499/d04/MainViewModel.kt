package ru.vit499.d04

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import kotlinx.coroutines.*
import ru.vit499.d04.database.Obj
import ru.vit499.d04.database.ObjDatabaseDao
import ru.vit499.d04.fcm.FcmToken
import ru.vit499.d04.http.HttpCor
import ru.vit499.d04.http.HttpWorker
import ru.vit499.d04.http.Link
import ru.vit499.d04.mq.*
import ru.vit499.d04.objstate.ObjStringUpd
import ru.vit499.d04.ui.misc.Account
import ru.vit499.d04.ui.notify.NotifyItem
import ru.vit499.d04.ui.notify.ParseEvents
import ru.vit499.d04.util.*
import ru.vit499.d04.util.Str
import ru.vit499.d04.objstate.ObjState
import ru.vit499.d04.objstate.OutState
import ru.vit499.d04.ui.main.StatusItem
import ru.vit499.d04.ui.misc.Settings
import ru.vit499.d04.ui.misc.Settings.Companion.initSettings
import ru.vit499.d04.ui.outputs.OutItem


class MainViewModel(
    val database: ObjDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope((Dispatchers.Main + viewModelJob))

    val httpJob = Job()
    val httpScope = CoroutineScope(Dispatchers.Main + httpJob)

    val mqttJob = Job()
    val mqttScope = CoroutineScope(Dispatchers.Main + mqttJob)

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
    private var mesId = (1..65535).random()
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
    // переход в события из уведомлений
    private val _navigateToNotify = MutableLiveData<Boolean>()
    val navigateToNotify : LiveData<Boolean>
        get() = _navigateToNotify
    fun clrNavigateToNotify() {
        _navigateToNotify.value = false
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

    private val _statList = MutableLiveData<List<StatusItem>>()
    val statList : LiveData<List<StatusItem>>
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
        _navigateToNotify.value = false
        curObjName = Filem.getCurrentObjName()
        objExist = !curObjName.equals("")
        val acc = Account.fill()
        accExist = acc
        initSettings()

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

        if(accExist && objExist){
            //getStateList()
            onReqStatus()
            onMqttStart()
        }
    }

    fun initLogFile (s: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {

            }
        }
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
                    getStateList(obj)

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
                    getStateList(obj)
                    mqttRestart()
                    _navigateBackFromObj.postValue(true)
                } ?: return@withContext
            }
        }
    }
    // выбор текущего объекта и переход на уведомления
    fun getObjByName (num: String) {
        uiScope.launch {
            withContext(Dispatchers.IO){
                val obj = database.getObjByName(num)
                obj?.let {
                    _curObj.postValue(obj)
                    curObjName = obj.objName
                    Filem.setCurrentObjName(curObjName)
                    Logm.aa("new current obj: ${curObjName}")
                    getStateList(obj)
                    mqttRestart()
                    _navigateToNotify.postValue(true)
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
                mqttRestart()
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
                        mqttRestart()
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
    fun onCurrentObjByName(numObj: String){
        if(!curObjName.equals(numObj)) {
            getObjByName(numObj)
        }
        else {
            _navigateToNotify.value = true
        }
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
        if(accExist && objExist){
            //onReqStatus()
            mqttRestart()
        }
    }

    //--------------------------

    private var _strHttpStat = MutableLiveData<String>()
    val strHttpStat : LiveData<String>
        get() = _strHttpStat

    private var _progress = MutableLiveData<Boolean>()
    val progress : LiveData<Boolean>
        get() = _progress

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        httpJob.cancel()
        mqttJob.cancel()
        _progress.value = false
        Stp.en(false)
        //httpR?.Close()
        //cancelWork()
        Logm.aa("shutdown")
    }

    //======================================================= http =========================


    //private var httpR: HttpCor? = null

    fun onReqStat () {
        if(_progress.value == true) return
        val link = Link.isLink(this.getApplication())
        Logm.aa("link: $link")
        if(!link) {

        }
        _progress.value = true
       // Logm.aa("on Rec Stat...")
        val strReq = strReqHttp(curObjName, "state")
        //Logm.aa(strReq)
        httpScope.launch {
            var httpR = HttpCor(10)
            val s = httpR?.reqStat(strReq, 1, 10) ?: "error"
            UpdState(s)
            _progress.postValue(false)
        }
    }
    fun onReqEvent () {
        if(_progress.value == true) return
        _progress.value = true
      //  Logm.aa("on Rec Ev...")
        val strReq = strReqHttp(curObjName, "events")
        //Logm.aa(strReq)
        httpScope.launch {
            //if(httpR == null) httpR = HttpCor()
            val httpR = HttpCor(10)
            val s = httpR?.reqStat(strReq, 2, 10) ?: "error"
            updEvents(s)
            _progress.postValue(false)
        }
    }

    fun onHttpClose(){
        Logm.aa("on http close")
        //httpR?.Close()
        httpJob.cancel()
        _progress.postValue(false)
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

        //_progress.value = true
        httpScope.launch {
            var arr = getArrObj() ?: return@launch
            //Logm.aa("arr: ${arr.toString()}")
            FcmToken.getFcmToken()
            val token = FcmToken.waitToken()
            val strReq = strSendToken(arr, token)
            Logm.aa("token=")
            Logm.aa(token)
            Logm.aa(strReq)
            //if(httpR == null) httpR = HttpCor()
            val http = HttpCor(10)
            val s = http?.reqStat(strReq, 3, 10) ?: "error"
            updHttpAnswer(s)
            //_progress.postValue(false)
        }
    }

    //--------------------------- events

    private var _events = MutableLiveData<List<NotifyItem>>()
    val events : LiveData<List<NotifyItem>>
        get() = _events

    fun updEvents (s: String){
        if(s.equals("error")) return
        var arr : List<NotifyItem>? = null
        arr = ParseEvents.ParseEvents(s)
        _events.postValue(arr)
    }

    //================================== state

    suspend fun UpdState(s: String){

        if(s.equals("error")) {
            return
        }
        val map = ObjStringUpd.getMapState(s)
        if(map == null) return

       // Logm.aa("start update")

        updObjFromList(map)
    }

    suspend fun getStateList(obj: Obj) {
        withContext(Dispatchers.IO){
            val objState = ObjState(obj)
            val sList = objState.getObjStatList()

            val outState = OutState(obj)
            val oList = outState.getOutStatList()

            _outList.postValue(oList)
            _statList.postValue(sList)
        }
    }
    suspend fun updObjFromList(map: Map<String, String>) {
        var obj = _curObj.value ?: return
        withContext(Dispatchers.IO){
            val objUpd = ObjStringUpd(obj)
            obj = objUpd.UpdStringAll(map)
            database.update(obj)
            getStateList(obj)
        }
    }

    fun updHttpAnswer (s: String) {
       // Logm.aa("http end:")
       // Logm.aa(s)
        _strHttpStat.postValue(s)
    }

    //------------------  send cmd -------------
    fun onPostCmd (key: String, value: String) {

        //_progress.value = true
        httpScope.launch {

            val strReq = strSendCmd(curObjName, key, value, (mesId++).toString())
           // Logm.aa(strReq)
            //if(httpR == null) httpR = HttpCor()
            val http = HttpCor(10)
            val s = http?.reqStat(strReq, 3, 10) ?: "error"
            updHttpAnswer(s)
            //_progress.postValue(false)
        }
    }

    fun onReqStatus() {
        onPostCmd("reqstatus", "")
    }

    //---------------------------
    //============================================= mqtt ============================

    var mqtt : MqttCor? = null

    fun onMqttStart () {
        if(!Settings.mqttEnable) return
        httpScope.launch {
            withContext(Dispatchers.IO) {
                if(mqtt == null) mqtt = MqttCor()
                val res = mqtt!!.subMqtt(curObjName, updCallback = { map -> updFromMqtt(map) } )
                Logm.aa("mqtt finished $res")
                //_progress.postValue(false)
            }
        }
    }
    fun mqttClose(){
        if(mqtt != null) mqtt!!.Close()
    }
    fun mqttRestart() {
        mqttClose()
        onMqttStart()
    }

    fun updFromMqtt (map: Map<String, String>) {
        Logm.aa("callback: ")
        //if(map == null) return
        for((key, value) in map){
           // Logm.aa("key=$key")
           // Logm.aa("value=$value")
        }
        if(_progress.value == true) return
        mqttScope.launch {
            updObjFromList(map)
        }
    }

    private val workManager : WorkManager = WorkManager.getInstance()

    fun onMqttStart2() {

        val inputData = Data.Builder()
            .putString("CURRENT_NAME", curObjName)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val mqttWorkRequest = OneTimeWorkRequestBuilder<MqttWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .addTag("mqtt")
            .build()
        WorkManager.getInstance().enqueue(mqttWorkRequest)

    }
    fun cancelWork() {
        WorkManager.getInstance().cancelAllWorkByTag("mqtt")
    }

    fun onMqttStart3() {
        //val mqtt = Mqtt()
        //mqtt.mqttStart(curObjName, updCallback = { s -> updFromMqtt(s) })
    }

    //================================================================

    fun onWork() {

        if(_progress.value == true) return
        _progress.value = true
        Logm.aa("on work Rec Stat...")
        val strReq = strReqHttp(curObjName, "state")

        httpScope.launch {
            val constraints = Constraints.Builder()
                //.setRequiredNetworkType(NetworkType.CONNECTED)
                //.setRequiresCharging(true)
                //.setRequiresBatteryNotLow(true)
                .build()

            val builder = Data.Builder()
            builder.putString("str", strReq)
            val param = builder.build()
           // Logm.aa("param: $param")
            val httpWorkRequest = OneTimeWorkRequestBuilder<HttpWorker>()
                .setInputData(param)
                .build()
            WorkManager.getInstance().enqueue(httpWorkRequest)

            UpdState("error")
            _progress.postValue(false)
        }
    }
}