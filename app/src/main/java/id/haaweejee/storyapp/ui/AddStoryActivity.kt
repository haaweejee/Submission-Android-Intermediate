package id.haaweejee.storyapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import id.haaweejee.storyapp.R
import id.haaweejee.storyapp.databinding.ActivityAddStoryBinding
import id.haaweejee.storyapp.service.preferences.SettingsPreference
import id.haaweejee.storyapp.utils.PreferenceViewModelFactory
import id.haaweejee.storyapp.utils.reduceFileImage
import id.haaweejee.storyapp.utils.rotateBitmap
import id.haaweejee.storyapp.utils.uriToFile
import id.haaweejee.storyapp.viewmodel.PreferencesViewModel
import id.haaweejee.storyapp.viewmodel.StoryViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private var mLocation: Location? = null
    private var latLng: String? = null
    private var lonLng: String? = null
    private var getFile: File? = null


    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var prefViewModel: PreferencesViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (!allPermissionGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.not_got_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }


    private fun allPermissionGranted() = REQUIRED_PERMISSIONS_CAMERA.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.add_story)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS_CAMERA,
                REQUEST_CODE_CAMERA
            )
        }

        storyViewModel = ViewModelProvider(this)[StoryViewModel::class.java]
        val pref = SettingsPreference.getInstance(dataStore)
        prefViewModel =
            ViewModelProvider(this, PreferenceViewModelFactory(pref))[PreferencesViewModel::class.java]

        storyViewModel.addStory.observe(this) {
            if (it != null) {
                if (it.error == true) {
                    showLoading(false)
                    Snackbar.make(
                        binding.root,
                        getString(R.string.upload_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    showLoading(false)
                    intent = Intent(this, MainActivity::class.java)
                    Toast.makeText(this, getString(R.string.upload_success), Toast.LENGTH_SHORT)
                        .show()
                    startActivity(intent)
                    finish()
                }

            }
        }
        binding.btnCamera.setOnClickListener {
            startCameraX()
        }
        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnLocation.setOnClickListener {
            getLastLocation()
        }

        binding.btnUpload.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val description = binding.edtDescription.text.toString().trim()
            val lat = binding.edtLatitude.text.toString().trim()
            val long = binding.edtLongitude.text.toString().trim()

            when {
                description.isEmpty() -> binding.tlDescription.error = getString(R.string.description_not_yet_added)
                description.isNotEmpty() -> binding.tlDescription.error = null
            }

            when {
                lat.isEmpty() -> binding.tlLatitude.error = getString(R.string.please_add_location)
                lat.isNotEmpty() -> binding.tlLatitude.error = null
            }

            when{
                long.isEmpty() -> binding.tlLongitude.error = getString(R.string.longitude_not_yet_added)
                long.isNotEmpty() -> binding.tlLongitude.error = null
            }

            if (description.isNotEmpty() && lat.isNotEmpty() && long.isNotEmpty()){
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile,
                )
                val sendDescription = description.toRequestBody("text/plain".toMediaType())
                val sendLat = lat.toRequestBody("text/plain".toMediaType())
                val sendLong = long.toRequestBody("text/plain".toMediaType())
                prefViewModel.getBearerToken().observe(this) {
                    val bearer = "Bearer $it"
                    storyViewModel.addStory(bearer, sendDescription, imageMultiPart, sendLat, sendLong)
                    showLoading(true)
                }
            }

        } else {
            Snackbar.make(binding.root, getString(R.string.upload_failed_please_enter_photo), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)

            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )
            binding.previewImageView.setImageBitmap(result)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
        } else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                mLocation = location
                if (location != null) {
                    latLng = location.latitude.toString()
                    lonLng = location.longitude.toString()
                    binding.apply {
                        edtLatitude.setText(latLng)
                        edtLongitude.setText(lonLng)
                    }
                }
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            REQUIRED_PERMISSIONS_LOCATION,
            REQUEST_CODE_LOCATION
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS_CAMERA = arrayOf(Manifest.permission.CAMERA)
        private val REQUIRED_PERMISSIONS_LOCATION =
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        private const val REQUEST_CODE_CAMERA = 10
        private const val REQUEST_CODE_LOCATION = 11
    }
}