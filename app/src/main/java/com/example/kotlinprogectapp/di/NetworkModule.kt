package com.example.kotlinprogectapp.di

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.kotlinprogectapp.BuildConfig
import com.example.kotlinprogectapp.data.remote.api.ApiService
import com.example.kotlinprogectapp.data.remote.interceptor.AuthInterceptor
import com.google.gson.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDate
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, object : JsonDeserializer<LocalDate> {
            override fun deserialize(
                json: JsonElement, type: Type, ctx: JsonDeserializationContext
            ): LocalDate = LocalDate.parse(json.asString)
        })
        .registerTypeAdapter(LocalDate::class.java, object : JsonSerializer<LocalDate> {
            override fun serialize(
                src: LocalDate, type: Type, ctx: JsonSerializationContext
            ): JsonElement = JsonPrimitive(src.toString())
        })
        .create()

    @Provides
    @Singleton
    fun provideOkHttp(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): ApiService =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
}