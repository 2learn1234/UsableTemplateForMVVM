package com.example.template

import android.hardware.camera2.CameraManager.AvailabilityCallback
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

import com.example.template.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.template.data.webdata.json.CatalogJson
import com.example.template.data.webdata.json.EmbeddedEvents
import com.example.template.data.webdata.json.EventJson
import com.example.template.observeconnectivity.ConnectivityObserver
import com.example.template.observeconnectivity.NetworkConnectivityObserver
import com.example.template.ui.dialogs.showInfoDialog
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        checkInternet()
    }

    private fun checkInternet() {
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        connectivityObserver.observe().onEach { connectionStatus ->
            if (connectionStatus.equals(ConnectivityObserver.ConnectionStatus.Lost)) {
                showInfoDialog(
                    title = "No Connection",
                    message = "Connection unavailable- Local data used"
                )
            }
        }.launchIn(lifecycleScope)
    }
}