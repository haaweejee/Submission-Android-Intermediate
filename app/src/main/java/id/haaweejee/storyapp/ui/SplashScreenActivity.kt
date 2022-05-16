package id.haaweejee.storyapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import id.haaweejee.storyapp.R
import id.haaweejee.storyapp.service.preferences.SettingsPreference
import id.haaweejee.storyapp.utils.ViewModelFactory
import id.haaweejee.storyapp.viewmodel.PreferencesViewModel


class SplashScreenActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var prefViewModel: PreferencesViewModel

    companion object{
        const val DELAY = 4000L
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        val pref = SettingsPreference.getInstance(dataStore)
        prefViewModel = ViewModelProvider(this, ViewModelFactory(pref))[PreferencesViewModel::class.java]

        Handler(Looper.getMainLooper()).postDelayed({
            prefViewModel.getLoginState().observe(this@SplashScreenActivity){ state ->
                if (state){
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            finish() }, DELAY)
    }
}