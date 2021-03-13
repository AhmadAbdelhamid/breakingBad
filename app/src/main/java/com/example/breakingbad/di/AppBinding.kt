package com.example.breakingbad.di


import com.example.remote.RemoteDataSource
import com.example.remote.SafeNetworkRequestCaller
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
abstract class NetworkRequestModule {
    @Singleton
    @Binds
    abstract fun bindSafeNetworkRequest(
            remoteDataSource: RemoteDataSource
    ): SafeNetworkRequestCaller
}
