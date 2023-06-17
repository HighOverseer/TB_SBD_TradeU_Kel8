package com.example.tradeu.ui.mainpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tradeu.R
import com.example.tradeu.databinding.ActivityMainPageBinding

class MainPageActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainPageBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLayout()
    }

    private fun setLayout(){
        binding.bnvMenu.itemIconTintList = null
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bnvMenu.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.menu_home, R.id.menu_favorite, R.id.menu_publish, R.id.menu_profile)
        )
    }


}