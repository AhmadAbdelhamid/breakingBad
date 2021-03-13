package com.example.breakingbad.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.remote.data
import com.example.remote.isSuccessful
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import com.example.model.Character

private const val LIMIT_OF_CHARACTER_PER_PAGE = 10
class CharacterPagingSource(
        private val remoteDataSource: com.example.remote.RemoteDataSource,
) : PagingSource<Int, Character>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val offset = params.key ?: 0
            val limit = params.loadSize ?: LIMIT_OF_CHARACTER_PER_PAGE

            val allCharacters = remoteDataSource.getAllCharacters(limit, offset)
                .takeIf { it.isSuccessful }?.data
                ?: return LoadResult.Error(Exception("failed request"))

            LoadResult.Page(
                data = allCharacters,
                prevKey = if (offset == 0) null else offset.minus(10),
                nextKey = if (allCharacters.isNullOrEmpty()) null else offset.plus(10)
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}