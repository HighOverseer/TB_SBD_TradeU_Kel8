package com.example.tradeu.ui.welcome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private lateinit var welcomeViewModel:WelcomeViewModel

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
        welcomeViewModel = obtainViewModel()

        welcomeViewModel.isAlreadyLoggedIn.observe(this){isLoggedIn ->
            checkLoginStatus(isLoggedIn)
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intentCheckAction.launch(intent)
        }
    }

    private fun checkLoginStatus(isLoggedIn:Boolean){
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
    }

    private fun obtainViewModel():WelcomeViewModel{
        val app = application as MyApplication
        val factory = ViewModelFactory.getInstance(app, dataStore)
        return ViewModelProvider(this, factory)[WelcomeViewModel::class.java]
    }
}