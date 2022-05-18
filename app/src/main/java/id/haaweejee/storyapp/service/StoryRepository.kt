package id.haaweejee.storyapp.service

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import id.haaweejee.storyapp.service.api.ApiService
import id.haaweejee.storyapp.service.data.liststory.StoryResponse
import id.haaweejee.storyapp.service.data.liststory.StoryResults

class StoryRepository(private val apiService: ApiService) {
    fun getAllStory(bearer: String) : LiveData<PagingData<StoryResults>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, bearer)
            }
        ).liveData
    }
}