package com.appinsnap.aishrm.data.di

import android.app.Application
import android.content.Context
import com.google.android.datatransport.runtime.dagger.Provides
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Singleton
    @Provides
    fun contextProviders(application: Application): Context {
        return application
    }
}