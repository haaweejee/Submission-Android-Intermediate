package id.haaweejee.storyapp.service

import androidx.lifecycle.LiveData
import androidx.paging.*
import id.haaweejee.storyapp.service.api.ApiService
import id.haaweejee.storyapp.service.data.StoryRemoteMediator
import id.haaweejee.storyapp.service.data.liststory.StoryEntity
import id.haaweejee.storyapp.service.database.StoryDatabase

class StoryRepository(private val apiService: ApiService, private val database: StoryDatabase) {
    fun getAllStory(bearer: String) : LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(database, apiService, bearer),
            pagingSourceFactory = {
//                StoryPagingSource(apiService, bearer)
                database.storyDao().getAllStory()
            }
        ).liveData
    }
}