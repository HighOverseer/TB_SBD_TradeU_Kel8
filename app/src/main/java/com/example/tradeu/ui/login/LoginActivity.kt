package com.example.tradeu.ui.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.tradeu.MyApplication
import com.example.tradeu.R
import com.example.tradeu.databinding.ActivityLoginBinding
import com.example.tradeu.showSnackbar
import com.example.tradeu.ui.ViewModelFactory
import com.example.tradeu.ui.mainpage.fragment.ProfileFragment
import com.example.tradeu.ui.welcome.WelcomeActivity

class LoginActivity : AppCompatActivity() {
    private val Context.dataStore:DataStore<Preferences> by preferencesDataStore("userAuth")

    private lateinit var binding:ActivityLoginBinding

    private val loginViewModel:LoginViewModel by viewModels{
        ViewModelFactory.getInstance((application as MyApplication), dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel.isLoginSuccess.observe(this){isSuccess ->
            checkResult(isSuccess)
        }

        binding.btnLogin.setOnClickListener {
            login()
        }

        loginViewModel.getListUsers().observe(this){
            it.forEach(::println)
        }

    }

    private fun login(){
        binding.apply {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()){
                pbLogin.isVisible = true
                btnLogin.isEnabled = false
                loginViewModel.login(username, password)
            }else showSnackbar(root, getString(R.string.username_password_empty))
        }
    }

    private fun checkResult(isSuccess:Boolean){
        closeKeyboard()
        binding.pbLogin.isVisible = false
        if (isSuccess){
            setResult(WelcomeActivity.LOGIN_SUCCESS or ProfileFragment.CHANGE_ACCOUNT_SUCCESS)
            finish()
        }else{
            binding.btnLogin.isEnabled = true
            showSnackbar(binding.root, getString(R.string.username_password_wrong))
        }
    }

    private fun closeKeyboard(){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}