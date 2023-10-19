package com.majelan.androidtechnicaltest.data.core.either

import android.util.Log
import com.majelan.androidtechnicaltest.data.core.AppException
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class EitherCallAdapterFactory : CallAdapter.Factory() {

   override fun get(
      returnType: Type,
      annotations: Array<Annotation>,
      retrofit: Retrofit
   ): CallAdapter<*, *>? {
      Log.d(TAG, "GET: returnType=$returnType, annotations=$annotations")

      if (getRawType(returnType) != Call::class.java) {
         Log.w(TAG, "return type is not Call")
         return null
      }
      check(returnType is ParameterizedType) { "Return type must be a parameterized type." }

      val responseType = getParameterUpperBound(0, returnType)
      if (getRawType(responseType) != Either::class.java) {
         Log.w(TAG, "return type is not Call<Either>")
         return null
      }
      check(responseType is ParameterizedType) { "Return type must be a parameterized type." }

      val errorType = getParameterUpperBound(1, responseType)
      if (getRawType(errorType) != AppException::class.java) {
         Log.w(TAG, "return type is not Call<Either<T, AppException>>")
         return null
      }

      val dataType = getParameterUpperBound(0, responseType)
      Log.d(TAG, "return type match: $returnType")
      return EitherCallAdapter<Any>(responseType = dataType)
   }

   companion object {
      private const val TAG = "EitherCallAdapterFactory"
   }
}