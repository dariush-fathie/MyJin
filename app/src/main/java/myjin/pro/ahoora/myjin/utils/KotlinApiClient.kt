package myjin.pro.ahoora.myjin.utils

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import io.realm.RealmObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KotlinApiClient {

    private val BASE_URL = "http://unary.ir/service/"
    private var retrofit: Retrofit? = null

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
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
            }
            return retrofit!!
        }

}
