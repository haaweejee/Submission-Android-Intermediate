package id.haaweejee.storyapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.haaweejee.storyapp.R
import id.haaweejee.storyapp.databinding.ActivityMainBinding
import id.haaweejee.storyapp.service.preferences.SettingsPreference
import id.haaweejee.storyapp.utils.PreferenceViewModelFactory
import id.haaweejee.storyapp.viewmodel.PreferencesViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefViewModel: PreferencesViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingsPreference.getInstance(dataStore)
        prefViewModel =
            ViewModelProvider(
                this,
                PreferenceViewModelFactory(pref)
            )[PreferencesViewModel::class.java]

        binding.apply {
            bottomNav.background = null
        }

        binding.btnAddStory.setOnClickListener {
            intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        setCurrentFragment(HomeFragment())
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(HomeFragment())
                R.id.mapList -> setCurrentFragment(MapsFragment())
                else -> setCurrentFragment(HomeFragment())
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            commit()
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.action_change_language -> {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            true
        }
        R.id.action_logout -> {
            prefViewModel.saveLoginState(false)
            prefViewModel.saveBearerToken("")
            intent = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}