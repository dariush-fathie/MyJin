package myjin.pro.ahoora.myjin.utils;

import java.util.List;

import myjin.pro.ahoora.myjin.models.KotlinAboutContactModel;
import myjin.pro.ahoora.myjin.models.KotlinGroupModel;
import myjin.pro.ahoora.myjin.models.KotlinItemModel;
import myjin.pro.ahoora.myjin.models.KotlinMessagesModel;
import myjin.pro.ahoora.myjin.models.KotlinProvCityModel;
import myjin.pro.ahoora.myjin.models.KotlinServicesModel;
import myjin.pro.ahoora.myjin.models.KotlinSignInModel;
import myjin.pro.ahoora.myjin.models.KotlinSlideMainModel;
import myjin.pro.ahoora.myjin.models.KotlinSpecialityModel2;
import myjin.pro.ahoora.myjin.models.SimpleResponseModel;
import myjin.pro.ahoora.myjin.models.TempModel;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("service/getMainGroupList/{provId}/{cityId}")
    Call<List<KotlinGroupModel>> getGroupCount(@Path("provId") int provId, @Path("cityId") int cityId);

    @GET("service/getItems/{gId}/{provId}/{cityId}")
    Call<List<KotlinItemModel>> getItems(@Path("gId") int groupId, @Path("provId") int provId, @Path("cityId") int cityId);

    @GET("service/getSpList/")
    Call<List<KotlinSpecialityModel2>> getSpList();

    @GET("service/search2/{someThing}/{provId}/{cityId}")
    Call<List<KotlinItemModel>> search(@Path("someThing") String searchedText, @Path("provId") int provId, @Path("cityId") int cityId);

    @GET("service/login/{user}/{pass}/{yekta}")
    Call<TempModel> login(@Path("user") String user, @Path("pass") String pass, @Path("yekta") String yekta);

    @GET("service/ac/")
    Call<KotlinAboutContactModel> getAc();

    @GET("service/update/{id}/{lat}/{lng}")
    Call<SimpleResponseModel> updateGeoLocation(@Path("id") int autoId, @Path("lat") double lat, @Path("lng") double lng);

    @GET("service/IsOkServerStatus/")
    Call<TempModel> IsOkServer();

    @GET("service/sliderMain/{slide}")
    Call<List<KotlinSlideMainModel>> sliderMain(@Path("slide") int slide);

    @GET("service/getServicesList/")
    Call<List<KotlinServicesModel>> getServicesList();

    @GET("service/getProvinceAndCitiesList/{cityCount}")
    Call<List<KotlinProvCityModel>> getProvinceAndCitiesList(@Path("cityCount") int cityCount);

    @GET("service/getMessagesList/")
    Call<List<KotlinMessagesModel>> getMessages();

    @FormUrlEncoded
    @POST("sms/src/VerifyLookup.php")
    Call<TempModel>sendSms(@Field("number") String number);



    @FormUrlEncoded
    @POST("service/signin/")
    Call<TempModel>signIn(@Field("p") String number,@Field("f") String fn,@Field("l") String ln,@Field("pr") String pr,@Field("du") String du
            ,@Field("logOut") String logOut,@Field("yekta") String yekta);





    @GET("service/getUserInfo/{number}")
    Call<List<KotlinSignInModel>> getUserInfo(@Path("number") String number);
}
