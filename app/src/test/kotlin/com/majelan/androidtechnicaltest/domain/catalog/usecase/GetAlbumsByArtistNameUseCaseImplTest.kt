package com.majelan.androidtechnicaltest.domain.catalog.usecase

import com.majelan.androidtechnicaltest.data.catalog.repositories.CatalogRepository
import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either.Failure
import com.majelan.androidtechnicaltest.data.core.either.Either.Successful
import com.majelan.androidtechnicaltest.domain.catalog.EntityFactory.getEmptyAlbumDomain
import com.majelan.androidtechnicaltest.domain.catalog.EntityFactory.getEmptyMedia
import com.majelan.androidtechnicaltest.domain.catalog.EntityFactory.getEmptyMediaDomain
import com.majelan.androidtechnicaltest.domain.catalog.mappers.AlbumDomainMapper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetAlbumsByArtistNameUseCaseImplTest {

   private val catalogRepository: CatalogRepository = mockk()
   private val albumDomainMapper: AlbumDomainMapper = mockk()

   private fun TestScope.getUseCase(): GetAlbumsByArtistNameUseCase {
      val testDispatcher = UnconfinedTestDispatcher(testScheduler)
      return GetAlbumsByArtistNameUseCaseImpl(
         catalogRepository = catalogRepository,
         albumDomainMapper = albumDomainMapper,
         defaultDispatcher = testDispatcher,
      )
   }

   @Test
   fun `repository returns empty list`() =  runTest {
      coEvery { catalogRepository.getMedias() } returns Successful(emptyList())

      val result = getUseCase().invoke("toto")

      assert(result is Successful && result.data.isEmpty())
   }

   @Test
   fun `repository returns failure`() =  runTest {
      coEvery { catalogRepository.getMedias() } returns Failure(AppException())

      val result = getUseCase().invoke("toto")

      assert(result is Failure)
   }

   @Test
   fun `repository returns list but artist not found`() =  runTest {
      coEvery { catalogRepository.getMedias() } returns Successful(
         listOf(
            getEmptyMedia().copy(id = "1", artist = "titi"),
            getEmptyMedia().copy(id = "2", artist = "tutu"),
            getEmptyMedia().copy(id = "3", artist = "tyty"),
            getEmptyMedia().copy(id = "4", artist = "tete"),
            getEmptyMedia().copy(id = "5", artist = "tata"),
         )
      )

      val result = getUseCase().invoke("toto")

      assert(result is Successful && result.data.isEmpty())
   }

   @Test
   fun `artist have 1 album`() =  runTest {
      val totoAlbum = listOf(
         getEmptyMedia().copy(id = "1", artist = "toto", album = "toto"),
         getEmptyMedia().copy(id = "4", artist = "toto", album = "toto"),
         getEmptyMedia().copy(id = "7", artist = "toto", album = "toto"),
      )
      val catalog = listOf(
         getEmptyMedia().copy(id = "2", artist = "tutu", album = "toto"),
         getEmptyMedia().copy(id = "3", artist = "tyty", album = "toto"),
         getEmptyMedia().copy(id = "5", artist = "tata", album = "toto"),
         getEmptyMedia().copy(id = "6", artist = "tata", album = "toto"),
      ).plus(totoAlbum)

      coEvery { catalogRepository.getMedias() } returns Successful(catalog)
      every {
         albumDomainMapper.map(totoAlbum)
      } returns getEmptyAlbumDomain()
         .copy(
            name = "toto",
            medias = totoAlbum.map {
               getEmptyMediaDomain().copy(id = it.id)
            }
         )

      val result = getUseCase().invoke("toto")

      assert(
         result is Successful
            && result.data.size == 1
            && result.data.first().name == "toto"
            && result.data.first().medias.map { it.id }.size == 3
            && result.data.first().medias.map { it.id }.containsAll(listOf("1", "4", "7"))
      )
   }

   @Test
   fun `artist have 2 albums`() =  runTest {
      val totoAlbum1 = listOf(
         getEmptyMedia().copy(id = "1", artist = "toto", album = "toto"),
         getEmptyMedia().copy(id = "4", artist = "toto", album = "toto"),
      )
      val totoAlbum2 = listOf(
         getEmptyMedia().copy(id = "7", artist = "toto", album = "tata"),
      )
      val catalog = listOf(
         getEmptyMedia().copy(id = "2", artist = "tutu", album = "toto"),
         getEmptyMedia().copy(id = "3", artist = "tyty", album = "toto"),
         getEmptyMedia().copy(id = "5", artist = "tata", album = "toto"),
         getEmptyMedia().copy(id = "6", artist = "tata", album = "toto"),
      )
         .plus(totoAlbum1)
         .plus(totoAlbum2)

      coEvery { catalogRepository.getMedias() } returns Successful(catalog)
      every {
         albumDomainMapper.map(totoAlbum1)
      } returns getEmptyAlbumDomain()
         .copy(
            name = "toto",
            medias = totoAlbum1.map {
               getEmptyMediaDomain().copy(id = it.id)
            }
         )
      every {
         albumDomainMapper.map(totoAlbum2)
      } returns getEmptyAlbumDomain()
         .copy(
            name = "tata",
            medias = totoAlbum2.map {
               getEmptyMediaDomain().copy(id = it.id)
            }
         )

      val result = getUseCase().invoke("toto")

      assert(
         result is Successful
            && result.data.size == 2
            && result.data.map { it.name }.containsAll(listOf("toto", "tata"))
            && result.data.first().medias.map { it.id }.size == 2
            && result.data.first().medias.map { it.id }.containsAll(listOf("1", "4"))
            && result.data[1].medias.map { it.id }.size == 1
            && result.data[1].medias.map { it.id }.contains("7")
      )
   }
}