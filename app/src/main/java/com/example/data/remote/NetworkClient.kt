package com.example.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Network configuration and Retrofit service factory.
 */
object NetworkClient {

  /**
   * Base URL for EduHealth API backend.
   * - Local development emulator default: "http://10.0.2.2:8000/" (host localhost on Android emulator)
   * - Production default: configured to your backend domain.
   */
  const val DEFAULT_BASE_URL: String = "https://eduhealth-api.run.app/"

  @Volatile
  private var customBaseUrl: String = DEFAULT_BASE_URL

  private val loggingInterceptor: HttpLoggingInterceptor by lazy {
    HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.BODY
    }
  }

  private val headerInterceptor: Interceptor by lazy {
    Interceptor { chain ->
      val request = chain.request().newBuilder()
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/json")
        .addHeader("User-Agent", "EduHealth-Android/1.0")
        .build()
      chain.proceed(request)
    }
  }

  private val okHttpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
      .addInterceptor(headerInterceptor)
      .addInterceptor(loggingInterceptor)
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS)
      .retryOnConnectionFailure(true)
      .build()
  }

  private val moshi: Moshi by lazy {
    Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build()
  }

  private var retrofit: Retrofit? = null
  private var apiService: EduHealthApiService? = null

  /**
   * Dynamically allows updating the backend base URL (e.g. for testing with local server).
   */
  @Synchronized
  fun setBaseUrl(baseUrl: String) {
    var url = baseUrl
    if (!url.endsWith("/")) {
      url = "$url/"
    }
    customBaseUrl = url
    retrofit = null
    apiService = null
  }

  fun getBaseUrl(): String = customBaseUrl

  /**
   * Returns a singleton instance of the EduHealthApiService.
   */
  @Synchronized
  fun getApiService(): EduHealthApiService {
    return apiService ?: synchronized(this) {
      val rf = Retrofit.Builder()
        .baseUrl(customBaseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
      retrofit = rf
      val service = rf.create(EduHealthApiService::class.java)
      apiService = service
      service
    }
  }
}
