package ru.vit499.d04

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.vit499.d04.database.ObjDatabaseDao
import ru.vit499.d04.util.Logm
import java.lang.IllegalArgumentException

class MainViewModelFactory(
    private val dataSource: ObjDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            Log.i("aa", " create factory ")
            return MainViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}