package com.example.attestation

import android.app.DownloadManager
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.attestation.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

//Основные настройки: токен, первый запуск
const val mainConfiguration = "kotlinsharedpreference"
lateinit var sharedPreferences: SharedPreferences
lateinit var editor: SharedPreferences.Editor
var accessTokenApi = ""
var welcomeScreen = false

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        println("Присвоение токена")
        accessTokenApi = sharedPreferences.getString("TOKEN", "").toString()
    }

    //Загрузка фото
    fun startDownload(url: String, name: String) {
        try {
            val request = DownloadManager.Request(Uri.parse(url))
                .setTitle("${name}")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, System.currentTimeMillis().toString())
                .setMimeType("image/jpeg")
            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
        } catch (e:Exception){
            println("ERROR:${e.message}")
        }
    }
}