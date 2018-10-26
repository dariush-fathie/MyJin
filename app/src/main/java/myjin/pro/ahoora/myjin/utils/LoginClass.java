package myjin.pro.ahoora.myjin.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import myjin.pro.ahoora.myjin.activitys.DetailActivity;
import myjin.pro.ahoora.myjin.activitys.LoginActivity;
import myjin.pro.ahoora.myjin.interfaces.ServerStatusResponse;
import myjin.pro.ahoora.myjin.models.TempModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginClass {
    private String user;
    private String pass;
    private String val = "";
    private Context mContaxt;
    private boolean ch;
    private ServerStatusResponse serverStatusResponse;

    public LoginClass(Context context, ServerStatusResponse serverStatusResponse) {
        this.mContaxt = context;
        pass = "*";
        user = "*";
        ch = false;
        this.serverStatusResponse = serverStatusResponse;
        callBachServer();
    }


    public LoginClass(String user, String pass, Context context) {
        this.user = user;
        this.pass = pass;
        this.mContaxt = context;
        ch = true;
    }


    public void goToLogin() {
        if (user.trim().length() < 4 || pass.trim().length() < 4) {
            Toast.makeText(mContaxt, "نام کاربری یا رمز عبور موجود نیست", Toast.LENGTH_SHORT).show();
        } else if (user.trim().equals("")) {

            Toast.makeText(mContaxt, "نام کاربری خالی است", Toast.LENGTH_SHORT).show();

        } else if (pass.trim().equals("")) {

            Toast.makeText(mContaxt, "رمز عبور خالی است", Toast.LENGTH_SHORT).show();
        } else {
            callBachServer();
        }


    }

    private void callBachServer() {
        Utils.INSTANCE.setYekta();
        String yekta = VarableValues.INSTANCE.getYekta();
        Log.e("DeviceId", yekta);
        try {
            KotlinApiClient.INSTANCE.getClient().create(ApiInterface.class).login(user, pass, yekta).enqueue(new Callback<TempModel>() {
                @Override
                public void onResponse(@NonNull Call<TempModel> call, @NonNull Response<TempModel> response) {
                    TempModel tempModel = response.body();
                    assert tempModel != null;
                    val = tempModel.getVal();
                    Log.e("val", val + "");

                    if (val.equals("noserver")) {
                        //mContaxt.startActivity(new Intent(mContaxt, ServerStatusActivity.class));
                        // todo goto repair
                        if (!ch) {
                            ((DetailActivity) mContaxt).finish();
                        } else {
                            ((LoginActivity) mContaxt).finish();
                        }
                    } else {
                        if (val.equals("noUser") || val.equals("wPass")) {
                            if (ch) {
                                Toast.makeText(mContaxt, "نام کاربری یا رمز عبور موجود نیست", Toast.LENGTH_SHORT).show();
                            }
                        } else if (val.equals("okL")) {
                            if (ch) {
                                Toast.makeText(mContaxt, "ورود انجام شد", Toast.LENGTH_SHORT).show();
                                ((LoginActivity) mContaxt).finish();
                            } else {
                                serverStatusResponse.isOk();
                            }

                        } else if (val.equals("empty")) {
                            serverStatusResponse.notOk();
                        }
                    }


                }

                @Override
                public void onFailure(@NonNull Call<TempModel> call, @NonNull Throwable t) {

                    if (ch) {
                        Toast.makeText(mContaxt, "خطایی رخ داده است", Toast.LENGTH_SHORT).show();

                    } else {
                        serverStatusResponse.notOk();
                    }

                }
            });

        } catch (Exception e) {
            if (ch) {
                Toast.makeText(mContaxt, "خطایی رخ داده است", Toast.LENGTH_SHORT).show();

            } else {
                serverStatusResponse.notOk();
            }
        }

    }
}
