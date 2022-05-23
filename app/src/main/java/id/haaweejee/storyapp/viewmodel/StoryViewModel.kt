package id.haaweejee.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.haaweejee.storyapp.service.api.ApiConfig
import id.haaweejee.storyapp.service.data.addstory.AddStoryResponse
import id.haaweejee.storyapp.service.data.liststory.StoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel : ViewModel() {

    private val _addStory = MutableLiveData<AddStoryResponse>()
    val addStory : LiveData<AddStoryResponse> = _addStory

    private val _listStory = MutableLiveData<StoryResponse>()
    val listStory : LiveData<StoryResponse> = _listStory

    fun getListStory(bearer: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = ApiConfig.apiInstance.getListMapStories(bearer)
                if (client.isSuccessful){
                    _listStory.postValue(client.body())
                }else{
                    _listStory.postValue(StoryResponse(error = true, listStory = emptyList()))
                }
            }catch (ex: Exception){
                Log.d("Error", ex.toString())
            }
        }
    }

    fun addStory(bearer: String, description: RequestBody, photo: MultipartBody.Part, lat: RequestBody? = null, lon: RequestBody? = null){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = ApiConfig.apiInstance.postStories(bearer, photo, description, lat, lon)
                if (client.isSuccessful){
                    _addStory.postValue(client.body())
                }else{
                    _addStory.postValue(AddStoryResponse(error = true))
                }
            }catch (ex: Exception){
                Log.d("Error", ex.toString())
            }
        }
    }
}