package com.example.tradeu.ui.mainpage.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.tradeu.*
import com.example.tradeu.databinding.FragmentProfileBinding
import com.example.tradeu.ui.ViewModelFactory
import com.example.tradeu.ui.login.LoginActivity
import com.example.tradeu.ui.mainpage.MainPageActivity
import com.example.tradeu.ui.mainpage.viewmodels.ProfileViewModel
import com.example.tradeu.ui.welcome.WelcomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException


class ProfileFragment : Fragment() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userAuth")
    private var _binding:FragmentProfileBinding?=null
    private val binding get()= _binding!!

    private lateinit var profileViewModel:ProfileViewModel

    private var currentPhotoPath:String?=null

    companion object{
        const val CHANGE_ACCOUNT_SUCCESS = 100

        private const val DELAY_MILLIS = 1000L
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val file = currentPhotoPath?.let { path -> File(path) }
            file?.toUri()?.let { imgUri -> updateProfilePhoto(imgUri)}
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val nSelectedImg: Uri? = it.data?.data
            val selectedImg: Uri
            if (nSelectedImg != null) {
                selectedImg = nSelectedImg
                updateProfilePhoto(selectedImg)
            }

        }
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

        binding.civProfilePhoto.setOnClickListener {
            showDialogIntentPhoto()
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
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    showToast(requireContext(), getString(R.string.logout_success))
                    val intent = Intent(requireActivity(), WelcomeActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }, DELAY_MILLIS)

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

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)

        createCustomTempFile(requireActivity().application, ).also {
            val photoUri: Uri = FileProvider.getUriForFile(
                requireContext(),
                AUTHORITY,
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)

        }
    }

    private fun showDialogIntentPhoto(){
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.choose_method))
            .setItems(requireContext().resources.getStringArray(R.array.intent_photo)){ dialog, which ->
                if (which == 0){
                    openGallery()
                }else if(which == 1){
                    openCamera()
                }
                dialog.dismiss()
            }
            .create()
            .show()
    }
    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_a_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun updateProfilePhoto(newImgUri:Uri) {
        lifecycleScope.launch(Dispatchers.IO) {
            profileViewModel.userData.value?.let {userData ->
                val isFileDeleted:Boolean = if (userData.profilePhoto.isBlank()){
                    true
                }else deleteProfilePhoto(requireContext(), userData.profilePhoto)
                if (isFileDeleted){
                    val newImgUriString = saveUriToFile(newImgUri, requireContext())
                    newImgUriString?.let { profileViewModel.updateUserProfilePhoto(it) }
                }else showToast(requireContext(), getString(R.string.sorry_theres_some_mistake))
            }?:showToast(requireContext(), getString(R.string.sorry_theres_some_mistake))
        }

    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}