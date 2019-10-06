package ru.vit499.d04

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.vit499.d04.database.ObjDatabaseDao

class MainViewModel(
    val database: ObjDatabaseDao,
    application: Application
) : AndroidViewModel(application) {


}