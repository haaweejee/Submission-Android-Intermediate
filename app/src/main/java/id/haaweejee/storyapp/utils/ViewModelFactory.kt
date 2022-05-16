package id.haaweejee.storyapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.haaweejee.storyapp.service.preferences.SettingsPreference
import id.haaweejee.storyapp.viewmodel.PreferencesViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val pref: SettingsPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreferencesViewModel::class.java)){
            return PreferencesViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class: "+ modelClass.name)
    }
}