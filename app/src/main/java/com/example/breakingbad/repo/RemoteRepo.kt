package com.example.breakingbad.repo

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.breakingbad.paging.CharacterPagingSource
import com.example.remote.RemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton
import com.example.model.Character


@Singleton
class RemoteRepo @Inject constructor(
    private val remote: RemoteDataSource
) {
//Maximum size must be at least pageSize + 2*prefetchDist, pageSize=20, prefetchDist=1, maxSize=20
    fun getAllCharacters(): LiveData<PagingData<Character>> = Pager(
        config = PagingConfig(
            pageSize = 10, //loadSize
            maxSize = 12, //max size for rv
            prefetchDistance = 1,
            initialLoadSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { CharacterPagingSource(remote) }
    ).liveData

}
