package com.example.tradeu.ui.mainpage.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.tradeu.MyApplication
import com.example.tradeu.R
import com.example.tradeu.databinding.FragmentProfileBinding
import com.example.tradeu.loadImage
import com.example.tradeu.showToast
import com.example.tradeu.ui.ViewModelFactory
import com.example.tradeu.ui.login.LoginActivity
import com.example.tradeu.ui.mainpage.MainPageActivity
import com.example.tradeu.ui.mainpage.viewmodels.ProfileViewModel
import com.example.tradeu.ui.welcome.WelcomeActivity


class ProfileFragment : Fragment() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userAuth")
    private var _binding:FragmentProfileBinding?=null
    private val binding get()= _binding!!

    private lateinit var profileViewModel:ProfileViewModel

    companion object{
        const val CHANGE_ACCOUNT_SUCCESS = 100
    }

    private val intentChangeAccount = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if (result.resultCode == CHANGE_ACCOUNT_SUCCESS){
            val intent = Intent(requireActivity(), MainPageActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = obtainViewModel(requireContext().dataStore)

        profileViewModel.userData.observe(viewLifecycleOwner){user ->
            binding.civProfilePhoto.loadImage(requireContext(), user.profilePhoto)
            binding.tvName.text = user.name
        }

        profileViewModel.isLogoutSuccess.observe(viewLifecycleOwner){singleEvent ->
            singleEvent.getContentIfNotHandled()?.let {isSuccess ->
                checkResult(isSuccess)
            }
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }

        binding.btnChangeAccount.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intentChangeAccount.launch(intent)
        }
    }


    private fun logout(){
        binding.apply {
            btnLogout.isEnabled = false
            btnChangeAccount.isEnabled = false
            progressBar.isVisible = true
            profileViewModel.logout()
        }
    }

    private fun checkResult(isSuccess:Boolean){
        binding.apply {
            if (isSuccess){
                val intent = Intent(requireActivity(), WelcomeActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }else{
                progressBar.isVisible = false
                btnLogout.isEnabled = true
                btnChangeAccount.isEnabled = true
                showToast(requireContext(), getString(R.string.sorry_theres_some_mistake))
            }
        }
    }

    private fun obtainViewModel(dataStore: DataStore<Preferences>):ProfileViewModel{
        val app = requireActivity().application as MyApplication
        val factory = ViewModelFactory.getInstance(app, dataStore)
        return ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}