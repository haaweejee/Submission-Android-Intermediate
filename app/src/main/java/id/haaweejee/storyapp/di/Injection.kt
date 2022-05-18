package id.haaweejee.storyapp.di

import android.content.Context
import id.haaweejee.storyapp.service.StoryRepository
import id.haaweejee.storyapp.service.api.ApiConfig

object Injection {
    fun provideRepository(context: Context) : StoryRepository{
        val apiService = ApiConfig.apiInstance
        return StoryRepository(apiService)
    }
}