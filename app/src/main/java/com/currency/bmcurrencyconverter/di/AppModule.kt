package com.currency.bmcurrencyconverter.di

import android.content.Context
import android.content.SharedPreferences
import com.currency.bmcurrencyconverter.data.api.FixerApi
import com.currency.currencyconverter.dataControllers.repositories.CurrencyRepository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyRepository(api: FixerApi): CurrencyRepository {
        return CurrencyRepository(api)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("currency_preferences", Context.MODE_PRIVATE)
    }
}
