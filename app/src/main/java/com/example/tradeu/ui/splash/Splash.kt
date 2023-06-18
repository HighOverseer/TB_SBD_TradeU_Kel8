package com.example.tradeu.ui.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.tradeu.MyApplication
import com.example.tradeu.databinding.ActivitySplashBinding
import com.example.tradeu.ui.ViewModelFactory
import com.example.tradeu.ui.mainpage.MainPageActivity
import com.example.tradeu.ui.welcome.WelcomeActivity

class Splash : AppCompatActivity() {
    private val Context.dataStore:DataStore<Preferences> by preferencesDataStore("userAuth")
    private lateinit var splashViewModel: SplashViewModel

    companion object{
        private const val DELAY_MILLIS = 2500L
    }

    private lateinit var binding:ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        splashViewModel = obtainViewModel()

        toWelcomeOrMain()
    }

    private fun obtainViewModel(): SplashViewModel {
        val app = application as MyApplication
        val factory = ViewModelFactory.getInstance(app, dataStore)
        return ViewModelProvider(this, factory)[SplashViewModel::class.java]
    }

    private fun toWelcomeOrMain(){
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (splashViewModel.userId != -1L){
                val intent = Intent(this, MainPageActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, DELAY_MILLIS)
    }

}