package myjin.pro.ahoora.myjin.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import myjin.pro.ahoora.myjin.interfaces.TempListener;
import myjin.pro.ahoora.myjin.models.TempModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerStatus {

    private TempListener tempListener;
    private Context mContaxt;
    private String v = "";

    public ServerStatus(TempListener tempListener, Context context) {
        this.tempListener = tempListener;
        this.mContaxt = context;

    }

    public void IsOkServer() {
        try {
            KotlinApiClient.INSTANCE.getClient().create(ApiInterface.class).IsOkServer().enqueue(new Callback<TempModel>() {
                @Override
                public void onResponse(@NonNull Call<TempModel> call, @NonNull Response<TempModel> response) {

                    try {
                        TempModel tempModel = response.body();
                        assert tempModel != null;
                        v = tempModel.getVal();
                        Log.e("val", v + "");
                        VarableValues.INSTANCE.setNetworkState(true);
                        if (v.equals("ok")) {
                            tempListener.IsOk();
                        } else {
                            tempListener.IsNotOk();
                        }
                    } catch (Exception e) {
                        VarableValues.INSTANCE.setNetworkState(true);
                        tempListener.IsNotOk();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TempModel> call, @NonNull Throwable t) {
                    VarableValues.INSTANCE.setNetworkState(false);
                    tempListener.IsNotOk();

                }
            });

        } catch (Exception e) {
            Toast.makeText(mContaxt, "خطایی رخ داده است", Toast.LENGTH_SHORT).show();
            tempListener.IsNotOk();
        }

    }

}
