package com.example.tradeu.ui.welcome

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.tradeu.MyApplication
import com.example.tradeu.R
import com.example.tradeu.databinding.ActivityWelcomeBinding
import com.example.tradeu.ui.ViewModelFactory
import com.example.tradeu.ui.login.LoginActivity
import com.example.tradeu.ui.mainpage.MainPageActivity

class WelcomeActivity : AppCompatActivity() {
    private val Context.dataStore:DataStore<Preferences> by preferencesDataStore("userAuth")
    private lateinit var binding: ActivityWelcomeBinding

    companion object{
        const val LOGIN_SUCCESS = 100
    }

    private val intentCheckAction = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if (result.resultCode == LOGIN_SUCCESS){
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intentCheckAction.launch(intent)
        }
    }


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
 /*   private fun checkLoginStatus(isLoggedIn:Boolean){
        if (isLoggedIn){
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            binding.apply {
                btnLogin.isEnabled = true
                btnSignup.isEnabled = true
            }
        }
    }*/
}