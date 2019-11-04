package ru.vit499.d04

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.crashlytics.android.Crashlytics
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.fabric.sdk.android.Fabric

import kotlinx.android.synthetic.main.activity_main.*
import ru.vit499.d04.database.ObjDatabase
import ru.vit499.d04.ui.notify.NotifyFragment
import ru.vit499.d04.util.Logm

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // Fabric.with(this, Crashlytics())

        Logm.aa("onCreate")
        var notifyObjName = ""
        val s1 = intent.getStringExtra("NOTICE")
        if(s1 != null && !s1.equals("")) {
            val s2 = intent.getStringExtra("OBJ")
            if(s2 != null && !s2.equals("")) {
                Logm.aa("notify $s1")
                intent.putExtra("NOTICE", "")
                intent.putExtra("OBJ", "")
                notifyObjName = s2
            }

        }
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav.setupWithNavController(navController)

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.mainFragment || nd.id == R.id.outputsFragment
                || nd.id == R.id.notifyFragment || nd.id == R.id.infoFragment) {
                //toolbar.visibility = View.VISIBLE
                bottomNav.visibility = View.VISIBLE
            } else {
                //toolbar.visibility =  View.GONE
                bottomNav.visibility =  View.GONE
            }
        }

        val application = requireNotNull(this).application
        val dataSource = ObjDatabase.getInstance(application).objDatabaseDao
        val viewModelFactory = MainViewModelFactory(dataSource, application)
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        if(!notifyObjName.equals("")) {
            Logm.aa("change cur obj: $notifyObjName")
            mainViewModel.onCurrentObjByName(notifyObjName)
        }
        mainViewModel.navigateToNotify.observe(this, Observer {
            if(it){
                navController.navigate(R.id.notifyFragment)
                mainViewModel.clrNavigateToNotify()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
//        val navController = this.findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp()
        Log.i("aa", "up")
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()

    }

    override fun onStart(){
        super.onStart()
        Logm.aa("onStart")
        val s1 = intent.getStringExtra("NOTICE")
        if(s1 == null) Logm.aa("null")
        Logm.aa(s1)
        if(s1 != null && !s1.equals("")) {
            val s2 = intent.getStringExtra("OBJ")
            if(s2 != null && !s2.equals("")) {
                Logm.aa("start notify $s1")
                intent.putExtra("NOTICE", "")
                intent.putExtra("OBJ", "")
                //notifyObjName = s2
            }

        }
    }
    override fun onResume(){
        super.onResume()
        Logm.aa("onResume")
        val s1 = intent.getStringExtra("NOTICE")
        if(s1 == null) Logm.aa("null")
        Logm.aa(s1)
        if(s1 != null && !s1.equals("")) {
            val s2 = intent.getStringExtra("OBJ")
            if(s2 != null && !s2.equals("")) {
                Logm.aa("resume notify $s1")
                intent.putExtra("NOTICE", "")
                intent.putExtra("OBJ", "")
                //notifyObjName = s2
            }

        }
    }

}
