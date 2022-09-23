package com.hornet.hornetinbox.di

import com.hornet.hornetinbox.data.DataFetcher
import com.hornet.hornetinbox.data.DataFetcherImpl
import com.hornet.hornetinbox.domain.repository.InboxRepository
import com.hornet.hornetinbox.data.repository.InboxRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(inboxRepositoryImpl: InboxRepositoryImpl): InboxRepository

    @Binds
    abstract fun bindDataFetcher(dataFetcher: DataFetcherImpl): DataFetcher
}