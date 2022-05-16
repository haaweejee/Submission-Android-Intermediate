package id.haaweejee.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.haaweejee.storyapp.service.api.ApiConfig
import id.haaweejee.storyapp.service.data.addstory.AddStoryResponse
import id.haaweejee.storyapp.service.data.liststory.StoryResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel : ViewModel() {
    private val _listStory = MutableLiveData<List<StoryResults>>()
    val listStory : LiveData<List<StoryResults>> = _listStory

    private val _addStory = MutableLiveData<AddStoryResponse>()
    val addStory : LiveData<AddStoryResponse> = _addStory

    fun getAllStory(bearer: String){
        viewModelScope.launch(Dispatchers.IO) {
            val client = ApiConfig.apiInstance.getAllStories(bearer)
            if (client.isSuccessful){
                _listStory.postValue(client.body()?.listStory!!)
            }
        }
    }

    fun addStory(bearer: String, description: RequestBody, photo: MultipartBody.Part){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = ApiConfig.apiInstance.postStories(bearer, photo, description)
                if (client.isSuccessful){
                    _addStory.postValue(client.body())
                }
            }catch (ex: Exception){
                Log.d("Error", ex.toString())
            }
        }
    }
}