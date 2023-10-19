package com.majelan.androidtechnicaltest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DispatcherModule {

   @IODispatcher
   @Provides
   @Singleton
   fun providesIODispatcher() = Dispatchers.IO

   @DefaultDispatcher
   @Provides
   @Singleton
   fun providesDefaultDispatcher() = Dispatchers.Default
}