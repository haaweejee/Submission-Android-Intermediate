package id.haaweejee.storyapp.service.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsPreference private constructor(private val dataStore: DataStore<Preferences>){

    private val LOGIN_STATE = booleanPreferencesKey("login")
    private val BEARER_KEY = stringPreferencesKey("bearer")
    private val USERNAME = stringPreferencesKey("name")
    private val SPLASHSCREENSHOW = booleanPreferencesKey("splash")

    fun getLoginState(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[LOGIN_STATE] ?: false
        }
    }

    fun getBearerToken() : Flow<String>{
        return dataStore.data.map { preferences ->
            preferences[BEARER_KEY] ?: ""
        }
    }

    fun getUserName () : Flow<String>{
        return dataStore.data.map { preferences ->
            preferences[USERNAME] ?: ""
        }
    }

    fun splashScreenShow () : Flow<Boolean>{
        return dataStore.data.map { preferences ->
            preferences[SPLASHSCREENSHOW] ?: false
        }
    }

    suspend fun saveLoginState(loginState: Boolean){
        dataStore.edit { preferences ->
            preferences[LOGIN_STATE] = loginState
        }
    }

    suspend fun saveBearerToken(bearerKey : String){
        dataStore.edit { prefences ->
            prefences[BEARER_KEY] = bearerKey
        }
    }

    suspend fun saveUserName(name : String){
        dataStore.edit { preferences ->
            preferences[USERNAME] = name
        }
    }

    suspend fun saveSplashState(isShow : Boolean){
        dataStore.edit { preferences ->
            preferences[SPLASHSCREENSHOW] = isShow
        }
    }
    companion object{
        @Volatile
        private var INSTANCE : SettingsPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>) : SettingsPreference{
            return INSTANCE ?: synchronized(this){
                val instance = SettingsPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}