package com.majelan.androidtechnicaltest.data.core.either

sealed interface Either<out T, out E> {
   data class Successful<out T>(val data: T): Either<T, Nothing>
   data class Failure<out E>(val fail: E): Either<Nothing, E>

   suspend fun <R> mapSuccess(block: suspend (T) -> R): Either<R, E> {
      return when(this) {
         is Successful -> Successful(block(data))
         is Failure -> this
      }
   }

   fun onSuccess(block: (T) -> Unit) = apply {
      if (this is Successful) block(data)
   }
}