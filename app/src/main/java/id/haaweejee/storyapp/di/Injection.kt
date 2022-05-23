package id.haaweejee.storyapp.di

import android.content.Context
import id.haaweejee.storyapp.service.StoryRepository
import id.haaweejee.storyapp.service.api.ApiConfig
import id.haaweejee.storyapp.service.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context) : StoryRepository{
        val apiService = ApiConfig.apiInstance
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository(apiService, database)
    }
}