package com.example.tradeu.ui.register

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
import com.example.tradeu.R
import com.example.tradeu.databinding.ActivityRegisterBinding
import com.example.tradeu.showToast
import com.example.tradeu.ui.ViewModelFactory
import com.example.tradeu.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private val Context.dataStore:DataStore<Preferences> by preferencesDataStore("userAuth")
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    companion object{
        const val REGISTER_SUCCESS = 10
        private const val DELAY_MILLIS = 1500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerViewModel = obtainViewModel()

        registerViewModel.message.observe(this){singleEvent ->
            singleEvent.getContentIfNotHandled()?.let {
                showToast(this@RegisterActivity, it)
            }
        }

        registerViewModel.isRegisterSuccess.observe(this){isRegisterSuccess ->
            checkRegisterStatus(isRegisterSuccess)
        }

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun checkRegisterStatus(isRegisterSuccess: Boolean) {
        if (isRegisterSuccess){
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                setResult(REGISTER_SUCCESS)
                finish()
            }, DELAY_MILLIS)
        }else binding.btnRegister.isEnabled = true
    }

    private fun register(){
        binding.apply {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val fullname = etName.text.toString().trim()
            val address = etAddress.text.toString().trim()


            if (username.isNotEmpty() && password.isNotEmpty() && fullname.isNotEmpty() && address.isNotEmpty()){
                binding.btnRegister.isEnabled = false
                registerViewModel.register(username, password, fullname, address)
            }else showToast(this@RegisterActivity, getString(R.string.fill_the_form))
        }
    }

    private fun obtainViewModel(): RegisterViewModel {
        val factory = ViewModelFactory.getInstance(application as MyApplication, dataStore)
        return ViewModelProvider(this, factory)[RegisterViewModel::class.java]
    }
}