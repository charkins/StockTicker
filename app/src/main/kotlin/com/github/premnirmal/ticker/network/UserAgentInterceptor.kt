package com.github.premnirmal.ticker.network

import android.content.Context
import android.os.Build
import com.github.premnirmal.tickerwidget.BuildConfig
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import java.io.IOException

class UserAgentInterceptor(private val context: Context) : Interceptor {

  companion object {
    private const val USER_AGENT_FORMAT = "%s/%s(%s) (Android %s; %s %s)"
    private const val USER_AGENT_KEY = "UserAgent"
  }

  private val userAgent by lazy {
      val regex = Regex("[^A-Za-z0-9 ]") // strip non alpha-numeric characters
      String.format(
          USER_AGENT_FORMAT,
          context.packageName,
          BuildConfig.VERSION_NAME,
          BuildConfig.VERSION_CODE,
          Build.VERSION.SDK_INT,
          regex.replace(Build.MANUFACTURER, ""),
          regex.replace(Build.MODEL, "")
      )
  }

  @Throws(IOException::class) override fun intercept(chain: Chain): Response {
    val newRequest = chain.request()
        .newBuilder()
        .addHeader(USER_AGENT_KEY, userAgent)
        .build()
    return chain.proceed(newRequest)
  }

}