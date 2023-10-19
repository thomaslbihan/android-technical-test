package com.majelan.androidtechnicaltest.di

import com.majelan.androidtechnicaltest.BuildConfig
import com.majelan.androidtechnicaltest.data.catalog.api.CatalogApi
import com.majelan.androidtechnicaltest.data.core.either.EitherCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Logger
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
   @Provides
   @Singleton
   fun providesOkHttpClient() = OkHttpClient.Builder()
      .run {
         if (BuildConfig.DEBUG) {
            addInterceptor(
               HttpLoggingInterceptor(Logger.DEFAULT).apply {
                  level = BODY
               }
            )
         } else this
      }
      .build()

   @Provides
   @Singleton
   fun providesRetrofit(
      okHttpClient: OkHttpClient
   ) = Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl("https://storage.googleapis.com/uamp/")
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(EitherCallAdapterFactory())
      .build()

   @Provides
   @Singleton
   fun providesCatalogApi(retrofit: Retrofit) = retrofit.create(CatalogApi::class.java)
}