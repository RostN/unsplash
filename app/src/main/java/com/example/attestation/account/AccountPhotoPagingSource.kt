package com.example.attestation.account

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.attestation.LoadPhotoResponse
import com.example.attestation.Repository

class AccountPhotoPagingSource(private val id: String) :
    PagingSource<Int, LoadPhotoResponse>() {
    private val firstPage: Int = 1
    private val repository = Repository()

    override fun getRefreshKey(state: PagingState<Int, LoadPhotoResponse>): Int =
        firstPage

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LoadPhotoResponse> {
        val page = params.key ?: firstPage
        return kotlin.runCatching {
            repository.getUsersPhoto(page=page, id=id)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            },
            onFailure = {
                LoadResult.Error(it)
            }
        )
    }
}