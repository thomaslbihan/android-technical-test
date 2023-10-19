package com.majelan.androidtechnicaltest.domain.catalog.mappers

import com.majelan.androidtechnicaltest.domain.catalog.EntityFactory.getEmptyMedia
import com.majelan.androidtechnicaltest.domain.catalog.EntityFactory.getEmptyMediaDomain
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class AlbumDomainMapperImplTest {
   private val mediaDomainMapper: MediaDomainMapper = mockk()

   @Test
   fun `map empty media list`() {
      val mapper = AlbumDomainMapperImpl(mediaDomainMapper)

      val result = mapper.map(emptyList())

      assert(result == null)
   }

   @Test
   fun `map 1 media`() {
      val mapper = AlbumDomainMapperImpl(mediaDomainMapper)

      val media = getEmptyMedia().copy(
         id = "1",
         title = "totoSong",
         album = "totoAlbum",
         artist = "toto",
         genre = "totoGenre",
         totalTrackCount = 1,
         duration = 1,
         image = "totoImage",
      )
      every { mediaDomainMapper.map(media) } returns getEmptyMediaDomain().copy(
         id = "1",
         picture = "totoImage",
         name = "totoSong",
         duration = 1,
         artist = "toto",
         genre = "totoGenre"
      )

      val result = mapper.map(listOf(media))

      assert(
         result != null
            && result.name == "totoAlbum"
            && result.picture == "totoImage"
            && result.totalTrackCount == 1
            && result.medias.size == 1
            && result.medias.first().id == "1"
      )
   }
}