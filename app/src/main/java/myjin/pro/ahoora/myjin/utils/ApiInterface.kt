package myjin.pro.ahoora.myjin.utils

import myjin.pro.ahoora.myjin.models.*
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {

    @get:GET("service/getSpList/")
    val spList: Call<List<KotlinSpecialityModel2>>

    @get:GET("service/ac/")
    val ac: Call<KotlinAboutContactModel>

    @get:GET("service/getServicesList/")
    val servicesList: Call<List<KotlinServicesModel>>

    @get:GET("service/getMessagesList/")
    val messages: Call<List<KotlinMessagesModel>>

    @GET("service/getMainGroupList/{provId}/{cityId}")
    fun getGroupCount(@Path("provId") provId: Int, @Path("cityId") cityId: Int): Call<List<KotlinGroupModel>>

    @GET("service/getItems/{gId}/{provId}/{cityId}")
    fun getItems(@Path("gId") groupId: Int, @Path("provId") provId: Int, @Path("cityId") cityId: Int): Call<List<KotlinItemModel>>

    @GET("service/search2/{someThing}/{provId}/{cityId}")
    fun search(@Path("someThing") searchedText: String, @Path("provId") provId: Int, @Path("cityId") cityId: Int): Call<List<KotlinItemModel>>

    @GET("service/login/{user}/{pass}/{yekta}")
    fun login(@Path("user") user: String, @Path("pass") pass: String, @Path("yekta") yekta: String): Call<TempModel>

    @GET("service/update/{id}/{lat}/{lng}")
    fun updateGeoLocation(@Path("id") autoId: Int, @Path("lat") lat: Double, @Path("lng") lng: Double): Call<SimpleResponseModel>

    @GET("service/IsOkServerStatus/")
    fun IsOkServer(): Call<TempModel>

    @GET("service/sliderMain/{slide}")
    fun sliderMain(@Path("slide") slide: Int): Call<List<KotlinSlideMainModel>>

    @GET("service/getProvinceAndCitiesList/{cityCount}")
    fun getProvinceAndCitiesList(@Path("cityCount") cityCount: Int): Call<List<KotlinProvCityModel>>

    @FormUrlEncoded
    @POST("sms/src/VerifyLookup.php")
    fun sendSms(@Field("number") number: String): Call<TempModel>


    @FormUrlEncoded
    @POST("service/signin/")
    fun signIn(@Field("p") number: String, @Field("f") fn: String, @Field("l") ln: String, @Field("pr") pr: String, @Field("du") du: String, @Field("logOut") logOut: String, @Field("yekta") yekta: String): Call<TempModel>


    @GET("service/getUserInfo/{number}")
    fun getUserInfo(@Path("number") number: String): Call<List<KotlinSignInModel>>
}
