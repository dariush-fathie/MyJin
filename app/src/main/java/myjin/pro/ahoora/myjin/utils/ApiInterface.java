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

    @GET("service/getMainGroupList/{provId}/{cityId}")
    Call<List<KotlinGroupModel>> getGroupCount(@Path("provId") int provId, @Path("cityId") int cityId);

    @GET("service/getItems/{gId}/{provId}/{cityId}")
    Call<List<KotlinItemModel>> getItems(@Path("gId") int groupId, @Path("provId") int provId, @Path("cityId") int cityId);

    @GET("service/getSpList/")
    Call<List<KotlinSpecialityModel>> getSpList();

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

}
