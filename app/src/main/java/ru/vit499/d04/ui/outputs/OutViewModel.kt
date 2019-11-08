package ru.vit499.d04.ui.outputs

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class OutViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    // если нет объектов, то из mainFragment переходим в добавление объекта
    private val _navigateToValOut = MutableLiveData<Boolean>()
    val navigateToValOut : LiveData<Boolean>
        get() = _navigateToValOut
    fun clrNavigationToValOut() {
        _navigateToValOut.value = false
    }

    private val _navigateBackFromValOut = MutableLiveData<Boolean>()
    val navigateBackFromValOut : LiveData<Boolean>
        get() = _navigateBackFromValOut
    fun clrNavigationBackFromValOut() {
        _navigateBackFromValOut.value = false
    }

    init{
        _navigateBackFromValOut.value = false
        _navigateToValOut.value = false
    }

    fun onSetValueOut(nOut: Int) {

        _navigateToValOut.value = true
    }

    fun onBackFromValOut(nOut: Int) {

        _navigateBackFromValOut.value = true
    }
}



class OutViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(OutViewModel::class.java)) {
            Log.i("aa", " create factory ")
            return OutViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}