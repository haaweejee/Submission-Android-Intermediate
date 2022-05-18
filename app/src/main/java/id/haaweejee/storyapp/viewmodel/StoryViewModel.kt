package id.haaweejee.storyapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import id.haaweejee.storyapp.di.Injection
import id.haaweejee.storyapp.service.StoryRepository
import id.haaweejee.storyapp.service.api.ApiConfig
import id.haaweejee.storyapp.service.data.addstory.AddStoryResponse
import id.haaweejee.storyapp.service.data.liststory.StoryResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel : ViewModel() {

    private val _addStory = MutableLiveData<AddStoryResponse>()
    val addStory : LiveData<AddStoryResponse> = _addStory

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