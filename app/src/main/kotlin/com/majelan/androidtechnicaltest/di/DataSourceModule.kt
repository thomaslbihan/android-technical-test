package com.majelan.androidtechnicaltest.di

import com.majelan.androidtechnicaltest.data.catalog.datasource.CatalogDataSource
import com.majelan.androidtechnicaltest.data.catalog.datasource.LocalCatalogDataSource
import com.majelan.androidtechnicaltest.data.catalog.datasource.LocalCatalogDataSourceImpl
import com.majelan.androidtechnicaltest.data.catalog.datasource.RemoteCatalogDataSourceImpl
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