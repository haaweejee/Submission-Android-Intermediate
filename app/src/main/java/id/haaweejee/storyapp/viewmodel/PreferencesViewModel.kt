package id.haaweejee.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import id.haaweejee.storyapp.service.preferences.SettingsPreference
import kotlinx.coroutines.launch

class PreferencesViewModel(private val pref: SettingsPreference) : ViewModel() {
    fun getLoginState(): LiveData<Boolean>{
        return pref.getLoginState().asLiveData()
    }

    fun saveLoginState(loginState: Boolean){
        viewModelScope.launch {
            pref.saveLoginState(loginState)
        }
    }

    fun getBearerToken() : LiveData<String>{
        return pref.getBearerToken().asLiveData()
    }

    fun saveBearerToken(bearerKey: String){
        viewModelScope.launch {
            pref.saveBearerToken(bearerKey)
        }
    }

    fun getUsername () : LiveData <String>{
        return pref.getUserName().asLiveData()
    }

    fun saveUsername (username : String){
        viewModelScope.launch {
            pref.saveUserName(username)
        }
    }

    fun getSplashState() : LiveData <Boolean>{
        return pref.splashScreenShow().asLiveData()
    }

    fun saveSplashState(isShow : Boolean){
        viewModelScope.launch {
            pref.saveSplashState(isShow)
        }
    }
}