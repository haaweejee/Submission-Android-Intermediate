package id.haaweejee.storyapp.ui

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import id.haaweejee.storyapp.R
import id.haaweejee.storyapp.databinding.FragmentProfileBinding
import id.haaweejee.storyapp.service.data.liststory.StoryResults
import id.haaweejee.storyapp.service.preferences.SettingsPreference
import id.haaweejee.storyapp.utils.CustomInfoAdapter
import id.haaweejee.storyapp.utils.PreferenceViewModelFactory
import id.haaweejee.storyapp.viewmodel.PreferencesViewModel
import id.haaweejee.storyapp.viewmodel.StoryViewModel


class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentProfileBinding
    private lateinit var prefViewModel: PreferencesViewModel
    private lateinit var storyViewModel: StoryViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingsPreference.getInstance(requireContext().dataStore)
        prefViewModel =
            ViewModelProvider(
                this,
                PreferenceViewModelFactory(pref)
            )[PreferencesViewModel::class.java]
        storyViewModel = ViewModelProvider(this)[StoryViewModel::class.java]
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        prefViewModel.getBearerToken().observe(this) {
            val bearer = "Bearer $it"
            Log.d("MainActivity", bearer)
            storyViewModel.getListStory(bearer)
            storyViewModel.listStory.observe(this) { data ->
                val dataList = ArrayList<StoryResults>()
                dataList.addAll(data.listStory)
                for (i in dataList.indices) {
                    val location = dataList[0].lon?.let { lon ->
                        dataList[0].lat?.let { lat ->
                            LatLng(
                                lat,
                                lon
                            )
                        }
                    }
                    val customInfoWindows = CustomInfoAdapter(requireContext())
                    mMap.setInfoWindowAdapter(customInfoWindows)
                    val marker = mMap.addMarker(
                        MarkerOptions()
                            .position(
                                LatLng(
                                    dataList[i].lat!!.toDouble(),
                                    dataList[i].lon!!.toDouble()
                                )
                            )
                    )
                    val info = StoryResults()
                    info.name = dataList[i].name
                    info.photoUrl = dataList[i].photoUrl
                    info.createdAt = dataList[i].createdAt
                    marker?.tag = info

                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            location!!, 5f
                        )
                    )
                }


                setMapStyle()
            }
        }

    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.map_style
                    )
                )
            if (!success) {
                Log.e("error", "style parsing failed")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("error", "Can't find style, Error: ", exception)
        }
    }
}