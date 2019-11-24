package ru.vit499.d04

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
import android.os.Build
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.IntentFilter
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import ru.vit499.d04.database.MesDatabase
import ru.vit499.d04.fcm.FbNotifyService
import ru.vit499.d04.ui.notifysms.MesViewModel
import ru.vit499.d04.ui.notifysms.MesViewModelFactory
import ru.vit499.d04.ui.outputs.OutViewModel
import ru.vit499.d04.ui.outputs.OutViewModelFactory
import ru.vit499.d04.util.Stp


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mainViewModel: MainViewModel
    private lateinit var outViewModel: OutViewModel
    private lateinit var mesViewModel: MesViewModel
    private lateinit var brRec: BroadcastReceiver
    private var mainFr: Boolean = false
    private var notifFr: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // Fabric.with(this, Crashlytics())

        Logm.aa("onCreate")
        var notifyObjName = ""
        var notifyText = ""
        val s1 = intent.getStringExtra("NOTICE")
        if(s1 != null && !s1.equals("")) {
            val s2 = intent.getStringExtra("OBJ")
            if(s2 != null && !s2.equals("")) {
                Logm.aa("notify $s1")
                intent.putExtra("NOTICE", "")
                intent.putExtra("OBJ", "")
                notifyText = s1;
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
            if (nd.id == R.id.mainFragment) mainFr = true
            else mainFr = false
            if(nd.id == R.id.notifyFragment) notifFr = true
            else notifFr = false
        }

        val application = requireNotNull(this).application
        val dataSource = ObjDatabase.getInstance(application).objDatabaseDao
        val viewModelFactory = MainViewModelFactory(dataSource, application)
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        val outViewModelFactory = OutViewModelFactory(application)
        outViewModel = ViewModelProviders.of(this, outViewModelFactory).get(OutViewModel::class.java)
        val dsMes = MesDatabase.getInstance(application).mesDatabaseDao
        mesViewModel = ViewModelProviders.of(this, MesViewModelFactory(dsMes, application)).get(MesViewModel::class.java)

        //mainViewModel = createViewModel(this)

        mainViewModel.navigateToNotify.observe(this, Observer {
            if(it){
                if(!notifFr) navController.navigate(R.id.notifyFragment)
                else mainViewModel.onReqEvent()
                mainViewModel.clrNavigateToNotify()
            }
        })
        mainViewModel.navigateToNotify2.observe(this, Observer {
            if(it){
                Logm.aa("to sms fragment")
                navController.navigate(R.id.notifyTextFragment)
                mainViewModel.clrNavigateToNotify2()
            }
        })

        Stp.en(true)
        addPm()

        if(!notifyObjName.equals("")) {
            Logm.aa("change cur obj: $notifyObjName")
            mainViewModel.onCurrentObjByName(notifyObjName, notifyText)
        }
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
        //registerReceiver(broadcastReceiver, IntentFilter("FB1"))
    }
    override fun onResume(){
        super.onResume()
        Logm.aa("onResume")
        checkNotify()
    }
    override fun onStop(){
        super.onStop()
        //unregisterReceiver(broadcastReceiver)
    }
    fun checkNotify(){
        var notifyObjName = ""
        val s1 = Stp.mes
        if(s1 != null && !s1.equals("")) {
            val s2 = Stp.numObj
            if(s2 != null && !s2.equals("")) {
                Logm.aa("stuped $s1")
                Stp.clrFbId()
                notifyObjName = s2
            }
        }
        if (!notifyObjName.equals("")) {
            Logm.aa("change cur obj: $notifyObjName")
            mainViewModel.onCurrentObjByName(notifyObjName, s1!!)
        }
    }

//    fun setForceAppStandby(
//        uid: Int, packageName: String,
//        mode: Int
//    ) {
//        val isPreOApp = isPreOApp(packageName)
//        if (isPreOApp) {
//            // Control whether app could run in the background if it is pre O app
//            mAppOpsManager.setMode(AppOpsManager.OP_RUN_IN_BACKGROUND, uid, packageName, mode)
//        }
//        // Control whether app could run jobs in the background
//        mAppOpsManager.setMode(AppOpsManager.OP_RUN_ANY_IN_BACKGROUND, uid, packageName, mode)
//    }

    fun addPm () {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val packageName = this.getPackageName()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val isBackground = activityManager.isBackgroundRestricted()
            Logm.aa("is backgr= $isBackground")
           // if(!isBackground){
                if(!Stp.getPm()) {
                    Stp.setPm(true)
                    val intent = Intent()
                    intent.setAction(Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS)
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
                    intent.setData(Uri.parse("package:" + packageName))
                    startActivity(intent)
                }
           // }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //val packageName = this.getPackageName()
            val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                val intent = Intent()
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
                intent.setData(Uri.parse("package:" + packageName))
                startActivity(intent)

//                intent.setAction(Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS)
//                intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
//                intent.setData(Uri.parse("package:" + packageName))
//                startActivity(intent)
            }
        }
    }

//    val broadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(contxt: Context?, intent: Intent?) {
//            val s = intent?.getStringExtra("OBJ") ?: "-"
//            Logm.aa("rec: $s")
//            //checkNotify()
//        }
//    }

    override fun onBackPressed() {
        if(mainFr) {
            val alert = AlertDialog.Builder(this)
            with(alert) {
                //setTitle(R.string.text_shutdown)
                //setMessage(R.string.are_you_sure)
                setMessage(R.string.text_shutdown)
                setPositiveButton(" Да ") { dialog, whichButton ->
                    super.onBackPressed()
                }
                setNegativeButton("Отмена") { dialog, whichButton -> }
                create()
                show()
            }
            return
        }
        super.onBackPressed()
    }
    companion object{
        fun createViewModel(activity: AppCompatActivity) : MainViewModel{
            val application = requireNotNull(activity).application
            val dataSource = ObjDatabase.getInstance(application).objDatabaseDao
            val viewModelFactory = MainViewModelFactory(dataSource, application)
            var mainViewModel = ViewModelProviders.of(activity, viewModelFactory).get(MainViewModel::class.java)
            return mainViewModel
        }
    }
}
