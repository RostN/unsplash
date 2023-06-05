package com.example.attestation.feed

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import coil.network.HttpException
import com.example.attestation.AppDatabase
import com.example.attestation.FeedPhoto
import com.example.attestation.RemoteKeys
import com.example.attestation.Repository
import okio.IOException

@OptIn(ExperimentalPagingApi::class)
class FeedRemoteMediator(
    private val feedDatabase: AppDatabase,
) : RemoteMediator<Int, FeedPhoto>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FeedPhoto>
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val apiResponse = Repository().getPhoto(page = page)
            val endOfPaginationReached = apiResponse.isEmpty()
            feedDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    feedDatabase.getRemoteKeysDao().clearRemoteKeys()
                    feedDatabase.getFeedPhotoDao().delete()
                }
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys = apiResponse.map {
                    RemoteKeys(
                        photoId = it.id,
                        prevKey = prevKey,
                        currentPage = page,
                        nextKey = nextKey
                    )
                }
                feedDatabase.getRemoteKeysDao().insertAll(remoteKeys)
                feedDatabase.getFeedPhotoDao().insert(apiResponse
                    .map {
                    FeedPhoto(
                        id = it.id,
                        urlsSmall = it.urls.small,
                        likes = it.likes!!,
                        liked_by_user = it.liked_by_user!!,
                        nameAuthor = it.user.name,
                        usernameAuthor = it.user.username,
                        userAvatar = it.user.profile_image.small
                    )
                })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (error: IOException) {
            customToast("Ошибка 1:\n $error")
            return MediatorResult.Error(error)
        } catch (error: HttpException) {
            customToast("Ошибка 2:\n $error")
            return MediatorResult.Error(error)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, FeedPhoto>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                feedDatabase.getRemoteKeysDao().getRemoteKeyByPhotoID(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, FeedPhoto>): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { feedPhoto ->
            feedDatabase.getRemoteKeysDao().getRemoteKeyByPhotoID(feedPhoto.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, FeedPhoto>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { feedPhoto ->
            feedDatabase.getRemoteKeysDao().getRemoteKeyByPhotoID(feedPhoto.id)
        }
    }
}