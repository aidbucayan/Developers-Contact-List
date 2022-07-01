package com.adrian.bucayan.developercontacts.di

import android.content.Context
import com.adrian.bucayan.developercontacts.common.Constants
import com.adrian.bucayan.developercontacts.data.remote.DeveloperContactsApi
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesOkHttpClient(
        @ApplicationContext context: Context,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {

        val cacheSize = 10 * 1024 * 1024
        val cache = okhttp3.Cache(context.cacheDir, cacheSize.toLong())
        var retry: Boolean = true

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .cache(cache)
            .connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(retry)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create(getObjectMapper()))
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit.Builder): DeveloperContactsApi {
        return retrofit
            .build()
            .create(DeveloperContactsApi::class.java)
    }

    @Provides
    @Singleton
    fun getObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()

        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, false)
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(JodaModule())

        return objectMapper
    }

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = object: LoggingInterceptor() {}
        val loggingInterceptor = HttpLoggingInterceptor(logger)
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return loggingInterceptor
    }

    open class LoggingInterceptor: HttpLoggingInterceptor.Logger {

        override fun log(message: String) {
            Timber.tag("HTTP").d(message)
        }
    }

}
