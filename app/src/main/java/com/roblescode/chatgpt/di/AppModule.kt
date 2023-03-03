package com.roblescode.chatgpt.di

import com.roblescode.chatgpt.data.OPEN_IA_KEY
import com.roblescode.chatgpt.data.api.OpenIAService
import com.roblescode.chatgpt.data.repository.OpenIARepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOpenIARepository(
        api: OpenIAService
    ) = OpenIARepository(api)


    @Singleton
    @Provides
    fun provideOpenIAService():OpenIAService{
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer $OPEN_IA_KEY")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.openai.com")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenIAService::class.java)
    }
}