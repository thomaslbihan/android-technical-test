package com.majelan.androidtechnicaltest.di

import com.majelan.androidtechnicaltest.data.catalog.repositories.CatalogRepository
import com.majelan.androidtechnicaltest.data.catalog.repositories.CatalogRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

   @Binds
   abstract fun bindsCatalogRepository(catalogRepositoryImpl: CatalogRepositoryImpl): CatalogRepository
}