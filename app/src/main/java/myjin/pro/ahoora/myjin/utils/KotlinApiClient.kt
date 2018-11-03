package myjin.pro.ahoora.myjin.utils

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import io.realm.RealmObject
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient


object KotlinApiClient {

    private val BASE_URL = "http://myjin.ir/"
    private var retrofit: Retrofit? = null
    internal var logging = HttpLoggingInterceptor()

    val gson = GsonBuilder().setExclusionStrategies(object : ExclusionStrategy {
        override fun shouldSkipClass(clazz: Class<*>?): Boolean {
            return false
        }

        override fun shouldSkipField(f: FieldAttributes?): Boolean {
            return f?.declaredClass == RealmObject::class.java
        }

    }).create()


    val client: Retrofit
        get() {

            logging.level = HttpLoggingInterceptor.Level.BASIC
            val client = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build()

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL).client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
            }
            return retrofit!!
        }

}
