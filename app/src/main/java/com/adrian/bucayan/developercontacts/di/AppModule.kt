package com.adrian.bucayan.developercontacts.di

import android.util.Log
import com.adrian.bucayan.developercontacts.BuildConfig
import com.adrian.bucayan.developercontacts.data.remote.DeveloperContactsApi
import com.adrian.bucayan.developercontacts.data.repository.DeveloperContactsRepositoryImpl
import com.adrian.bucayan.developercontacts.domain.repository.DeveloperContactsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMyRecipeRepository(api: DeveloperContactsApi): DeveloperContactsRepository {
        return DeveloperContactsRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun providesTimberTree(): Timber.Tree {
        class ReportingTree : Timber.Tree() {
            override fun log(
                priority: Int,
                tag: String?,
                message: String,
                throwable: Throwable?
            ) {
                if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                    return
                }
                // val t = throwable ?: Exception(message)
                // Pass the exception variable t to crash reporting service
            }
        }

        return when(BuildConfig.DEBUG) {
            true -> Timber.DebugTree()
            else -> ReportingTree()
        }
    }

}