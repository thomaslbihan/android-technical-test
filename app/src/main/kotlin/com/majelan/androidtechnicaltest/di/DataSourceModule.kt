package com.majelan.androidtechnicaltest.di

import com.majelan.androidtechnicaltest.data.catalog.datasources.CatalogDataSource
import com.majelan.androidtechnicaltest.data.catalog.datasources.LocalCatalogDataSource
import com.majelan.androidtechnicaltest.data.catalog.datasources.LocalCatalogDataSourceImpl
import com.majelan.androidtechnicaltest.data.catalog.datasources.RemoteCatalogDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

   @Binds
   fun bindsRemoteCatalogDataSource(dataSource: RemoteCatalogDataSourceImpl): CatalogDataSource

   @Singleton
   @Binds
   fun bindsLocalCatalogDataSource(dataSource: LocalCatalogDataSourceImpl): LocalCatalogDataSource
}