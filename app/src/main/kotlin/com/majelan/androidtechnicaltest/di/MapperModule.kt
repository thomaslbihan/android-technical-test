package com.majelan.androidtechnicaltest.di

import com.majelan.androidtechnicaltest.domain.catalog.mappers.AlbumDomainMapper
import com.majelan.androidtechnicaltest.domain.catalog.mappers.AlbumDomainMapperImpl
import com.majelan.androidtechnicaltest.domain.catalog.mappers.ArtistDomainMapper
import com.majelan.androidtechnicaltest.domain.catalog.mappers.ArtistDomainMapperImpl
import com.majelan.androidtechnicaltest.domain.catalog.mappers.MediaDomainMapper
import com.majelan.androidtechnicaltest.domain.catalog.mappers.MediaDomainMapperImpl
import com.majelan.androidtechnicaltest.presentation.artist_details.mappers.AlbumUIMapper
import com.majelan.androidtechnicaltest.presentation.artist_details.mappers.AlbumUIMapperImpl
import com.majelan.androidtechnicaltest.presentation.artist_details.mappers.MediaUIMapper
import com.majelan.androidtechnicaltest.presentation.artist_details.mappers.MediaUIMapperImpl
import com.majelan.androidtechnicaltest.presentation.artist_list.mappers.ArtistUIMapper
import com.majelan.androidtechnicaltest.presentation.artist_list.mappers.ArtistUIMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface MapperModule {

   @Binds
   fun bindsArtistsUIMapper(mapper: ArtistUIMapperImpl): ArtistUIMapper

   @Binds
   fun bindsArtistMapper(mapper: ArtistDomainMapperImpl): ArtistDomainMapper

   @Binds
   fun bindsAlbumDomainMapper(mapper: AlbumDomainMapperImpl): AlbumDomainMapper

   @Binds
   fun bindsMediaDomainMapper(mapper: MediaDomainMapperImpl): MediaDomainMapper

   @Binds
   fun bindsAlbumUIMapper(mapper: AlbumUIMapperImpl): AlbumUIMapper

   @Binds
   fun bindsMediaUIMapper(mapper: MediaUIMapperImpl): MediaUIMapper
}