package com.example.tradeu.ui.mainpage.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.tradeu.*
import com.example.tradeu.databinding.FragmentUploadBinding
import com.example.tradeu.ui.ViewModelFactory
import com.example.tradeu.ui.mainpage.viewmodels.UploadViewModel
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.sin

class UploadFragment : Fragment() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userAuth")
    private lateinit var uploadViewModel: UploadViewModel

    private var _binding:FragmentUploadBinding?=null
    private val binding get() = _binding!!

    private var currentPhotoPath:String?=null

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val file = currentPhotoPath?.let { path -> File(path) }
            uploadViewModel.setImageSelected(file?.toUri().toString())
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
                uploadViewModel.setImageSelected(selectedImg.toString())
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uploadViewModel = obtainViewModel()

        uploadViewModel.latestImageSelected.observe(viewLifecycleOwner){stringUri ->
            if (stringUri!=null){
                binding.ivItemPhoto.loadImage(requireContext(), stringUri)
            }else binding.ivItemPhoto.loadImage(requireContext(), R.drawable.logo_1)
        }

        uploadViewModel.message.observe(viewLifecycleOwner){singleEvent->
            singleEvent.getContentIfNotHandled()?.let {
                showToast(requireContext(), it)
            }
        }

        binding.btnTakePhoto.setOnClickListener {
            showDialogIntentPhoto()
        }

        binding.btnPublish.setOnClickListener {
            publishItem()
        }
    }

    private fun setFormBlank(){
        binding.apply {
            uploadViewModel.setImageSelected(null)
            etProductName.text?.clear()
            etDescription.text?.clear()
            etPrice.text?.clear()
            etStock.text?.clear()
        }


    }

    private fun publishItem() {
        binding.apply {
            lifecycleScope.launch {
                uploadViewModel.latestImageSelected.value?.let {
                    val imgPhotoUri = Uri.parse(it)
                    val imgSavedStringUri = saveUriToFile(imgPhotoUri, requireContext())
                    imgSavedStringUri?.let {
                        val productName = etProductName.text.toString()
                        val description = etDescription.text.toString()
                        val price = etPrice.text.toString()
                        val stock = etStock.text.toString()
                        if (productName.isNotEmpty() && description.isNotEmpty() && price.isNotEmpty() && stock.isNotEmpty()){
                            uploadViewModel.addItem(imgSavedStringUri, productName, description, price.toLong(), stock.toShort())
                            setFormBlank()
                            uploadViewModel.setSingleEventMessage(getString(R.string.publish_success))
                        }else{
                            uploadViewModel.setSingleEventMessage(getString(R.string.fill_the_form))
                        }

                    }
                }
            }

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

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_a_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun obtainViewModel():UploadViewModel{
        val app = requireActivity().application as MyApplication
        val factory = ViewModelFactory.getInstance(app, requireContext().dataStore)
        return ViewModelProvider(this, factory)[UploadViewModel::class.java]
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}