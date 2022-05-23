package id.haaweejee.storyapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import id.haaweejee.storyapp.di.Injection
import id.haaweejee.storyapp.service.StoryRepository
import id.haaweejee.storyapp.service.data.liststory.StoryEntity

class ListStoryViewModel constructor(private val storyRepository: StoryRepository) : ViewModel() {
    fun getListStory(bearer: String) : LiveData<PagingData<StoryEntity>> = storyRepository.getAllStory(bearer)
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListStoryViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ListStoryViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}