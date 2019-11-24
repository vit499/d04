package ru.vit499.d04.ui.notifysms

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.vit499.d04.database.Mes
import ru.vit499.d04.database.MesDatabaseDao
import ru.vit499.d04.util.Filem

class MesViewModel(
    val database : MesDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope((Dispatchers.Main + viewModelJob))

    val messages = database.getAllMes1()

    var _listMesAll = MutableLiveData<List<Mes>>()
    val listMesAll : LiveData<List<Mes>>
        get() = _listMesAll

    var _listMes = MutableLiveData<List<Mes>>()
    val listMes : LiveData<List<Mes>>
        get() = _listMes

    var _currentObjName = MutableLiveData<String?>()
    val currentObjName : LiveData<String?>
        get() = _currentObjName

    init{
        val curObj = Filem.strCurrentObjName
        _currentObjName.value = curObj
        updMesObj(curObj)
    }

    fun updMesObj(numObj : String?){
        if(numObj == null) return
        uiScope.launch {
            withContext(Dispatchers.IO){
                val mes = database.getMesByName(numObj)
                if(mes != null) _listMes.postValue(mes)
            }
        }
    }
    fun delete(numObj:String?) {
        if(numObj == null) return
        uiScope.launch {
            withContext(Dispatchers.IO){
                database.deleteMesObj(numObj)
                _listMes.postValue(null)
            }
        }
    }

    fun onUpdMesObj(){
        updMesObj(_currentObjName.value)
    }
    fun getListMes() : List<Mes>? {
        //return(_listMes.value)
        onUpdMesObj()
        return _listMes.value
    }
    fun onDelete() {
        delete(_currentObjName.value)
    }
}


class MesViewModelFactory(
    private val dataSource: MesDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MesViewModel::class.java)) {
            Log.i("aa", " create factory ")
            return MesViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}