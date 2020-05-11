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

internal const val PUBLIC_KEY = "8505a155c66a8d185240640ab157be41"
internal const val PRIVATE_KEY = "9dda3d974c57ee4a40f7c75b0d6f395ec61cf9b9"
internal const val TIME_STAMP_KEY = "ts"
internal const val HASH_KEY = "hash"
internal const val API_KEY = "apikey"

object MarvellModule {
    lateinit var retrofit: Retrofit private set
    private var timeStamp = System.currentTimeMillis().toString()

    fun setRetrofit(moviedbEndpoint: MarvelApiEndPoint,
                    logLevel: LoggingInterceptor.Level = LoggingInterceptor.Level.FULL
    ) {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor =
            LoggingInterceptor(Clock.systemDefaultZone(), logLevel)
        builder.addInterceptor(loggingInterceptor)
        builder.addInterceptor { chain ->
            val original = chain.request()
            val url = original.url().newBuilder()
                .addQueryParameter(
                    TIME_STAMP_KEY,
                    timeStamp
                )
                .addQueryParameter(
                    API_KEY,
                    PUBLIC_KEY
                )
                .addQueryParameter(
                    HASH_KEY,
                    generateHash()
                )
                .build()

            val requestBuilder = original.newBuilder().url(url)
            requestBuilder.header("Content-Type", "application/json")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        val okClient = builder.build()
        retrofit = Retrofit.Builder()
            .baseUrl(moviedbEndpoint.url)
            .client(okClient)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun gsonBuilder(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .setPrettyPrinting()
            .create()
    }

    private fun generateHash(): String {
        return (timeStamp + PRIVATE_KEY + PUBLIC_KEY).toMd5()
    }
}