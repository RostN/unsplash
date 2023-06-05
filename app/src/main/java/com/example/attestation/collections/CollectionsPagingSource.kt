package com.example.attestation.collections

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.attestation.LoadCollections
import com.example.attestation.Repository

class CollectionsPagingSource() :
    PagingSource<Int, LoadCollections>() {
    private val firstPage: Int = 1
    private val repository = Repository()

    override fun getRefreshKey(state: PagingState<Int, LoadCollections>): Int =
        firstPage

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LoadCollections> {
        val page = params.key ?: firstPage
        return kotlin.runCatching {
            repository.getCollections(page)
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