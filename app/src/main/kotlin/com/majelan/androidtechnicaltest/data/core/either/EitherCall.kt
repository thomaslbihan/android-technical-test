package com.majelan.androidtechnicaltest.data.core.either

import com.majelan.androidtechnicaltest.data.core.AppException
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EitherCall<T>(private val delegate: Call<T?>) : Call<Either<T?, AppException>> {

   override fun enqueue(callback: Callback<Either<T?, AppException>>) {
      delegate.enqueue(object : Callback<T?> {
         override fun onResponse(call: Call<T?>, response: Response<T?>) {
            handleSuccess(callback, call, response)
         }

         override fun onFailure(call: Call<T?>, t: Throwable) {
            handleFailure(callback, t)
         }
      })
   }

   override fun isExecuted() = delegate.isExecuted

   override fun clone() = EitherCall(delegate = delegate.clone())

   override fun isCanceled() = delegate.isCanceled

   override fun cancel() = delegate.cancel()

   override fun execute(): Response<Either<T?, AppException>> {
      //because it will send a request Synchronously
      throw UnsupportedOperationException("EitherCall doesn't support execute")
   }

   override fun request(): Request = delegate.request()

   override fun timeout(): Timeout = delegate.timeout()

   /**
    * Handle the success a call and the status code of the response
    * @param callback The callback to complete
    * @param call The call that succeeded
    * @param response The response of the call
    */
   private fun <T : Any?> handleSuccess(
      callback: Callback<Either<T?, AppException>>,
      call: Call<T>,
      response: Response<T?>
   ) {
      val data = response.body()
      if (response.isSuccessful) {
         callback.onResponse(this as Call<Either<T?, AppException>>, Response.success(Either.Successful(data)))
      } else {
         val failure = Either.Failure(AppException(message = response.errorBody()?.string().orEmpty()))
         callback.onResponse(this as Call<Either<T?, AppException>>, Response.success(failure))
      }
   }

   /**
    * Handle the failure of a call
    * @param callback The callback to complete
    * @param call The call that succeeded
    * @param t The exception that happened
    */
   private fun <T : Any?> handleFailure(
      callback: Callback<Either<T, AppException>>,
      t: Throwable
   ) {
      val error = AppException(message = t.localizedMessage ?: t.message.orEmpty(), cause = t)
      callback.onResponse(this as Call<Either<T, AppException>>, Response.success(Either.Failure(error)))
   }
}