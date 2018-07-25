package myjin.pro.ahoora.myjin.utils;

import java.util.List;

import myjin.pro.ahoora.myjin.models.KotlinAboutContactModel;
import myjin.pro.ahoora.myjin.models.KotlinGroupModel;
import myjin.pro.ahoora.myjin.models.KotlinItemModel;
import myjin.pro.ahoora.myjin.models.KotlinMessagesModel;
import myjin.pro.ahoora.myjin.models.KotlinProvCityModel;
import myjin.pro.ahoora.myjin.models.KotlinServicesModel;
import myjin.pro.ahoora.myjin.models.KotlinSlideMainModel;
import myjin.pro.ahoora.myjin.models.KotlinSpecialityModel;
import myjin.pro.ahoora.myjin.models.SimpleResponseModel;
import myjin.pro.ahoora.myjin.models.TempModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("getMainGroupList/{provId}/{cityId}")
    Call<List<KotlinGroupModel>> getGroupCount(@Path("provId") int provId, @Path("cityId") int cityId);

    @GET("getItems/{gId}/{provId}/{cityId}")
    Call<List<KotlinItemModel>> getItems(@Path("gId") int groupId, @Path("provId") int provId, @Path("cityId") int cityId);

    @GET("getSpList/")
    Call<List<KotlinSpecialityModel>> getSpList();

    @GET("search/{someThing}")
    Call<List<KotlinItemModel>> search(@Path("someThing") String searchedText);

    @GET("login/{user}/{pass}/{yekta}")
    Call<TempModel> login(@Path("user") String user, @Path("pass") String pass, @Path("yekta") String yekta);

    @GET("ac/")
    Call<KotlinAboutContactModel> getAc();

    @GET("update/{id}/{lat}/{lng}")
    Call<SimpleResponseModel> updateGeoLocation(@Path("id") int autoId, @Path("lat") double lat, @Path("lng") double lng);

    @GET("IsOkServerStatus/")
    Call<TempModel> IsOkServer();

    @GET("sliderMain/{slide}")
    Call<List<KotlinSlideMainModel>> sliderMain(@Path("slide") int slide);

    @GET("getServicesList/")
    Call<List<KotlinServicesModel>> getServicesList();

    @GET("getProvinceAndCitiesList/{cityCount}")
    Call<List<KotlinProvCityModel>> getProvinceAndCitiesList(@Path("cityCount") int cityCount);

    @GET("getMessagesList/")
    Call<List<KotlinMessagesModel>> getMessages();

}
