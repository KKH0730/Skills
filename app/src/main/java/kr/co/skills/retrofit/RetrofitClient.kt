package kr.co.skills.retrofit

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private var retrofitClient: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit? {

        val client = OkHttpClient.Builder()

        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.e("test", "RetrofitClient - log: $message")
        }

        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        client.addInterceptor(loggingInterceptor)

        client.connectTimeout(100, TimeUnit.SECONDS)
        client.readTimeout(100, TimeUnit.SECONDS)
        client.writeTimeout(100, TimeUnit.SECONDS)
        client.retryOnConnectionFailure(false)

        val gson = GsonBuilder()
            .setLenient()
            .create()

        if (retrofitClient == null) {
            retrofitClient = Retrofit.Builder()
                .baseUrl("$baseUrl/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client.build())
                .build()
        }
        return retrofitClient
    }
}