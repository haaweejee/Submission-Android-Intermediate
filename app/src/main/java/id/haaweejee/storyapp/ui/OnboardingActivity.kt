package id.haaweejee.storyapp.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.ramotion.paperonboarding.PaperOnboardingFragment
import com.ramotion.paperonboarding.PaperOnboardingPage
import id.haaweejee.storyapp.R
import id.haaweejee.storyapp.databinding.ActivityOnboardingBinding
import id.haaweejee.storyapp.service.preferences.SettingsPreference
import id.haaweejee.storyapp.utils.PreferenceViewModelFactory
import id.haaweejee.storyapp.viewmodel.PreferencesViewModel

class OnboardingActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding : ActivityOnboardingBinding
    private lateinit var fragmentManager : FragmentManager
    private lateinit var prefViewModel: PreferencesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val pref = SettingsPreference.getInstance(dataStore)
        prefViewModel = ViewModelProvider(this, PreferenceViewModelFactory(pref))[PreferencesViewModel::class.java]

        fragmentManager = supportFragmentManager

        val paperOnboardingFragment = PaperOnboardingFragment.newInstance(getDataforOnboarding())
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.frameLayout, paperOnboardingFragment)
        fragmentTransaction.commit()

        binding.btnLewati.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            prefViewModel.saveSplashState(true)
        }
    }

    private fun getDataforOnboarding() : ArrayList<PaperOnboardingPage>  {

        val source = PaperOnboardingPage(getString(R.string.story_title), getString(R.string.welcome_title), Color.parseColor("#bd5ef7"), R.drawable.friend, R.drawable.ic_circle)
        val source1 = PaperOnboardingPage(getString(R.string.add_story_title), getString(R.string.add_story_subtitle), Color.parseColor("#22eaaa"),R.drawable.add, R.drawable.ic_circle)
        val source2 = PaperOnboardingPage(getString(R.string.look_story_title), getString(R.string.look_story_subtitle), Color.parseColor("#ee5a5a"),R.drawable.map, R.drawable.ic_circle)

        val elements = arrayListOf<PaperOnboardingPage>()

        elements.add(source)
        elements.add(source1)
        elements.add(source2)
        return elements
    }
}
