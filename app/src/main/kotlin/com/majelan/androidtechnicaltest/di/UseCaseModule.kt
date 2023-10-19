package com.majelan.androidtechnicaltest.di

import com.majelan.androidtechnicaltest.domain.catalog.usecase.GetAlbumsByArtistNameUseCase
import com.majelan.androidtechnicaltest.domain.catalog.usecase.GetAlbumsByArtistNameUseCaseImpl
import com.majelan.androidtechnicaltest.domain.catalog.usecase.GetArtistsUseCase
import com.majelan.androidtechnicaltest.domain.catalog.usecase.GetArtistsUseCaseImpl
import com.majelan.androidtechnicaltest.domain.catalog.usecase.GetMediaUseCase
import com.majelan.androidtechnicaltest.domain.catalog.usecase.GetMediaUseCaseImpl
import com.majelan.androidtechnicaltest.domain.catalog.usecase.GetMediasByArtistUseCase
import com.majelan.androidtechnicaltest.domain.catalog.usecase.GetMediasByArtistUseCaseImpl
import com.majelan.androidtechnicaltest.domain.catalog.usecase.GetMediasByGenreUseCase
import com.majelan.androidtechnicaltest.domain.catalog.usecase.GetMediasByGenreUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface UseCaseModule {

   @Binds
   fun providesGetArtistsUseCase(useCase: GetArtistsUseCaseImpl): GetArtistsUseCase

   @Binds
   fun providesGetAlbumsByArtistUseCase(useCase: GetAlbumsByArtistNameUseCaseImpl): GetAlbumsByArtistNameUseCase

   @Binds
   fun providesGetMediaUseCase(useCase: GetMediaUseCaseImpl): GetMediaUseCase

   @Binds
   fun providesGetMediasByArtistUseCase(useCase: GetMediasByArtistUseCaseImpl): GetMediasByArtistUseCase

   @Binds
   fun providesGetMediasByGenreUseCase(useCase: GetMediasByGenreUseCaseImpl): GetMediasByGenreUseCase
}