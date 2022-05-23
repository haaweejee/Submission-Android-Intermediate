package id.haaweejee.storyapp.ui

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import id.haaweejee.storyapp.R
import id.haaweejee.storyapp.databinding.ActivityMapsBinding
import id.haaweejee.storyapp.service.data.liststory.StoryEntity
import id.haaweejee.storyapp.utils.DateFormatter.formatDateAndTime
import id.haaweejee.storyapp.utils.DateFormatter.formatDateAndTimeO
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val data = intent.getParcelableExtra<StoryEntity>(DATA)

        val photoLocation = LatLng(data?.lat!!.toDouble(), data.lon!!.toDouble())
        val photoCreate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatDateAndTimeO(data.createdAt.toString(), TimeZone.getDefault().id)
        } else {
            formatDateAndTime(data.createdAt.toString())
        }
        mMap.addMarker(
            MarkerOptions()
                .position(photoLocation)
                .title("Photo : ${data.name}")
                .snippet(
                    "Time Created : $photoCreate"
                )
        )

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(photoLocation, 15f))
        setMapStyle()

    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e("error", "style parsing failed")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("error", "Can't find style, Error: ", exception)
        }
    }

    companion object {
        const val DATA = "data"
    }
}