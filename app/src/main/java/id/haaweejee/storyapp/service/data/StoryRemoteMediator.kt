package id.haaweejee.storyapp.service.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import id.haaweejee.storyapp.service.api.ApiService
import id.haaweejee.storyapp.service.data.liststory.StoryEntity
import id.haaweejee.storyapp.service.data.liststory.StoryResults
import id.haaweejee.storyapp.service.database.StoryDatabase

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database : StoryDatabase,
    private val apiService : ApiService,
    private val bearer : String
) : RemoteMediator<Int, StoryEntity>() {

    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = when(loadType){
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return  MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getAllStories(page = page, size = state.config.pageSize, bearer = bearer)

            val endOfPaginationReached = responseData.listStory.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH){
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.storyDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.listStory.map {
                    RemoteKeys(id = it.id.toString(), prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.storyDao().insertStory(mapResultToEntity(responseData.listStory))
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }catch (exception : Exception){
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>) : RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>) : RemoteKeys?{
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>) : RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    private fun mapResultToEntity(story: List<StoryResults>) : List<StoryEntity>{
        val entity = ArrayList<StoryEntity>()
        story.map { storyResults ->
            val storyEntity = StoryEntity(
                id = storyResults.id!!,
                name = storyResults.name,
                description = storyResults.description,
                photoUrl = storyResults.photoUrl,
                createdAt = storyResults.createdAt,
                lat = storyResults.lat,
                lon = storyResults.lon
            )
            entity.add(storyEntity)
        }
        return entity
    }
}