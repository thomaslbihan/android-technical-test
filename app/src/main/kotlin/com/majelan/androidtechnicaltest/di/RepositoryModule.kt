package com.majelan.androidtechnicaltest.di

import com.majelan.androidtechnicaltest.data.catalog.repository.CatalogRepository
import com.majelan.androidtechnicaltest.data.catalog.repository.CatalogRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

   @Binds
   abstract fun bindsCatalogRepository(catalogRepositoryImpl: CatalogRepositoryImpl): CatalogRepository
}