package com.majelan.androidtechnicaltest.data.core.either

import com.majelan.androidtechnicaltest.data.core.AppException
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class EitherCallAdapter<T>(
   private val responseType: Type,
) : CallAdapter<T?, Call<Either<T?, AppException>>> {

   override fun responseType() = object : ParameterizedType {
      override fun getActualTypeArguments(): Array<Type> = arrayOf(responseType)

      override fun getRawType(): Type = responseType

      override fun getOwnerType(): Type? = null
   }

   override fun adapt(call: Call<T?>): Call<Either<T?, AppException>> {
      return EitherCall(call)
   }
}