package com.lreisdeandrade.marvelservice

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import com.lreisdeandrade.marvelservice.util.toMd5
import okhttp3.OkHttpClient
import org.threeten.bp.Clock
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal const val PUBLIC_KEY = "YOUR-PUBLIC-KEY"
internal const val PRIVATE_KEY = "YOUR-PRIVATE-KEY"
internal const val TIME_STAMP_KEY = "ts"
internal const val HASH_KEY = "hash"
internal const val API_KEY = "apikey"
internal const val LIMIT_KEY = "limit"

const val ITEMS_PER_PAGE = 30

object MarvellModule {
    lateinit var retrofit: Retrofit private set
    private var timeStamp = System.currentTimeMillis().toString()

    fun setRetrofit(
        marveldbEndpoint: MarvelApiEndPoint,
        logLevel: LoggingInterceptor.Level = LoggingInterceptor.Level.FULL
    ) {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor =
            LoggingInterceptor(Clock.systemDefaultZone(), logLevel)
        builder.addInterceptor(loggingInterceptor)
        builder.addInterceptor { chain ->
            val original = chain.request()
            val url = original.url().newBuilder()
                .addQueryParameter(TIME_STAMP_KEY, timeStamp)
                .addQueryParameter(API_KEY, PUBLIC_KEY)
                .addQueryParameter(HASH_KEY, generateHash())
                .addQueryParameter(LIMIT_KEY, ITEMS_PER_PAGE.toString())
                .build()

            val requestBuilder = original.newBuilder().url(url)
            requestBuilder.header("Content-Type", "application/json")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        val okClient = builder.build()
        retrofit = Retrofit.Builder()
            .baseUrl(marveldbEndpoint.url)
            .client(okClient)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    fun gsonBuilder(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .setPrettyPrinting()
            .create()
    }

    fun generateHash(): String {
        return (timeStamp + PRIVATE_KEY + PUBLIC_KEY).toMd5()
    }
}
