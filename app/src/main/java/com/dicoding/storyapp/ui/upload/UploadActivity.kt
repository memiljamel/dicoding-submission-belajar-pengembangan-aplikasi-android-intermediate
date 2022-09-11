package com.dicoding.storyapp.ui.upload

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.databinding.ActivityUploadBinding
import com.dicoding.storyapp.ui.camera.CameraActivity
import com.dicoding.storyapp.ui.home.HomeActivity
import com.dicoding.storyapp.utils.reduceFileImage
import com.dicoding.storyapp.utils.rotateBitmap
import com.dicoding.storyapp.utils.uriToFile
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class UploadActivity : AppCompatActivity(), View.OnClickListener {

    private var _activityUploadBinding: ActivityUploadBinding? = null
    private val binding get() = _activityUploadBinding!!

    private val uploadViewModel by viewModels<UploadViewModel>()

    private var getFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.error_permission_message),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityUploadBinding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding.btnCamera.setOnClickListener(this)
        binding.btnGallery.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_camera -> startCameraX()
            R.id.btn_gallery -> startGallery()
            R.id.btn_submit -> handleSubmit()
        }
    }

    private fun startCameraX() {
        val cameraActivity = Intent(this@UploadActivity, CameraActivity::class.java)
        launcherIntentCameraX.launch(cameraActivity)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result = rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera)

            getFile = myFile

            Glide.with(this)
                .load(result)
                .centerCrop()
                .fitCenter()
                .placeholder(R.drawable.ic_custom_loading)
                .error(R.drawable.ic_custom_error)
                .into(binding.ivPreview)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@UploadActivity)

            getFile = myFile

            Glide.with(this)
                .load(selectedImg)
                .centerCrop()
                .fitCenter()
                .placeholder(R.drawable.ic_custom_loading)
                .error(R.drawable.ic_custom_error)
                .into(binding.ivPreview)
        }
    }

    private fun handleSubmit() {
        val description = binding.edtDescription.editText?.text.toString().trim()

        if (validateAllFields(description)) {
            val file = reduceFileImage(getFile as File)
            val descriptionBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            lifecycleScope.launch {
                uploadViewModel.getBearerToken().collect { token ->
                    uploadViewModel.addNewStory(token, descriptionBody, imageMultipart)
                        .collect { result ->
                            when (result) {
                                is Result.Loading -> {
                                    binding.progressIndicator.visibility = View.VISIBLE
                                    binding.btnSubmit.isEnabled = false
                                }
                                is Result.Success -> {
                                    binding.progressIndicator.visibility = View.GONE
                                    binding.btnSubmit.isEnabled = true

                                    Toast.makeText(
                                        this@UploadActivity,
                                        resources.getString(R.string.successful_upload_message),
                                        Toast.LENGTH_LONG
                                    ).show()

                                    val homeActivity =
                                        Intent(this@UploadActivity, HomeActivity::class.java)
                                    startActivity(homeActivity)
                                    finish()
                                }
                                is Result.Error -> {
                                    binding.progressIndicator.visibility = View.GONE
                                    binding.btnSubmit.isEnabled = true

                                    Snackbar.make(binding.root, result.error, Snackbar.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                }
            }
        }
    }

    private fun validateAllFields(description: String): Boolean {
        if (getFile == null) {
            Snackbar.make(
                binding.root,
                resources.getString(R.string.error_required_photo_message),
                Snackbar.LENGTH_SHORT
            ).show()
            return false
        }

        if (description.isEmpty()) {
            binding.edtDescription.error =
                resources.getString(R.string.error_required_description_message)
            return false
        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityUploadBinding = null
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}