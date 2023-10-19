package com.majelan.androidtechnicaltest.data.core

import java.lang.Exception

class AppException(
   message: String = "",
   cause: Throwable? = null
): Exception(message, cause)