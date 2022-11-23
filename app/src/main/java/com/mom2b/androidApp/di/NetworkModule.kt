package com.mom2b.androidApp.di

import com.mom2b.androidApp.api.Api
import com.mom2b.androidApp.api.ApiService
import com.mom2b.androidApp.utils.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)

        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideLiveDataCallAdapterFactory(): LiveDataCallAdapterFactory = LiveDataCallAdapterFactory()

    @Provides
    @Singleton
    fun provideScalarsConverterFactory(): ScalarsConverterFactory = ScalarsConverterFactory.create()

    @Provides
    @Singleton
    fun provideNullOrEmptyConverterFactory(): Converter.Factory =
        object : Converter.Factory() {
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<out Annotation>,
                retrofit: Retrofit
            ): Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(
                    this,
                    type,
                    annotations
                )

                return Converter { body: ResponseBody ->
                    if (body.contentLength() == 0L) null
                    else nextResponseBodyConverter.convert(body)
                }
            }
        }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        liveDataCallAdapterFactory: LiveDataCallAdapterFactory,
        nullOrEmptyConverterFactory: Converter.Factory,
        scalarsConverterFactory: ScalarsConverterFactory,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(Api.API_ROOT_URL)
            .addConverterFactory(scalarsConverterFactory)
            .addConverterFactory(gsonConverterFactory)
            .addConverterFactory(nullOrEmptyConverterFactory)
            .addCallAdapterFactory(liveDataCallAdapterFactory)

    @Provides
    @Singleton
    fun provideApiService(
        okHttpClient: OkHttpClient,
        retrofitBuilder: Retrofit.Builder
    ): ApiService {
        return retrofitBuilder
            .client(okHttpClient).build()
            .create(ApiService::class.java)
    }

}